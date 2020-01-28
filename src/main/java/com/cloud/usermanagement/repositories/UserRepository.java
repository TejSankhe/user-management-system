package com.cloud.usermanagement.repositories;

import com.cloud.usermanagement.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.lang.String;
import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {
	
	User findByEmailAddress(String emailaddress);

}
