package com.cloud.usermanagement.repositories;

import com.cloud.usermanagement.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.lang.String;
import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
	
	User findByEmailAddress(String emailaddress);

}
