package com.Ecart.Shopsyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Ecart.Shopsyapp.dto.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>
{

	Customer findByEmailAndPassword(String email, String password);

	Customer findByToken(String token);
	
	
	
}
