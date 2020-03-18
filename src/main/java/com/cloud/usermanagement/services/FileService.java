package com.cloud.usermanagement.services;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloud.usermanagement.Exceptions.FileStorageException;
import com.cloud.usermanagement.Exceptions.ValidationException;
import com.cloud.usermanagement.models.Bill;
import com.cloud.usermanagement.models.File;
import com.cloud.usermanagement.repositories.BillRepository;
import com.cloud.usermanagement.repositories.FileRepository;
import com.cloud.usermanagement.utilities.CommonUtil;
import com.cloud.usermanagement.utilities.FileStorageUtil;
import com.cloud.usermanagement.utilities.ValidationHelper;
import com.timgroup.statsd.StatsDClient;


@Service
public class FileService {

	@Autowired
	private FileStorageUtil fileStorageUtil;
	
	@Autowired
	private BillService billService;
	
	@Autowired
	private BillRepository billRepository;
	
	@Autowired
	private FileRepository fileRepository;
	
	@Autowired
	private ValidationHelper validationHelper;
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private StatsDClient statsDClient;
	
	private static final Logger logger = LogManager.getLogger(FileService.class);
	
	public File save(@Valid MultipartFile file, Bill bill, String authorName) throws FileStorageException, ValidationException, NoSuchAlgorithmException, IOException {
		if(!validationHelper.validateAttachmentExtension(file.getOriginalFilename()))
		{
			throw new ValidationException("Only pdf, png, jpg and jpeg file formates are allowed");
		}
		if(bill.getAttachment()!=null) {
			throw new ValidationException("attachment is already present for bill please delete it first");
		}
		String storedFilePath = fileStorageUtil.storeFile(file);
		File attachment = new File();
		attachment.setFileName(file.getOriginalFilename());
		attachment.setUrl(storedFilePath);
		attachment.setBill(bill);
		attachment.setContentType(file.getContentType());
		attachment.setSize(file.getSize());
		attachment.setOwner(authorName);
		attachment.setHash(commonUtil.computeMD5Hash(file.getBytes()));
		long startTime= System.currentTimeMillis();
		fileRepository.save(attachment);
		long endTime= System.currentTimeMillis();
		statsDClient.recordExecutionTime("addFileS3", endTime-startTime);
		logger.info("file saved successfully");
		return attachment;
	}

	public File getAttachment(String fileId, String billId, String name) {
		Bill bill = billService.getBill(billId, name);
		if(bill!=null) {
			File file= bill.getAttachment();
			if(file!=null && file.getId().toString().compareTo(fileId)==0)
			{
				logger.info("get file");
				return file;
			}
		}
		logger.info("no file found");
		return null;
	}

	public Boolean deleteAttachment(String fileId, String billId, String name) throws FileStorageException, ValidationException {
		
		Bill bill = billService.getBill(billId, name);
		if(bill!=null) {
			File file = bill.getAttachment();
			if(file==null) {
				throw new ValidationException("No attachment present to delete");
			}
			if(file.getId().toString().compareTo(fileId)==0)
			{
				fileStorageUtil.deleteFile(bill.getAttachment().getUrl());
				bill.setAttachment(null);
				long startTime= System.currentTimeMillis();
				billRepository.save(bill);
				long endTime= System.currentTimeMillis();
				statsDClient.recordExecutionTime("savebillquery", endTime-startTime);
				fileRepository.delete(file);
				logger.info("file deleted successfully");
				return true;
			}
		
		}
		return false;

	}
	
	
	

}
