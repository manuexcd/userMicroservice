package tfg.microservice.user.mapper;

import org.springframework.stereotype.Component;

import tfg.microservice.user.dto.UserDTO;
import tfg.microservice.user.model.User;

@Component
public class UserMapperImpl extends GenericMapperImpl<User, UserDTO> implements UserMapper {

	@Override
	public Class<User> getClazz() {
		return User.class;
	}

	@Override
	public Class<UserDTO> getDtoClazz() {
		return UserDTO.class;
	}
}
