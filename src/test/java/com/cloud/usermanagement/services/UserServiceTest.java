package com.cloud.usermanagement.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.cloud.usermanagement.Exceptions.ValidationException;
import com.cloud.usermanagement.models.User;
import com.cloud.usermanagement.repositories.UserRepository;
import com.cloud.usermanagement.utilities.ValidationHelper;

@SpringBootTest
public class UserServiceTest {

	//	@Autowired
//	private UserRepository userRepository;
//
//	@Test
//	public void TestGetUser() throws Exception
//	{
//		User user= new User();
//		user.setFirstName("Tej");
//		user.setLastName("Sankhe");
//		user.setEmailAddress("tejtest@gmail.com");
//		user.setPassword("Tejtest@123");
//		userRepository.save(user);
//		User userRetrived= userService.getUser(user.getEmailAddress());
//		assertNotNull(userRetrived);
//		assertEquals("Username match", user.getEmailAddress(), (userRetrived).getEmailAddress());
//		userRepository.delete((User) userRetrived);
//	}
//
	@Test
	public void createUserPasswordVali()
	{
		ValidationHelper validationHelper= new ValidationHelper();
		Assert.assertTrue(validationHelper.validatePassword("Sankhe@12345"));
		Assert.assertFalse(validationHelper.validatePassword("5"));

	}
}
