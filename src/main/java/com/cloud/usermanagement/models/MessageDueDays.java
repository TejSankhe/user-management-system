package com.cloud.usermanagement.models;

public class MessageDueDays {
	
	private String emailId;
	private int days;
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public int getDays() {
		return days;
	}
	public void setDays(int days) {
		this.days = days;
	}
	public MessageDueDays(String emailId, int days) {
		super();
		this.emailId = emailId;
		this.days = days;
	}
	
	
	

}
