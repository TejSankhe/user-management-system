package com.cloud.usermanagement.security;

import java.util.Collections;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.cloud.usermanagement.services.UserService;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
	
	@Autowired
	private UserService userService;
	
	private static final Logger logger = LogManager.getLogger(CustomAuthenticationProvider.class);
	
	@Override
    public Authentication authenticate(Authentication authentication) 
      throws AuthenticationException {
  
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
         
        if (userService.authenticateUser(name,password)) {
        	logger.debug("authenticated="+"name:"+name);
            return new UsernamePasswordAuthenticationToken(
              name, password,Collections.emptyList());
        } else {
        	logger.error("Wrong credentials="+"name:"+name);
        	throw new BadCredentialsException("Wrong credentials");
        }
    }
	
	@Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
