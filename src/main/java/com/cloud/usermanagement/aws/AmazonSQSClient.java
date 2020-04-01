package com.cloud.usermanagement.aws;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.AmazonSQSException;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.SendMessageBatchRequest;
import com.amazonaws.services.sqs.model.SendMessageBatchRequestEntry;
import com.amazonaws.services.sqs.model.SendMessageRequest;

@Service
@Profile("dev")
public class AmazonSQSClient {
	final String QUEUE_NAME = "mySQSQueue";
    final AmazonSQS sqs =  AmazonSQSClientBuilder.defaultClient();
    final String queueUrl = sqs.getQueueUrl(QUEUE_NAME).getQueueUrl();
    
    @Autowired
    private AmazonSNSTopicClient amazonSNSTopicClient;
    
    public void sendMessage(String message) {
    	SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(message);
        sqs.sendMessage(send_msg_request);
        System.out.println("message send:"+message);
    }
    
    public void pollMessage() {
    	List<Message> messages = sqs.receiveMessage(queueUrl).getMessages();
    	for (Message m : messages) {
    		System.out.println("message to publish:"+m.getBody());
    		amazonSNSTopicClient.publishMessage(m.getBody());
            sqs.deleteMessage(queueUrl, m.getReceiptHandle());
            System.out.println("delete msg");
        }
    	
    }
	
	

}
