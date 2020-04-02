package com.cloud.usermanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import com.cloud.usermanagement.aws.AmazonSQSClient;
import com.cloud.usermanagement.properties.FileStorageProperties;
import com.cloud.usermanagement.security.CustomAuthenticationProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@SpringBootApplication
@Configuration
@EnableWebSecurity
@EnableConfigurationProperties({
	FileStorageProperties.class
})
public class UsermanagementApplication extends WebSecurityConfigurerAdapter{

	@Autowired
	private CustomAuthenticationProvider customAuthenticationProvider;
	
	@Autowired
	private AmazonSQSClient amazonSQSClient;
	
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
	
	@Bean
	public TaskExecutor taskExecutor() {
	    return new SimpleAsyncTaskExecutor(); 
	}
	
	@Bean
	public CommandLineRunner schedulingRunner(TaskExecutor executor) {
	    return new CommandLineRunner() {
	        public void run(String... args) throws Exception {
	            executor.execute(() -> {
	    		    while(true) {
	    		    	try {
							amazonSQSClient.pollMessage();
						} catch (JsonMappingException e) {
							e.printStackTrace();
						} catch (JsonProcessingException e) {
							e.printStackTrace();
						}
	    		    }
	    		});
	        }
	    };
	}
	
}
