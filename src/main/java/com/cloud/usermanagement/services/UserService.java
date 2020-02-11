package com.cloud.usermanagement.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.cloud.usermanagement.Exceptions.ValidationException;
import com.cloud.usermanagement.models.User;
import com.cloud.usermanagement.repositories.UserRepository;
import com.cloud.usermanagement.utilities.PasswordEncryptHelper;
import com.cloud.usermanagement.utilities.ValidationHelper;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ValidationHelper validationHelper;
	
	@Autowired
	private PasswordEncryptHelper passwordEncryptHelper;
	
	
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
			userRepository.save(user);
		}
		else {
			throw new ValidationException("User already exists");
		}
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
			userRepository.save(searchedUser);
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
		User searcheduser= userRepository.findByEmailAddress(emailAddress.toLowerCase());
		if(searcheduser != null)
		{
			return searcheduser;
		}
		return null;
		
	}
	
	
	

}
