package com.cloud.usermanagement.services;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloud.usermanagement.Exceptions.FileStorageException;
import com.cloud.usermanagement.Exceptions.ValidationException;
import com.cloud.usermanagement.models.Bill;
import com.cloud.usermanagement.models.File;
import com.cloud.usermanagement.repositories.FileRepository;
import com.cloud.usermanagement.utilities.FileStorageUtil;
import com.cloud.usermanagement.utilities.ValidationHelper;


@Service
public class FileService {

	@Autowired
	private FileStorageUtil fileStorageUtil;
	
	@Autowired
	private BillService billService;
	
	@Autowired
	private FileRepository fileRepository;
	
	@Autowired
	private ValidationHelper validationHelper;
	
	public File save(@Valid MultipartFile file, Bill bill, String authorName) throws FileStorageException, ValidationException {
		if(!validationHelper.validateAttachmentExtension(file.getOriginalFilename()))
		{
			throw new ValidationException("Only pdf, png, jpg and jpeg file formates are allowed");
		}
		String storedFilePath = fileStorageUtil.storeFile(file);
		File attachment = new File();
		attachment.setFileName(file.getOriginalFilename());
		attachment.setUrl(storedFilePath);
		attachment.setBill(bill);
		attachment.setContentType(file.getContentType());
		attachment.setSize(file.getSize());
		attachment.setOwner(authorName);
		fileRepository.save(attachment);
		return attachment;
	}

	public File getAttachment(String fileId, String billId, String name) {
		Bill bill = billService.getBill(billId, name);
		if(bill!=null)
			return bill.getAttachment();
		return null;
	}

	public Boolean deleteAttachment(String fileId, String billId, String name) throws FileStorageException {
		Bill bill = billService.getBill(billId, name);
		if(bill!=null) {
			fileStorageUtil.deleteFile(bill.getAttachment().getUrl());
			return true;
		}
		return false;

	}
	
	
	

}
