package com.Ecart.Shopsyapp.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.Ecart.Shopsyapp.dto.Address;
import com.Ecart.Shopsyapp.dto.Customer;
import com.Ecart.Shopsyapp.repository.AddressRepository;
import com.Ecart.Shopsyapp.repository.CustomerRepository;

@Repository
public class CustomerDao 
{
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private AddressRepository repo;

	
	
	public Customer saveCustomer(Customer customer)
	{
		return customerRepository.save(customer);
	}
	
	public Optional<Customer> findCustomer(long id)
	{
		return customerRepository.findById(id);
	}
	
	public List<Customer> getAllCustomer() 
	{
		return customerRepository.findAll();
	}
	
	public void deleteCustomer(long id) 
	{
		customerRepository.deleteById(id);
	}
	
	public Customer updateCustomer(Customer customer, long id) 
	{
		Customer revCustomer = findCustomer(id).get();
		if (revCustomer != null) {
			customer.setId(id);
			return customerRepository.save(customer);
		}
		return null;
	}
	
	  public Customer verifyCustomer(String email, String password) 
	  {
	        return customerRepository.findByEmailAndPassword(email, password);
	  }

	public List<Address> saveAddress(List<Address> address) 
	{
        return repo.saveAll(address);		
	}


	
	  public boolean confirmAccount(String token) {
	        Customer customer = customerRepository.findByToken(token);

	        if (customer != null && !customer.isConfirmed()) {
	            customer.setConfirmed(true);
	            customer.setToken(null);
	            customerRepository.save(customer);
	            return true;
	        }

	        return false;
	    }
	
}
