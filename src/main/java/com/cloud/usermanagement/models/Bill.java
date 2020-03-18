package com.cloud.usermanagement.models;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "bill")
public class Bill {
    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @CreatedDate
    @JsonProperty(value = "created_ts", access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'z'", timezone="America/New_York")
    private Date createdTS;
    
    @LastModifiedDate
    @JsonProperty(value = "updated_ts", access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'z'", timezone="America/New_York")
    private Date updatedTS;
    
    @JsonProperty(value = "owner_id", access = JsonProperty.Access.READ_ONLY)
    private UUID ownerID;
    
    @NotBlank(message = "Vendor name is mandatory")
    @JsonProperty(value = "vendor")
    private String vendor;
    
    @NotNull(message = "Bill Date name is mandatory")
    @JsonProperty(value = "bill_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone="America/New_York")
    private Date billDate;
    
    
    @NotNull(message = "Due Date name is mandatory")
    @JsonProperty(value = "due_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone="America/New_York")
    private Date dueDate;
    
    @NotNull(message = "Amount due is mandatory")
    @JsonProperty(value = "amount_due")
    private Double amountDue;
    
    @NotEmpty(message = "Categories are mandatory")
    @JsonProperty(value = "categories")
    private String categories;
    
    @NotNull(message = "Payment Status are mandatory")
    @JsonProperty(value = "paymentStatus")
    private PaymentStatus paymentStatus;
    
    @JsonProperty(value = "attachment", access = JsonProperty.Access.READ_ONLY)
    @OneToOne(mappedBy = "bill")
    private File attachment;
    
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public Date getCreatedTS() {
		return createdTS;
	}
	public void setCreatedTS(Date createdTS) {
		this.createdTS = createdTS;
	}
	public Date getUpdatedTS() {
		return updatedTS;
	}
	public void setUpdatedTS(Date updatedTS) {
		this.updatedTS = updatedTS;
	}
	public UUID getOwnerID() {
		return ownerID;
	}
	public void setOwnerID(UUID ownerID) {
		this.ownerID = ownerID;
	}
	public String getVendor() {
		return vendor;
	}
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
	public Date getBillDate() {
		return billDate;
	}
	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	public double getAmountDue() {
		return amountDue;
	}
	public void setAmountDue(double amountDue) {
		this.amountDue = amountDue;
	}
	public String[] getCategories() {
		return categories.split(",");
	}

	public void setCategories(String[] input) {
		this.categories =StringUtils.arrayToCommaDelimitedString(input);
	}
	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public File getAttachment() {
		return attachment;
	}
	public void setAttachment(File attachment) {
		this.attachment = attachment;
	}
	@Override
	public String toString() {
		return "Bill [id=" + id + ", createdTS=" + createdTS + ", updatedTS=" + updatedTS + ", ownerID=" + ownerID
				+ ", vendor=" + vendor + ", billDate=" + billDate + ", dueDate=" + dueDate + ", amountDue=" + amountDue
				+ ", categories=" + categories + ", paymentStatus=" + paymentStatus + ", attachment=" + attachment
				+ "]";
	}
	
	
 
}
