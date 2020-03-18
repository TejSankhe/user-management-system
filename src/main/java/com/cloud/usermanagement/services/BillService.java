package com.cloud.usermanagement.services;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jdt.internal.compiler.env.IUpdatableModule.UpdateKind;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.usermanagement.Exceptions.FileStorageException;
import com.cloud.usermanagement.Exceptions.ValidationException;
import com.cloud.usermanagement.models.Bill;
import com.cloud.usermanagement.models.User;
import com.cloud.usermanagement.repositories.BillRepository;
import com.cloud.usermanagement.repositories.FileRepository;
import com.cloud.usermanagement.repositories.UserRepository;
import com.cloud.usermanagement.utilities.ValidationHelper;

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
		User user= userRepository.findByEmailAddress(emailAddress.toLowerCase());
		List<Bill> bills = billRepository.findByOwnerID(user.getId());
		logger.info("Bills");
		return bills;
	}

	public Bill getBill(String id, String name) {
		User user= userRepository.findByEmailAddress(name.toLowerCase());
		Bill bill = billRepository.findByOwnerIDAndId(user.getId(), UUID.fromString(id));
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
			billRepository.delete(bill);
			logger.info("bill deleted");
			return true;
		}
		logger.info("bill not found");
		return false;
	}


}
