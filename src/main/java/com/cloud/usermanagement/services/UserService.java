package com.cloud.usermanagement.services;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.cloud.usermanagement.Exceptions.ValidationException;
import com.cloud.usermanagement.models.User;
import com.cloud.usermanagement.repositories.UserRepository;
import com.cloud.usermanagement.utilities.PasswordEncryptHelper;
import com.cloud.usermanagement.utilities.ValidationHelper;
import com.timgroup.statsd.StatsDClient;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ValidationHelper validationHelper;
	
	@Autowired
	private PasswordEncryptHelper passwordEncryptHelper;
	
	private static final Logger logger = LogManager.getLogger(UserService.class);
	
	@Autowired
	private StatsDClient statsDClient;
	
	public User save(User user) throws ValidationException {
		
		User searcheduser= userRepository.findByEmailAddress(user.getEmailAddress().toLowerCase());
		if(searcheduser == null)
		{
			if(!validationHelper.validatePassword(user.getPassword()))
			{
				throw new ValidationException("Password should be between 8 and 64 character and it should contain at least one lowercase, uppercase, number, and symbol");
			}
			user.setEmailAddress(user.getEmailAddress().toLowerCase());
			user.setPassword(passwordEncryptHelper.encryptPassword(user.getPassword()));
			long startTime= System.currentTimeMillis();
			userRepository.save(user);
			long endTime= System.currentTimeMillis();
			statsDClient.recordExecutionTime("saveuserquery", endTime-startTime);
		}
		else {
			throw new ValidationException("User already exists");
		}
		logger.info("user saved successful."+user);
		return user;
	}

	
	public void updateUser(User user, String email) throws ValidationException{
		User searchedUser= userRepository.findByEmailAddress(email.toLowerCase());
		if(user.getEmailAddress().equalsIgnoreCase((email)))
		{
			if(user.getAccountCreated()!=null || user.getAccountUpdated()!=null)
			{
				throw new ValidationException("Account created and account updated cannot be modified");
			}
			if(!validationHelper.validatePassword(user.getPassword()))
			{
				throw new ValidationException("Password should be between 8 and 64 character and it should contain at least one lowercase, uppercase, number, and symbol");
			}
			String encryptedPasword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
			searchedUser.setPassword(encryptedPasword);
			searchedUser.setFirstName(user.getFirstName());
			searchedUser.setLastName(user.getLastName());
			logger.info("user updated successful."+searchedUser);
			long startTime= System.currentTimeMillis();
			userRepository.save(searchedUser);
			long endTime= System.currentTimeMillis();
			statsDClient.recordExecutionTime("updateuserquery", endTime-startTime);
		}
		else
		{
			throw new ValidationException("Email address passed in payload doesn't matched with user's credential; unauthorized");
		}
	}

	public boolean authenticateUser(String name, String password) {
		User searcheduser= userRepository.findByEmailAddress(name.toLowerCase());
		if(searcheduser != null)
		{
			return BCrypt.checkpw(password, searcheduser.getPassword());

		}
		return false;
	}


	public User getUser(String emailAddress) {
		long startTime= System.currentTimeMillis();
		User searcheduser= userRepository.findByEmailAddress(emailAddress.toLowerCase());
		long endTime= System.currentTimeMillis();
		statsDClient.recordExecutionTime("getuserquery", endTime-startTime);
		if(searcheduser != null)
		{
			logger.info("get user"+ searcheduser);
			return searcheduser;
		}
		logger.info("user not found");
		return null;
		
	}
	
	
	

}
