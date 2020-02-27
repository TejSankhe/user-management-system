package com.cloud.usermanagement.utilities.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.cloud.usermanagement.Exceptions.FileStorageException;
import com.cloud.usermanagement.aws.AmazonClient;
import com.cloud.usermanagement.utilities.FileStorageUtil;


@Component
@Scope(value = "singleton")
@Profile("dev")
public class DevFileStorageUtil implements FileStorageUtil {

	@Autowired
	private AmazonClient amazonClient;

	@Override
	public String storeFile(MultipartFile file) throws FileStorageException {
		return amazonClient.uploadFile(file);
	}

	@Override
	public void deleteFile(String fileUrl) throws FileStorageException {
		amazonClient.deleteFileFromS3Bucket(fileUrl);
	}

}
