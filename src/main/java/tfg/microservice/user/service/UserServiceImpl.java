package tfg.microservice.user.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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

//	@Autowired
//	private OrderService orderService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private MailSender mailSender;

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

//	@Override
//	public Collection<Order> getOrders(long id) throws UserNotFoundException {
//		return repository.findById(id).map(User::getOrders).orElseThrow(UserNotFoundException::new);
//	}

//	@Override
//	public Order createTemporalOrder(long id, Order order) throws UserNotFoundException {
//		Optional<User> user = repository.findById(id);
//		if (user.isPresent()) {
//			order.setOrderStatus(Constants.ORDER_STATUS_TEMPORAL);
//			order.setUser(user.get());
//			return orderService.createTemporalOrder(order);
//		} else {
//			throw new UserNotFoundException();
//		}
//	}
//
//	@Override
//	public Order updateOrder(long id, Order order) throws UserNotFoundException {
//		Optional<User> user = repository.findById(id);
//		if (user.isPresent()) {
//			order.setUser(user.get());
//			return orderService.updateOrder(order);
//		} else {
//			throw new UserNotFoundException();
//		}
//	}
//
//	@Override
//	public Order cancelOrder(long id, long orderId) throws UserNotFoundException, OrderNotFoundException {
//		Optional<User> user = repository.findById(id);
//		if (user.isPresent()) {
//			return orderService.cancelOrder(orderId);
//		} else {
//			throw new UserNotFoundException();
//		}
//	}

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
			Map<Object, Object> params = new HashMap<>();
			params.put(Constants.TEMPLATE_PARAM_FULLNAME, userToUpdate.getFullName());
			mailSender.sendEmail(userToUpdate.getEmail(), Constants.SUBJECT_USER_REGISTERED,
					Constants.TEMPLATE_USER_REGISTERED, params);
			return repository.save(userToUpdate);
		} else {
			throw new UserNotFoundException();
		}
	}
}
