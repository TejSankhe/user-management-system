package com.cloud.usermanagement.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;
import java.time.LocalDate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jdt.internal.compiler.env.IUpdatableModule.UpdateKind;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cloud.usermanagement.Exceptions.FileStorageException;
import com.cloud.usermanagement.Exceptions.ValidationException;
import com.cloud.usermanagement.aws.AmazonSQSClient;
import com.cloud.usermanagement.models.Bill;
import com.cloud.usermanagement.models.User;
import com.cloud.usermanagement.repositories.BillRepository;
import com.cloud.usermanagement.repositories.FileRepository;
import com.cloud.usermanagement.repositories.UserRepository;
import com.cloud.usermanagement.utilities.ValidationHelper;
import com.timgroup.statsd.StatsDClient;

@Service
public class BillService {

	@Autowired
	private BillRepository billRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ValidationHelper validationHelper;

	@Autowired
	private FileService fileService;
	
	@Autowired
	private StatsDClient statsDClient;
	
	@Autowired
	private AmazonSQSClient amazonSQSClient;
	
	@Value("${spring.profiles.active}")
	private String activeProfile;
	
	private static final Logger logger = LogManager.getLogger(BillService.class);
	
	public Bill save(Bill bill, User user) throws ValidationException{
		if(Double.compare(bill.getAmountDue(), 0.01)<0){
			throw new ValidationException("amount_due should be atleast 0.01");
		}
		bill.setOwnerID(user.getId());
		logger.info("Bill saved successfully");
		return billRepository.save(bill);

	}

	public List<Bill> getBills(String emailAddress) {
		long startTime= System.currentTimeMillis();
		User user= userRepository.findByEmailAddress(emailAddress.toLowerCase());
		List<Bill> bills = billRepository.findByOwnerID(user.getId());
		long endTime= System.currentTimeMillis();
		statsDClient.recordExecutionTime("getbillsquery", endTime-startTime);
		logger.info("Bills");
		return bills;
	}

	public Bill getBill(String id, String name) {
		long startTime= System.currentTimeMillis();
		User user= userRepository.findByEmailAddress(name.toLowerCase());
		Bill bill = billRepository.findByOwnerIDAndId(user.getId(), UUID.fromString(id));
		long endTime= System.currentTimeMillis();
		statsDClient.recordExecutionTime("getbillsquery", endTime-startTime);
		logger.info("Bill");
		return bill;	
	}


	public Bill updateBill(String id, Bill updatedBill, String name) throws ValidationException {
		if(Double.compare(updatedBill.getAmountDue(), 0.01)<0){
			throw new ValidationException("amount_due should be atleast 0.01");
		}
		User user= userRepository.findByEmailAddress(name.toLowerCase());
		Bill searchedBill =  billRepository.findByOwnerIDAndId(user.getId(), UUID.fromString(id));
		if(searchedBill!=null) {
			searchedBill.setVendor(updatedBill.getVendor());
			searchedBill.setBillDate(updatedBill.getBillDate());
			searchedBill.setDueDate(updatedBill.getDueDate());
			searchedBill.setAmountDue(updatedBill.getAmountDue());
			searchedBill.setCategories(updatedBill.getCategories());
			searchedBill.setPaymentStatus(updatedBill.getPaymentStatus());
			logger.info("bill updated");
			return billRepository.save(searchedBill);
			
		}
		logger.info("bill not found");
		return null;	
	}
	
	public boolean deleteBill(String id, String name) throws FileStorageException, ValidationException {
		User user= userRepository.findByEmailAddress(name.toLowerCase());
		Bill bill = billRepository.findByOwnerIDAndId(user.getId(), UUID.fromString(id));
		if(bill!=null) {
			if(bill.getAttachment()!=null)
			fileService.deleteAttachment(bill.getAttachment().getId().toString(), id, name);
			long startTime= System.currentTimeMillis();
			billRepository.delete(bill);
			long endTime= System.currentTimeMillis();
			statsDClient.recordExecutionTime("deletebillquery", endTime-startTime);
			logger.info("bill deleted");
			return true;
		}
		logger.info("bill not found");
		return false;
	}

	public List<Bill> getDueBills(String x, String name) {
		long startTime= System.currentTimeMillis();
		User user= userRepository.findByEmailAddress(name.toLowerCase());
		List<Bill> bills = billRepository.findByOwnerID(user.getId());
		List<Bill> result = new ArrayList<>();
        Date today = new Date();
        System.out.println("date : " + today);     
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        cal.add(Calendar.DATE, Integer.parseInt(x));
        Date dueDate = cal.getTime();
        System.out.println("duedate : " + dueDate);
		long endTime= System.currentTimeMillis();
		for(Bill bill : bills) {
			if(today.compareTo(bill.getDueDate())<=0 && bill.getDueDate().compareTo(dueDate)<=0)
				result.add(bill);
		}
		statsDClient.recordExecutionTime("getbillsdueDateQuery", endTime-startTime);
		logger.info("get due Bills");
		amazonSQSClient.sendMessage("tejsankhe03@gmail.com");
		amazonSQSClient.pollMessage();
		return result;
	}


}
