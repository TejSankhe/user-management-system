package com.cloud.usermanagement.services;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.eclipse.jdt.internal.compiler.env.IUpdatableModule.UpdateKind;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.usermanagement.Exceptions.ValidationException;
import com.cloud.usermanagement.helper.ValidationHelper;
import com.cloud.usermanagement.models.Bill;
import com.cloud.usermanagement.models.User;
import com.cloud.usermanagement.repositories.BillRepository;
import com.cloud.usermanagement.repositories.UserRepository;

@Service
public class BillService {

	@Autowired
	private BillRepository billRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ValidationHelper validationHelper;

	public Bill save(Bill bill, User user){

		bill.setOwnerID(user.getId());
		return billRepository.save(bill);

	}

	public List<Bill> getBills(String emailAddress) {
		User user= userRepository.findByEmailAddress(emailAddress.toLowerCase());
		return billRepository.findByOwnerID(user.getId());
	}

	public Bill getBill(String id, String name) {
		User user= userRepository.findByEmailAddress(name.toLowerCase());
		return billRepository.findByOwnerIDAndId(user.getId(), UUID.fromString(id));	
	}


	public Bill updateBill(String id, Bill updatedBill, String name) {
		User user= userRepository.findByEmailAddress(name.toLowerCase());
		Bill searchedBill =  billRepository.findByOwnerIDAndId(user.getId(), UUID.fromString(id));
		if(searchedBill!=null) {
			searchedBill.setVendor(updatedBill.getVendor());
			searchedBill.setBillDate(updatedBill.getBillDate());
			searchedBill.setDueDate(updatedBill.getDueDate());
			searchedBill.setAmountDue(updatedBill.getAmountDue());
			searchedBill.setCategories(updatedBill.getCategories());
			searchedBill.setPaymentStatus(updatedBill.getPaymentStatus());
			return billRepository.save(searchedBill);
			
		}
		return null;	
	}
	
	public boolean deleteBill(String id, String name) {
		User user= userRepository.findByEmailAddress(name.toLowerCase());
		Bill bill = billRepository.findByOwnerIDAndId(user.getId(), UUID.fromString(id));
		if(bill!=null) {
			billRepository.delete(bill);
			return true;
		}
		return false;
	}


}
