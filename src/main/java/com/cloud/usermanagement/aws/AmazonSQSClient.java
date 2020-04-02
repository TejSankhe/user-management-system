package com.cloud.usermanagement.aws;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.cloud.usermanagement.models.Bill;
import com.cloud.usermanagement.models.MessageBills;
import com.cloud.usermanagement.models.MessageDueDays;
import com.cloud.usermanagement.models.User;
import com.cloud.usermanagement.repositories.BillRepository;
import com.cloud.usermanagement.repositories.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Profile("dev")
public class AmazonSQSClient {
	final String QUEUE_NAME = "mySQSQueue";
    final AmazonSQS sqs =  AmazonSQSClientBuilder.defaultClient();
    final String queueUrl = sqs.getQueueUrl(QUEUE_NAME).getQueueUrl();
    
    @Autowired
    private AmazonSNSTopicClient amazonSNSTopicClient;
    
	@Autowired
    private ObjectMapper objectMapper;
	
	@Autowired
	private BillRepository billRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Value("${domain_name}")
	private String domainName;
	
    public void sendMessage(String message) {
    	SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(message);
        sqs.sendMessage(send_msg_request);
        System.out.println("message send:"+message);
    }
    
    public void pollMessage() throws JsonMappingException, JsonProcessingException {
    	List<Message> messages = sqs.receiveMessage(queueUrl).getMessages();
    	for (Message m : messages) {
    		MessageDueDays messageDueDays = objectMapper.readValue(m.getBody(), MessageDueDays.class);
    		List<String> bills = getDueBillsHelper(messageDueDays.getDays(), messageDueDays.getEmailId());
    		MessageBills messageBills = new MessageBills(messageDueDays.getEmailId(), bills);
    		amazonSNSTopicClient.publishMessage(objectMapper.writeValueAsString(messageBills));
            sqs.deleteMessage(queueUrl, m.getReceiptHandle());
            System.out.println("delete msg");
        }	
    }
    
    public List<String> getDueBillsHelper(int x, String name) {
		User user= userRepository.findByEmailAddress(name.toLowerCase());
		List<Bill> bills = billRepository.findByOwnerID(user.getId());
		List<String> result = new ArrayList<>();
        Date today = new Date();
        System.out.println("date : " + today);     
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        cal.add(Calendar.DATE, x);
        Date dueDate = cal.getTime();
        System.out.println("duedate : " + dueDate);
		long endTime= System.currentTimeMillis();
		for(Bill bill : bills) {
			if(today.compareTo(bill.getDueDate())<=0 && bill.getDueDate().compareTo(dueDate)<=0)
				result.add("http://"+domainName+":80/v1/bill/"+bill.getId());
		}
		return result;
	}
	
	

}
