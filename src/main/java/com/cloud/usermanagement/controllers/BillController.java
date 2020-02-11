package com.cloud.usermanagement.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cloud.usermanagement.Exceptions.ValidationException;
import com.cloud.usermanagement.models.Bill;
import com.cloud.usermanagement.models.User;
import com.cloud.usermanagement.services.BillService;
import com.cloud.usermanagement.services.UserService;

@RestController
public class BillController {

	@Autowired
	private BillService billService;

	@Autowired
	private UserService userService;

	@PostMapping("/bill")
	protected ResponseEntity<Bill> createBill(@Valid @RequestBody Bill bill, Authentication authentication) throws ValidationException {
		if (authentication != null) {
			User user = userService.getUser(authentication.getName());
			return new ResponseEntity<Bill>(billService.save(bill, user), HttpStatus.CREATED);
		} else
			return new ResponseEntity<Bill>(HttpStatus.UNAUTHORIZED);

	}

	@PutMapping("/bill/{id}")
	protected ResponseEntity<Bill> updateBill(@PathVariable String id, @Valid @RequestBody Bill updatedBill,
			Authentication authentication) throws ValidationException {
		if (authentication != null) {
			Bill bill = billService.updateBill(id, updatedBill, authentication.getName());
			if (bill != null)
				return new ResponseEntity<Bill>(bill, HttpStatus.OK);
			else
				return new ResponseEntity(HttpStatus.NOT_FOUND);
		} else
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);

	}

	@GetMapping("/bill/{id}")
	protected ResponseEntity<Bill> getBill(@PathVariable String id, Authentication authentication) {
		if (authentication != null) {
			Bill bill = billService.getBill(id, authentication.getName());
			if (bill != null)
				return new ResponseEntity<Bill>(bill, HttpStatus.OK);
			else
				return new ResponseEntity(HttpStatus.NOT_FOUND);
		} else
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
	}

	@GetMapping("/bills")
	protected ResponseEntity<List<Bill>> getBills(Authentication authentication) {
		if (authentication != null) {
			return new ResponseEntity<List<Bill>>(billService.getBills(authentication.getName()), HttpStatus.OK);
		} else
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
	}

	@DeleteMapping("/bill/{id}")
	protected ResponseEntity deleteBill(@PathVariable String id, Authentication authentication) {
		if (authentication != null) {
			Boolean status = billService.deleteBill(id, authentication.getName());
			if (status)
				return new ResponseEntity(HttpStatus.NO_CONTENT);
			else
				return new ResponseEntity(HttpStatus.NOT_FOUND);
		} else
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
	}

}
