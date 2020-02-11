package com.cloud.usermanagement.utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class ValidationHelper {

	String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";

	String FILE_EXTENSION = "^.*\\.(jpg|png|jpeg|pdf)$";
	public boolean validatePassword(String password) {
		Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
		Matcher matcher = pattern.matcher(password);
		return matcher.matches();

	}
	
	public boolean validateAttachmentExtension(String url) {
		Pattern pattern = Pattern.compile(FILE_EXTENSION);
		Matcher matcher = pattern.matcher(url.toLowerCase());
		return matcher.matches();
		
	}

}
