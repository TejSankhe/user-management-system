package com.cloud.usermanagement.repositories;

import com.cloud.usermanagement.models.Bill;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.lang.String;
import java.util.UUID;
import java.util.List;


@Repository
public interface BillRepository extends CrudRepository<Bill, UUID> {

	List<Bill> findByOwnerID(UUID ownerid);
	Bill findByOwnerIDAndId(UUID ownerid, UUID id);
	
	
}
