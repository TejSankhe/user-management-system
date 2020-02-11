package com.cloud.usermanagement.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cloud.usermanagement.models.File;


@Repository
public interface FileRepository extends CrudRepository<File, UUID>{
	
}
