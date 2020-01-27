package com.cloud.usermanagement.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.cloud.usermanagement.Exceptions.ValidationException;
import com.cloud.usermanagement.models.User;
import com.cloud.usermanagement.repositories.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

	@Autowired
	private UserService userService;

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
	public void createUserInvalidEmailAddress()
	{
		User user= new User();
		user.setFirstName("Tej");
		user.setLastName("Sankhe");
		user.setEmailAddress("tejtest@gmail.com");
		user.setPassword("Tejtest123");
		try {
			User userSaved= userService.save(user);
		} catch (ValidationException e) {
			assertEquals(e.getMessage(),"Password should be between 8 and 64 character and it should contain at least one lowercase, uppercase, number, and symbol");
		}

	}
}
