package com.cloud.usermanagement.models;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import java.util.Date;
import java.util.UUID;
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @NotBlank(message = "First name is mandatory")
    @JsonProperty(value = "first_name")
    private String firstName;
    @NotBlank(message = "Last name is mandatory")
    @JsonProperty(value = "last_name")
    private String lastName;
    @NotBlank(message = "Email address is mandatory")
    @JsonProperty(value = "email_address")
    @Pattern(message = "Invalid email id", regexp = "^.+@.+\\..+$")
    private String emailAddress;
    @NotBlank(message = "Password is mandatory")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    
    @CreatedDate
    @JsonProperty(value = "account_created")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'z'", timezone="America/New_York")
    private Date accountCreated;
    @LastModifiedDate
    @JsonProperty(value = "account_updated")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'z'", timezone="America/New_York")
    private Date accountUpdated;

    public void setId(UUID id) {
        this.id = id;
    }

    public void setAccountCreated(Date accountCreated) {
        this.accountCreated = accountCreated;
    }

    public void setAccountUpdated(Date accountUpdated) {
        this.accountUpdated = accountUpdated;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public UUID getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public Date getAccountCreated() {
        return accountCreated;
    }

    public Date getAccountUpdated() {
        return accountUpdated;
    }

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", emailAddress="
				+ emailAddress + ", accountCreated=" + accountCreated + ", accountUpdated=" + accountUpdated + "]";
	}



}
