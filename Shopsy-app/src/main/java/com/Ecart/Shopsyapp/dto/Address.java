package com.Ecart.Shopsyapp.dto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address
{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String flatNo;
	private String street;
	private String city;
	private String state;
	private String country;
	private int pincode;
	
	    @ManyToOne
	    @JoinColumn(name = "customer_id")
	    private Customer customer;	    
}
