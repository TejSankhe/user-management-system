package com.cloud.usermanagement.utilities;

import org.springframework.web.multipart.MultipartFile;

import com.cloud.usermanagement.Exceptions.FileStorageException;

public interface FileStorageUtil {
	
	public String storeFile(MultipartFile file) throws FileStorageException;

	public void deleteFile(String fileUrl) throws FileStorageException;
}
