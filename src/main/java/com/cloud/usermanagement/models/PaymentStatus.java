package com.cloud.usermanagement.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum PaymentStatus {
	@JsonProperty(value = "paid")
	PAID,
	@JsonProperty(value = "due")
	DUE,
	@JsonProperty(value = "past_due")
	PAST_DUE,
	@JsonProperty(value = "no_payment_required")
	NO_PAYMENT_REQUIRED;
	
//    private final String name;
//
//    private PaymentStatus(String name) {
//        this.name = name;
//    }
}
