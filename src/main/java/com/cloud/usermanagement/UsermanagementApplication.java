package com.cloud.usermanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import com.cloud.usermanagement.security.CustomAuthenticationProvider;

@SpringBootApplication
@Configuration
@EnableWebSecurity
public class UsermanagementApplication extends WebSecurityConfigurerAdapter{

	@Autowired
	private CustomAuthenticationProvider customAuthenticationProvider;

	public static void main(String[] args) {
		SpringApplication.run(UsermanagementApplication.class, args);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(customAuthenticationProvider);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests()
		.antMatchers("/user").permitAll()
		.anyRequest().authenticated()
		.and().httpBasic().and().sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
}
