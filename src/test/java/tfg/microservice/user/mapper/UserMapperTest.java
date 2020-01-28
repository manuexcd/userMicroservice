package tfg.microservice.user.mapper;

import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import tfg.microservice.user.dto.UserDTO;
import tfg.microservice.user.model.User;

@RunWith(MockitoJUnitRunner.class)
public class UserMapperTest {
	@Mock
	private ModelMapper modelMapper;

	@InjectMocks
	private UserMapperImpl mapper;

	@Test
	public void testMapDtoToEntity() {
		UserDTO dto = new UserDTO();
		given(modelMapper.map(dto, User.class)).willReturn(new User());
		assertNotNull(mapper.mapDtoToEntity(dto));
	}

	@Test
	public void testMapEntityToDto() {
		User user = new User();
		given(modelMapper.map(user, UserDTO.class)).willReturn(new UserDTO());
		assertNotNull(mapper.mapEntityToDto(user));
	}

	@Test
	public void testMapDtoPageToEntityPage() {
		assertNotNull(mapper.mapDtoPageToEntityPage(Page.empty()));
	}

	@Test
	public void testMapEntityPageToDtoPage() {
		assertNotNull(mapper.mapEntityPageToDtoPage(Page.empty()));
	}

	@Test
	public void testMapDtoListToEntityList() {
		assertNotNull(mapper.mapDtoListToEntityList(new ArrayList<UserDTO>()));
	}

	@Test
	public void testMapEntityListToDtoList() {
		assertNotNull(mapper.mapEntityListToDtoList(new ArrayList<User>()));
	}
}
