package com.Ecart.Shopsyapp.service;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.Ecart.Shopsyapp.dao.CustomerDao;
import com.Ecart.Shopsyapp.dto.Address;
import com.Ecart.Shopsyapp.dto.Customer;
import com.Ecart.Shopsyapp.dto.ResponseStructure;

@Service
public class CustomerService 
{
	private final CustomerDao customerDao;
    private final EmailService emailService;
    
    @Value("${app.confirmation-url}")
    private String confirmationUrl;

    @Autowired
    public CustomerService(CustomerDao customerDao, EmailService emailService) {
        this.customerDao = customerDao;
        this.emailService = emailService;
    }

	public ResponseEntity<ResponseStructure<Customer>> saveCustomer(Customer customer) 
	{
	    try {
            if (customer != null) {
                List<Address> addresses = customer.getAddresses();
              
                if (addresses != null && !addresses.isEmpty()) {
                    for (Address address : addresses) {
                        address.setCustomer(customer);
                    }
                }
                
                // Generate a unique confirmation token
                String token = UUID.randomUUID().toString();
                
                // Set the token and confirmed fields
                customer.setToken(token);
                customer.setConfirmed(false);
                
                // Save the customer using the customerDao
                customerDao.saveCustomer(customer);
                
                // Create the response structure for a successful save
                ResponseStructure<Customer> response = new ResponseStructure<>();
                response.setStatusCode(HttpStatus.CREATED.value());
                response.setMessage("Customer saved successfully");
                response.setData(customer);
                
                // Create the confirmation URL
                String confirmationLink = confirmationUrl + "?token=" + token;
                
                // Send confirmation email
                String email = customer.getEmail();
                String subject = "Account Confirmation";
                String text = "Thank you for creating an account. Please click on the following link to confirm your account: " + confirmationLink;
                emailService.sendConfirmationEmail(email, subject, text);
                
                // Return a ResponseEntity with the response structure and the appropriate HTTP status
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                // Create the response structure for a null customer
                ResponseStructure<Customer> response = new ResponseStructure<>();
                response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                response.setMessage("Customer is null");
                response.setData(null);
                
                // Return a ResponseEntity with the response structure and the appropriate HTTP status
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            // If an error occurs during the saving process, handle the exception and return an error response
            ResponseStructure<Customer> errorResponse = new ResponseStructure<>();
            errorResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.setMessage("Error saving customer: " + e.getMessage());
            errorResponse.setData(null);
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
	
	public boolean confirmAccount(String token) {
        return customerDao.confirmAccount(token);
    }

	public ResponseEntity<ResponseStructure<Customer>> findById(long id)
	{
		try {
			// Find the customer using the customerDao
			Customer customer = customerDao.findCustomer(id).get();

			// Create the response structure
			ResponseStructure<Customer> response = new ResponseStructure<>();
			if (customer != null) 
			{
				response.setStatusCode(HttpStatus.OK.value());
				response.setMessage("Customer found");
				response.setData(customer);
			} else {
				response.setStatusCode(HttpStatus.NOT_FOUND.value());
				response.setMessage("Customer not found");
				response.setData(null);
			}

			// Return a ResponseEntity with the response structure and the appropriate HTTP status
			return ResponseEntity.status(response.getStatusCode()).body(response);
		} 
		catch (Exception e) {
			// If an error occurs during the finding process, handle the exception and return an error response
			ResponseStructure<Customer> errorResponse = new ResponseStructure<>();
			errorResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			errorResponse.setMessage("Error finding customer: " + e.getMessage());
			errorResponse.setData(null);

			return ResponseEntity.status(errorResponse.getStatusCode()).body(errorResponse);
		}
	}


	public ResponseEntity<ResponseStructure<List<Customer>>> getAllCustomer() 
	{ 
		try 
		{
			// Retrieve all customers using the customerDao or your data access layer
			List<Customer> customers = customerDao.getAllCustomer();

			// Create the response structure
			ResponseStructure<List<Customer>> responseStructure = new ResponseStructure<>();
			if (!customers.isEmpty()) {
				responseStructure.setStatusCode(HttpStatus.OK.value());
				responseStructure.setMessage("Customers found");
				responseStructure.setData(customers);
			} else 
			
			{
				responseStructure.setStatusCode(HttpStatus.NOT_FOUND.value());
				responseStructure.setMessage("No customers found");
				responseStructure.setData(null);
			}

			// Return a ResponseEntity with the response structure and the appropriate HTTP status
			return ResponseEntity.status(responseStructure.getStatusCode()).body(responseStructure);
		} 
		
		catch (Exception e) 
		{
			// If an error occurs during the retrieval process, handle the exception and return an error response
			ResponseStructure<List<Customer>> errorResponse = new ResponseStructure<>();
			errorResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			errorResponse.setMessage("Error retrieving customers: " + e.getMessage());
			errorResponse.setData(null);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}

	public ResponseEntity<ResponseStructure<Void>> deleteById(long id) 
	{

		try {
			// Find the customer using the customerDao or your data access layer
			Customer customer = customerDao.findCustomer(id).get();

			if (customer != null) 
			{
				// Delete the customer using the customerDao or your data access layer
				customerDao.deleteCustomer(id);

				// Create the response structure for a successful deletion
				ResponseStructure<Void> response = new ResponseStructure<>();
				response.setStatusCode(HttpStatus.OK.value());
				response.setMessage("Customer deleted successfully");
				response.setData(null); // No specific data for successful deletion

				// Return a ResponseEntity with the response structure and the appropriate HTTP status
				return ResponseEntity.status(HttpStatus.OK).body(response);
			} 
			else 
			{
				// Create the response structure for a customer not found
				ResponseStructure<Void> response = new ResponseStructure<>();
				response.setStatusCode(HttpStatus.NOT_FOUND.value());
				response.setMessage("Customer not found");
				response.setData(null);

				// Return a ResponseEntity with the response structure and the appropriate HTTP status
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}
		}
		catch (Exception e) 
		{
			// If an error occurs during the deletion process, handle the exception and return an error response
			ResponseStructure<Void> errorResponse = new ResponseStructure<>();
			errorResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			errorResponse.setMessage("Error deleting customer: " + e.getMessage());
			errorResponse.setData(null);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}
	

	public ResponseEntity<ResponseStructure<Customer>> updateCustomer(Customer customer, long id) {
		  try {
		        // Find the customer using the customerDao
		        Customer existingCustomer = customerDao.findCustomer(id).get();

		        if (existingCustomer != null) {
		            // Update the existing customer with the new data
		            existingCustomer.setFirstName(customer.getFirstName());
		            existingCustomer.setLastName(customer.getLastName());
		            existingCustomer.setEmail(customer.getEmail());
		            existingCustomer.setPhone(customer.getPhone());
		            existingCustomer.setPassword(customer.getPassword());
		            existingCustomer.setAge(customer.getAge());
		            existingCustomer.setGender(customer.getGender());
		            existingCustomer.setAddresses(customer.getAddresses());

		            // Perform the update using the customerDao or your data access layer
		            customerDao.saveCustomer(existingCustomer);

		            // Create the response structure for a successful update
		            ResponseStructure<Customer> response = new ResponseStructure<>();
		            response.setStatusCode(HttpStatus.OK.value());
		            response.setMessage("Customer updated successfully");
		            response.setData(existingCustomer);

		            // Return a ResponseEntity with the response structure and the appropriate HTTP status
		            return ResponseEntity.status(HttpStatus.OK).body(response);
		        } else {
		            // Create the response structure for a customer not found
		            ResponseStructure<Customer> response = new ResponseStructure<>();
		            response.setStatusCode(HttpStatus.NOT_FOUND.value());
		            response.setMessage("Customer not found");
		            response.setData(null);

		            // Return a ResponseEntity with the response structure and the appropriate HTTP status
		            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		        }
		    } 
		  catch (Exception e) 
		  {
		        // If an error occurs during the update process, handle the exception and return an error response
		        ResponseStructure<Customer> errorResponse = new ResponseStructure<>();
		        errorResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		        errorResponse.setMessage("Error updating customer: " + e.getMessage());
		        errorResponse.setData(null);

		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		    }
		}
	


	    public ResponseEntity<ResponseStructure<Customer>> verifyCustomer(String email, String password) {
	        try {
	            Customer customer = customerDao.verifyCustomer(email, password);

	            // Create the response structure
	            ResponseStructure<Customer> response = new ResponseStructure<>();

	            if (customer != null) {
	                response.setStatusCode(HttpStatus.OK.value());
	                response.setMessage("Customer verified");
	                response.setData(null);
	            } else {
	                response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
	                response.setMessage("Invalid credentials");
	                response.setData(null);
	            }

	            // Return a ResponseEntity with the response structure and the appropriate HTTP status
	            return ResponseEntity.status(response.getStatusCode()).body(response);
	        } catch (Exception e) {
	            // If an error occurs during the verification process, handle the exception and return an error response
	            ResponseStructure<Customer> errorResponse = new ResponseStructure<>();
	            errorResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
	            errorResponse.setMessage("Error verifying customer: " + e.getMessage());
	            errorResponse.setData(null);

	            return ResponseEntity.status(errorResponse.getStatusCode()).body(errorResponse);
	        }
	    }
	}










