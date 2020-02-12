package com.cloud.usermanagement.controllers;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cloud.usermanagement.Exceptions.FileStorageException;
import com.cloud.usermanagement.Exceptions.ValidationException;
import com.cloud.usermanagement.models.Bill;
import com.cloud.usermanagement.models.File;
import com.cloud.usermanagement.models.User;
import com.cloud.usermanagement.services.BillService;
import com.cloud.usermanagement.services.FileService;
import com.cloud.usermanagement.services.UserService;

@RestController
@RequestMapping("bill")
public class FileController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BillService billService;
	
	@Autowired
	private FileService fileService;
	
	
	@PostMapping("/{billId}/file")
	protected ResponseEntity<File> createBill(@Valid @RequestParam("billAttachment") MultipartFile file, @PathVariable String billId, Authentication authentication) throws FileStorageException, ValidationException, NoSuchAlgorithmException, IOException {
		if (authentication != null) {
			User user = userService.getUser(authentication.getName());
			Bill bill = billService.getBill(billId, authentication.getName());
					if (bill != null) {
						return new ResponseEntity<File>(fileService.save(file,bill,authentication.getName()), HttpStatus.CREATED);
				
					}
					else
						return new ResponseEntity(HttpStatus.NOT_FOUND);
		} else
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

	}
	
	@GetMapping("/{billId}/file/{fileId}")
	protected ResponseEntity<File> getAttachment(@PathVariable String billId, @PathVariable String fileId, Authentication authentication) {
		if (authentication != null) {
			File file = fileService.getAttachment(fileId, billId, authentication.getName());
			if (file != null)
				return new ResponseEntity<File>(file, HttpStatus.OK);
			else
				return new ResponseEntity(HttpStatus.NOT_FOUND);
		} else
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
	}
	
	@DeleteMapping("/{billId}/file/{fileId}")
	protected ResponseEntity deleteAttachment(@PathVariable String billId, @PathVariable String fileId, Authentication authentication) throws FileStorageException, ValidationException {
		if (authentication != null) {
			Boolean status = fileService.deleteAttachment(fileId, billId, authentication.getName());
			if (status)
				return new ResponseEntity(HttpStatus.NO_CONTENT);
			else
				return new ResponseEntity(HttpStatus.NOT_FOUND);
		} else
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
	}

}
