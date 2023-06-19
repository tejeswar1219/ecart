package com.Ecart.Shopsyapp.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.Ecart.Shopsyapp.dto.Product;
import com.Ecart.Shopsyapp.dto.Customer;
import com.Ecart.Shopsyapp.dto.Merchant;
import com.Ecart.Shopsyapp.repository.ProductRepository;
import com.Ecart.Shopsyapp.repository.MerchantRepository;

@Repository
public class MerchantDao 
{
	@Autowired
	private MerchantRepository merchantRepository;
	
	@Autowired
	private ProductRepository productRepository;

	
	
	public Merchant saveMerchant(Merchant merchant)
	{
		return merchantRepository.save(merchant);
	}
	
	public Optional<Merchant> findMerchant(long id)
	{
		return merchantRepository.findById(id);
	}
	
	public List<Merchant> getAllMerchant() 
	{
		return merchantRepository.findAll();
	}
	
	public void deleteMerchant(long id) 
	{
		merchantRepository.deleteById(id);
	}
	
	public Merchant updateMerchant(Merchant merchant, long id) 
	{
		Merchant revMerchant = findMerchant(id).get();
		if (revMerchant != null) {
			merchant.setId(id);
			return merchantRepository.save(merchant);
		}
		return null;
	}
	
	  public Merchant verifyMerchant(String email, String password) 
	  {
	        return merchantRepository.findByEmailAndPassword(email, password);
	  }

	public List<Product> saveProduct(List<Product> product) 
	{
        return productRepository.saveAll(product);		
	}
	
	  public boolean confirmAccount(String token) {
	        Merchant merchant = merchantRepository.findByToken(token);

	        if (merchant != null && !merchant.isConfirmed()) {
	            merchant.setConfirmed(true);
	            merchant.setToken(null);
	            merchantRepository.save(merchant);
	            return true;
	        }

	        return false;
	    }
	
}
