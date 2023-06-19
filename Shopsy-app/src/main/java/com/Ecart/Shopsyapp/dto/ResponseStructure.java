package com.Ecart.Shopsyapp.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ResponseStructure<T> 
{
	@JsonProperty("statusCode")
	private int statusCode;
	
	@JsonProperty("message")
	private String message;
	
	@JsonProperty("data")
	private T data;

}