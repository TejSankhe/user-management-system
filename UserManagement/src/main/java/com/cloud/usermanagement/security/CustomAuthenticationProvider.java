package com.cloud.usermanagement.security;

import java.util.Collections;

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
	
	@Override
    public Authentication authenticate(Authentication authentication) 
      throws AuthenticationException {
  
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
         
        if (userService.authenticateUser(name,password)) {
            return new UsernamePasswordAuthenticationToken(
              name, password,Collections.emptyList());
        } else {
        	throw new BadCredentialsException("Wrong credentials");
        }
    }
	
	@Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
