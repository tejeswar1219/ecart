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

import com.Ecart.Shopsyapp.dto.Merchant;
import com.Ecart.Shopsyapp.dto.ResponseStructure;
import com.Ecart.Shopsyapp.service.MerchantService;

@RestController
@RequestMapping("/merchants")
public class MerchantController 
{

	private MerchantService merchantService;
	
	   @Autowired
	    public MerchantController(MerchantService merchantService) 
	    {
	        this.merchantService = merchantService;
	    }

	@GetMapping("/findall")
	public ResponseEntity<ResponseStructure<List<Merchant>>> getAllMerchant()
	{
		return merchantService.getAllMerchant();	
	}

	@GetMapping("/{id}")
	public ResponseEntity<ResponseStructure<Merchant>> getMerchantById(@PathVariable("id") Long id)
	{
	return merchantService.findById(id);
	}

	@PostMapping("/save")
	public ResponseEntity<ResponseStructure<Merchant>> saveMerchant(@RequestBody(required = false) Merchant merchant) 
	{
		return merchantService.saveMerchant(merchant);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseStructure<Void>> deleteMerchant(@PathVariable("id") Long id) 
	{
		return merchantService.deleteById(id);
	}
	
	@PutMapping("/{id}")
 public ResponseEntity<ResponseStructure<Merchant>> updateMerchant(@RequestBody Merchant merchant, @PathVariable("id") Long id) 
	{
         return merchantService.updateMerchant(merchant,id);
	}
	
	    @PostMapping("/verify")
	    public ResponseEntity<ResponseStructure<Merchant>> verifyMerchant(@RequestParam("email") String email,
	                                                                      @RequestParam("password") String password) {
	        return merchantService.verifyMerchant(email, password);
	    }
	    
	    @GetMapping("/confirm")
	    public ResponseEntity<String> confirmAccount(@RequestParam("token") String token) {
	        // Verify the token and confirm the account
	        boolean isAccountConfirmed = merchantService.confirmAccount(token);

	        if (isAccountConfirmed) {
	            return ResponseEntity.ok("Account confirmed successfully");
	        } else {
	            return ResponseEntity.badRequest().body("Invalid token or account already confirmed");
	        }
	    }
}
