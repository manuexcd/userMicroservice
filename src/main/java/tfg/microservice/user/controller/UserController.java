package tfg.microservice.user.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tfg.microservice.user.dto.UserDTO;
import tfg.microservice.user.exception.EmailExistsException;
import tfg.microservice.user.exception.UserNotFoundException;
import tfg.microservice.user.mapper.UserMapper;
import tfg.microservice.user.model.Constants;
import tfg.microservice.user.model.User;
import tfg.microservice.user.service.UserService;

@RestController
@RequestMapping(value = Constants.PATH_USERS)
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserMapper mapper;

//	@Autowired
//	private OrderMapper orderMapper;
//
//	@Autowired
//	private OrderService orderService;

	@GetMapping
	public ResponseEntity<Page<UserDTO>> getAllUsers(
			@RequestParam(defaultValue = Constants.PAGINATION_DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = Constants.PAGINATION_DEFAULT_SIZE) int pageSize) {
		return new ResponseEntity<>(
				mapper.mapEntityPageToDtoPage(userService.getAllUsers(PageRequest.of(page, pageSize))), HttpStatus.OK);
	}

	@GetMapping(value = Constants.PARAM_ID)
	public ResponseEntity<UserDTO> getUser(@PathVariable long id) throws UserNotFoundException {
		return Optional.ofNullable(userService.getUser(id))
				.map(user -> new ResponseEntity<>(mapper.mapEntityToDto(user), HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@GetMapping(value = Constants.PATH_EMAIL + Constants.PARAM_EMAIL)
	public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
		return Optional.ofNullable(userService.getUserByEmail(email))
				.map(user -> new ResponseEntity<>(mapper.mapEntityToDto(user), HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@GetMapping(value = Constants.PATH_SEARCH + Constants.PARAM)
	public ResponseEntity<Page<UserDTO>> getUsersByParam(@PathVariable String param,
			@RequestParam(defaultValue = Constants.PAGINATION_DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = Constants.PAGINATION_DEFAULT_SIZE) int pageSize) {
		return new ResponseEntity<>(
				mapper.mapEntityPageToDtoPage(userService.getUsersByParam(param, PageRequest.of(page, pageSize))),
				HttpStatus.OK);
	}

//	@GetMapping(value = Constants.PARAM_ID + Constants.PATH_ORDERS)
//	public ResponseEntity<Page<OrderDTO>> getOrdersByUser(@PathVariable long id,
//			@RequestParam(defaultValue = Constants.PAGINATION_DEFAULT_PAGE) int page,
//			@RequestParam(defaultValue = Constants.PAGINATION_DEFAULT_SIZE) int pageSize) {
//		try {
//			return new ResponseEntity<>(orderMapper.mapEntityPageToDtoPage(
//					orderService.getOrdersByUser(id, PageRequest.of(page, pageSize))), HttpStatus.OK);
//		} catch (UserNotFoundException e) {
//			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//		}
//	}
//
//	@PostMapping(value = Constants.PARAM_ID + Constants.PATH_ORDERS)
//	public ResponseEntity<OrderDTO> createTemporalOrder(@PathVariable long id, @RequestBody OrderDTO dto) {
//		try {
//			return new ResponseEntity<>(
//					orderMapper.mapEntityToDto(userService.createTemporalOrder(id, orderMapper.mapDtoToEntity(dto))),
//					HttpStatus.OK);
//		} catch (UserNotFoundException e) {
//			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//		}
//	}
//
//	@PutMapping(value = Constants.PARAM_ID + Constants.PATH_ORDERS)
//	public ResponseEntity<OrderDTO> updateOrder(@PathVariable long id, @RequestBody OrderDTO dto) {
//		try {
//			return new ResponseEntity<>(
//					orderMapper.mapEntityToDto(userService.updateOrder(id, orderMapper.mapDtoToEntity(dto))),
//					HttpStatus.CREATED);
//		} catch (UserNotFoundException e) {
//			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//		}
//	}
//
//	@PutMapping(value = Constants.PARAM_ID + Constants.PATH_ORDERS + "/cancel" + Constants.PARAM_ORDER_ID)
//	public ResponseEntity<OrderDTO> cancelOrder(@PathVariable long id, @PathVariable long orderId) {
//		try {
//			return new ResponseEntity<>(orderMapper.mapEntityToDto(userService.cancelOrder(id, orderId)),
//					HttpStatus.CREATED);
//		} catch (Exception e) {
//			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//		}
//	}

	@PostMapping(value = Constants.PATH_SIGN_IN)
	public ResponseEntity<UserDTO> registerUserAccount(@RequestBody UserDTO dto) {
		User registered = null;
		try {
			registered = userService.registerNewUserAccount(mapper.mapDtoToEntity(dto));
		} catch (EmailExistsException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(mapper.mapEntityToDto(registered), HttpStatus.CREATED);
	}

	@PostMapping(value = "/confirm/{id}")
	public ResponseEntity<UserDTO> confirmUser(@PathVariable long id) {
		try {
			return new ResponseEntity<>(mapper.mapEntityToDto(userService.confirmUser(id)), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO dto) {
		try {
			userService.getUser(dto.getId());
			return new ResponseEntity<>(mapper.mapEntityToDto(userService.updateUser(mapper.mapDtoToEntity(dto))),
					HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
