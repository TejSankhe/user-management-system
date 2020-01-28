package com.cloud.usermanagement.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cloud.usermanagement.Exceptions.ValidationException;
import com.cloud.usermanagement.models.User;
import com.cloud.usermanagement.services.UserService;

@RestController
@RequestMapping("user")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping()
	protected ResponseEntity<User> createUser(@Valid @RequestBody User user) throws ValidationException {
		return new ResponseEntity<User>(userService.save(user), HttpStatus.CREATED);
	}

	@PutMapping("/self")
	protected ResponseEntity<String> updateUser(@Valid @RequestBody User updateUser, Authentication authentication)
			throws ValidationException {
		if (authentication != null) {
			userService.updateUser(updateUser, authentication.getName());
			return new ResponseEntity<String>("User updated successfully", HttpStatus.NO_CONTENT);
		}
		else
			return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);

	}

	@GetMapping("/self")
	protected ResponseEntity<User> getUser(Authentication authentication) throws ValidationException {
		if (authentication != null)
			return new ResponseEntity<User>(userService.getUser(authentication.getName()), HttpStatus.OK);
		else
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

	}
}
