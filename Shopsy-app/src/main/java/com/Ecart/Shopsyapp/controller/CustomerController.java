package com.Ecart.Shopsyapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Ecart.Shopsyapp.dto.Customer;
import com.Ecart.Shopsyapp.dto.ResponseStructure;
import com.Ecart.Shopsyapp.service.CustomerService;

@RestController
@RequestMapping("/customers")
public class CustomerController 
{

	private CustomerService customerService;
	
	   @Autowired
	    public CustomerController(CustomerService customerService) 
	    {
	        this.customerService = customerService;
	    }

	@GetMapping("/findall")
	public ResponseEntity<ResponseStructure<List<Customer>>> getAllCustomer()
	{
		return customerService.getAllCustomer();	
	}

	@GetMapping("/{id}")
	public ResponseEntity<ResponseStructure<Customer>> getCustomerById(@PathVariable("id") Long id)
	{
	return customerService.findById(id);
	}

	@PostMapping("/save")
	public ResponseEntity<ResponseStructure<Customer>> saveCustomer(@RequestBody(required = false) Customer customer) 
	{
		return customerService.saveCustomer(customer);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseStructure<Void>> deleteCustomer(@PathVariable("id") Long id) 
	{
		return customerService.deleteById(id);
	}
	
	@PutMapping("/{id}")
    public ResponseEntity<ResponseStructure<Customer>> updateCustomer(@RequestBody Customer customer, @PathVariable("id") Long id) 
	{
            return customerService.updateCustomer(customer,id);
	}
	
	    @PostMapping("/verify")
	    public ResponseEntity<ResponseStructure<Customer>> verifyCustomer(@RequestParam("email") String email,
	                                                                      @RequestParam("password") String password) {
	        return customerService.verifyCustomer(email, password);
	    }
	    
	    @GetMapping("/confirm")
	    public ResponseEntity<String> confirmAccount(@RequestParam("token") String token) {
	        // Verify the token and confirm the account
	        boolean isAccountConfirmed = customerService.confirmAccount(token);

	        if (isAccountConfirmed) {
	            return ResponseEntity.ok("Account confirmed successfully");
	        } else {
	            return ResponseEntity.badRequest().body("Invalid token or account already confirmed");
	        }
	    }

}
