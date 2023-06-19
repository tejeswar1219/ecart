package com.Ecart.Shopsyapp.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer
{

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	
	    private String firstName;
	    
	    private String lastName;
	    
	    @Column(unique = true)
	    private long phone;
	    
	    @Column(unique = true)
	    private String email;
	    
	    private String password;
	    private int age;
	    private String gender;
	    
	    private String token;
	    
	    private boolean confirmed;
	    
	    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	    @JsonIgnoreProperties("customer")
	    private List<Address> addresses;
	    
}
