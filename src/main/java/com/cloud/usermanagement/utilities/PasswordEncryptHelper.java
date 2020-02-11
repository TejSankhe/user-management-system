package com.cloud.usermanagement.utilities;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncryptHelper {
	public String encryptPassword(String password) {
		String strongSalt = BCrypt.gensalt(10);
		return BCrypt.hashpw(password, strongSalt);
		
	}
}
