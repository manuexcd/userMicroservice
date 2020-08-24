package tfg.microservice.user.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import tfg.microservice.user.exception.EmailExistsException;
import tfg.microservice.user.exception.UserNotFoundException;
import tfg.microservice.user.mail.MailSender;
import tfg.microservice.user.model.Constants;
import tfg.microservice.user.model.User;
import tfg.microservice.user.repository.UserRepository;

@Service("userManager")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository repository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private MailSender mailSender;

	@Value("${google.credentials}")
	private String credentialsContent;

	@Value("${google.bucket-name}")
	private String bucketName;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

	@Transactional
	@Override
	public User registerNewUserAccount(User user) throws EmailExistsException {

		if (emailExist(user.getEmail())) {
			throw new EmailExistsException("There is an account with that email adress: " + user.getEmail());
		}

		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setConfirmed(Boolean.FALSE);
		Map<Object, Object> params = new HashMap<>();
		params.put(Constants.TEMPLATE_PARAM_FULLNAME, user.getFullName());
		params.put(Constants.TEMPLATE_PARAM_PHONE, user.getPhone());
		params.put(Constants.TEMPLATE_PARAM_EMAIL, user.getEmail());
		params.put(Constants.TEMPLATE_PARAM_ADDRESS, user.getAddress());
		mailSender.sendEmail(user.getEmail(), Constants.SUBJECT_USER_REGISTERED, Constants.TEMPLATE_USER_REGISTERED,
				params);
		return repository.save(user);
	}

	public boolean emailExist(String email) {
		User user = repository.findByEmail(email);
		if (user != null)
			return repository.existsById(user.getId());
		else
			return false;
	}

	@Override
	public Page<User> getAllUsers(Pageable pagination) {
		return repository.findByOrderByName(pagination);
	}

	@Override
	public User getUser(long id) throws UserNotFoundException {
		return repository.findById(id).orElseThrow(UserNotFoundException::new);
	}

	@Override
	public User getUserByEmail(String email) {
		return repository.findByEmail(email);
	}

	@Override
	public Page<User> getUsersByParam(String param, Pageable pagination) {
		return Optional.ofNullable(param).map(parameter -> repository.findByParam(parameter, pagination))
				.orElse(repository.findAll(pagination));
	}

	@Override
	public User confirmUser(long id) throws UserNotFoundException {
		Optional<User> optional = repository.findById(id);
		if (optional.isPresent()) {
			User user = optional.get();
			user.setConfirmed(Boolean.TRUE);
			return repository.save(user);
		} else {
			throw new UserNotFoundException();
		}
	}

	@Override
	public void deleteUser(long id) {
		repository.deleteById(id);
	}

	@Override
	public User updateUser(User user) throws UserNotFoundException {
		Optional<User> optionalUserToUpdate = repository.findById(user.getId());
		if (optionalUserToUpdate.isPresent()) {
			User userToUpdate = optionalUserToUpdate.get();
			userToUpdate.setName(user.getName());
			userToUpdate.setSurname(user.getSurname());
			userToUpdate.setPhone(user.getPhone());
			userToUpdate.setEmail(user.getEmail());
			userToUpdate.setAddress(user.getAddress());
			userToUpdate.setImageUrl(user.getImageUrl());
			Map<Object, Object> params = new HashMap<>();
			params.put(Constants.TEMPLATE_PARAM_FULLNAME, userToUpdate.getFullName());
			params.put(Constants.TEMPLATE_PARAM_PHONE, userToUpdate.getPhone());
			params.put(Constants.TEMPLATE_PARAM_EMAIL, userToUpdate.getEmail());
			params.put(Constants.TEMPLATE_PARAM_ADDRESS, userToUpdate.getAddress());
			mailSender.sendEmail(userToUpdate.getEmail(), Constants.SUBJECT_USER_UPDATED,
					Constants.TEMPLATE_USER_UPDATED, params);
			return repository.save(userToUpdate);
		} else {
			throw new UserNotFoundException();
		}
	}

	@Override
	public String addImage(MultipartFile file) {
		return uploadFile(file, bucketName);
	}

	private String uploadFile(MultipartFile file, final String bucketName) {
		try {
			checkFileExtension(file.getOriginalFilename());
			final String fileName = System.currentTimeMillis() + file.getOriginalFilename();

			GoogleCredentials credentials = GoogleCredentials.fromStream(new ByteArrayInputStream(credentialsContent.getBytes()))
					.createScoped(Collections.singletonList("https://www.googleapis.com/auth/cloud-platform"));
			Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();

			BlobInfo blobInfo = storage.create(BlobInfo.newBuilder(bucketName, fileName).build(), file.getBytes());

			return blobInfo.getMediaLink();
		} catch (IOException | ServletException e) {
			LOGGER.debug("IOException", e);
		}
		return null;
	}

	private void checkFileExtension(String fileName) throws ServletException {
		if (fileName != null && !fileName.isEmpty() && fileName.contains(".")) {
			String[] allowedExt = { ".jpg", ".jpeg", ".png", ".gif" };
			for (String ext : allowedExt) {
				if (fileName.endsWith(ext)) {
					return;
				}
			}
			throw new ServletException("file must be an image");
		}
	}
}
