package com.Ecart.Shopsyapp.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.Ecart.Shopsyapp.dao.MerchantDao;
import com.Ecart.Shopsyapp.dto.Product;
import com.Ecart.Shopsyapp.dto.Merchant;
import com.Ecart.Shopsyapp.dto.ResponseStructure;

@Service
public class MerchantService 
{

	private final MerchantDao merchantDao;
    private final EmailService emailService;
    
    @Value("${app.confirmation-url}")
    private String confirmationUrl;

    @Autowired
    public MerchantService(MerchantDao merchantDao, EmailService emailService) {
        this.merchantDao = merchantDao;
        this.emailService = emailService;
    }

	public ResponseEntity<ResponseStructure<Merchant>> saveMerchant(Merchant merchant) 
	{
	    try 
	    {    
	    	if (merchant != null) 
         {
              List<Product> products = merchant.getProducts();
          
               if(products!=null && !products.isEmpty())
               {
            	   for(Product product:products)
            	  {
            		product.setMerchant(merchant);
            	  }
               }
               String token = UUID.randomUUID().toString();
               
               // Set the token and confirmed fields
               merchant.setToken(token);
               merchant.setConfirmed(false);
               
               // Save the merchant using the merchantDao
               merchantDao.saveMerchant(merchant);
               
               // Create the response structure for a successful save
               ResponseStructure<Merchant> response = new ResponseStructure<>();
               response.setStatusCode(HttpStatus.CREATED.value());
               response.setMessage("Merchant saved successfully");
               response.setData(merchant);
               
               // Create the confirmation URL
               String confirmationLink = confirmationUrl + "?token=" + token;
               
               // Send confirmation email
               String email = merchant.getEmail();
               String subject = "Account Confirmation";
               String text = "Thank you for creating an account. Please click on the following link to confirm your account: " + confirmationLink;
               emailService.sendConfirmationEmail(email, subject, text);
               

	            // Return a ResponseEntity with the response structure and the appropriate HTTP status
	            return ResponseEntity.status(HttpStatus.CREATED).body(response);
	        } 
	    	else {
	            // Create the response structure for a null merchant
	            ResponseStructure<Merchant> response = new ResponseStructure<>();
	            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
	            response.setMessage("Merchant is null");
	            response.setData(null);

	            // Return a ResponseEntity with the response structure and the appropriate HTTP status
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	        }
	        }
	        
	        
	        catch (Exception e) {
	        // If an error occurs during the saving process, handle the exception and return an error response
	        ResponseStructure<Merchant> errorResponse = new ResponseStructure<>();
	        errorResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
	        errorResponse.setMessage("Error saving merchant: " + e.getMessage());
	        errorResponse.setData(null);

	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	    }
	}
	
	

	public boolean confirmAccount(String token) {
        return merchantDao.confirmAccount(token);
    }

	public ResponseEntity<ResponseStructure<Merchant>> findById(long id)
	{
		try {
			// Find the merchant using the merchantDao
			Merchant merchant = merchantDao.findMerchant(id).get();

			// Create the response structure
			ResponseStructure<Merchant> response = new ResponseStructure<>();
			if (merchant != null) 
			{
				response.setStatusCode(HttpStatus.OK.value());
				response.setMessage("Merchant found");
				response.setData(merchant);
			} else {
				response.setStatusCode(HttpStatus.NOT_FOUND.value());
				response.setMessage("Merchant not found");
				response.setData(null);
			}

			// Return a ResponseEntity with the response structure and the appropriate HTTP status
			return ResponseEntity.status(response.getStatusCode()).body(response);
		} 
		catch (Exception e) {
			// If an error occurs during the finding process, handle the exception and return an error response
			ResponseStructure<Merchant> errorResponse = new ResponseStructure<>();
			errorResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			errorResponse.setMessage("Error finding merchant: " + e.getMessage());
			errorResponse.setData(null);

			return ResponseEntity.status(errorResponse.getStatusCode()).body(errorResponse);
		}
	}


	public ResponseEntity<ResponseStructure<List<Merchant>>> getAllMerchant() 
	{ 
		try 
		{
			// Retrieve all merchants using the merchantDao or your data access layer
			List<Merchant> merchants = merchantDao.getAllMerchant();

			// Create the response structure
			ResponseStructure<List<Merchant>> responseStructure = new ResponseStructure<>();
			if (!merchants.isEmpty()) {
				responseStructure.setStatusCode(HttpStatus.OK.value());
				responseStructure.setMessage("Merchants found");
				responseStructure.setData(merchants);
			} else 
			
			{
				responseStructure.setStatusCode(HttpStatus.NOT_FOUND.value());
				responseStructure.setMessage("No merchants found");
				responseStructure.setData(null);
			}

			// Return a ResponseEntity with the response structure and the appropriate HTTP status
			return ResponseEntity.status(responseStructure.getStatusCode()).body(responseStructure);
		} 
		
		catch (Exception e) 
		{
			// If an error occurs during the retrieval process, handle the exception and return an error response
			ResponseStructure<List<Merchant>> errorResponse = new ResponseStructure<>();
			errorResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			errorResponse.setMessage("Error retrieving merchants: " + e.getMessage());
			errorResponse.setData(null);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}

	public ResponseEntity<ResponseStructure<Void>> deleteById(long id) 
	{

		try {
			// Find the merchant using the merchantDao or your data access layer
			Merchant merchant = merchantDao.findMerchant(id).get();

			if (merchant != null) 
			{
				// Delete the merchant using the merchantDao or your data access layer
				merchantDao.deleteMerchant(id);

				// Create the response structure for a successful deletion
				ResponseStructure<Void> response = new ResponseStructure<>();
				response.setStatusCode(HttpStatus.OK.value());
				response.setMessage("Merchant deleted successfully");
				response.setData(null); // No specific data for successful deletion

				// Return a ResponseEntity with the response structure and the appropriate HTTP status
				return ResponseEntity.status(HttpStatus.OK).body(response);
			} 
			else 
			{
				// Create the response structure for a merchant not found
				ResponseStructure<Void> response = new ResponseStructure<>();
				response.setStatusCode(HttpStatus.NOT_FOUND.value());
				response.setMessage("Merchant not found");
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
			errorResponse.setMessage("Error deleting merchant: " + e.getMessage());
			errorResponse.setData(null);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}
	

	public ResponseEntity<ResponseStructure<Merchant>> updateMerchant(Merchant merchant, long id) {
		  try {
		        // Find the merchant using the merchantDao
		        Merchant existingMerchant = merchantDao.findMerchant(id).get();

		        if (existingMerchant != null) {
		            // Update the existing merchant with the new data
		            existingMerchant.setFirstName(merchant.getFirstName());
		            existingMerchant.setLastName(merchant.getLastName());
		            existingMerchant.setEmail(merchant.getEmail());
		            existingMerchant.setPhone(merchant.getPhone());
		            existingMerchant.setPassword(merchant.getPassword());
		            existingMerchant.setProducts(merchant.getProducts());

		            // Perform the update using the merchantDao or your data access layer
		            merchantDao.saveMerchant(existingMerchant);

		            // Create the response structure for a successful update
		            ResponseStructure<Merchant> response = new ResponseStructure<>();
		            response.setStatusCode(HttpStatus.OK.value());
		            response.setMessage("Merchant updated successfully");
		            response.setData(existingMerchant);

		            // Return a ResponseEntity with the response structure and the appropriate HTTP status
		            return ResponseEntity.status(HttpStatus.OK).body(response);
		        } else {
		            // Create the response structure for a merchant not found
		            ResponseStructure<Merchant> response = new ResponseStructure<>();
		            response.setStatusCode(HttpStatus.NOT_FOUND.value());
		            response.setMessage("Merchant not found");
		            response.setData(null);

		            // Return a ResponseEntity with the response structure and the appropriate HTTP status
		            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		        }
		    } 
		  catch (Exception e) 
		  {
		        // If an error occurs during the update process, handle the exception and return an error response
		        ResponseStructure<Merchant> errorResponse = new ResponseStructure<>();
		        errorResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		        errorResponse.setMessage("Error updating merchant: " + e.getMessage());
		        errorResponse.setData(null);

		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		    }
		}
	


	    public ResponseEntity<ResponseStructure<Merchant>> verifyMerchant(String email, String password) {
	        try {
	            Merchant merchant = merchantDao.verifyMerchant(email, password);

	            // Create the response structure
	            ResponseStructure<Merchant> response = new ResponseStructure<>();

	            if (merchant != null) {
	                response.setStatusCode(HttpStatus.OK.value());
	                response.setMessage("Merchant verified");
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
	            ResponseStructure<Merchant> errorResponse = new ResponseStructure<>();
	            errorResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
	            errorResponse.setMessage("Error verifying merchant: " + e.getMessage());
	            errorResponse.setData(null);

	            return ResponseEntity.status(errorResponse.getStatusCode()).body(errorResponse);
	        }
	    }
	}
