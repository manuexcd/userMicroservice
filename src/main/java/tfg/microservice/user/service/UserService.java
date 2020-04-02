package tfg.microservice.user.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import tfg.microservice.user.exception.EmailExistsException;
import tfg.microservice.user.exception.UserNotFoundException;
import tfg.microservice.user.model.User;

public interface UserService {
	public User registerNewUserAccount(User user) throws EmailExistsException;

	public Page<User> getAllUsers(Pageable pagination);

	public User getUser(long id) throws UserNotFoundException;

	public User getUserByEmail(String email);

	public Page<User> getUsersByParam(String param, Pageable pagination);

	public void deleteUser(long id);

	public boolean emailExist(String email);

	public User confirmUser(long id) throws UserNotFoundException;

	public User updateUser(User user) throws UserNotFoundException;

	public String addImage(MultipartFile file);
}
