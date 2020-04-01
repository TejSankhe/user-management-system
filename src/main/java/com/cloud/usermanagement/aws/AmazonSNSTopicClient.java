package com.cloud.usermanagement.aws;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;

@Service
@Profile("dev")
public class AmazonSNSTopicClient {
	
	final AmazonSNSClient snsClient = new AmazonSNSClient();
	
	@Value("${sns.topic_arn}")
	private String SNSTopicARN;
	
	public void publishMessage(String message) {
		PublishRequest publishRequest = new PublishRequest(SNSTopicARN, message);
		PublishResult publishResponse = snsClient.publish(publishRequest);
		System.out.println("published MessageId: " + publishResponse.getMessageId());
	}

}
