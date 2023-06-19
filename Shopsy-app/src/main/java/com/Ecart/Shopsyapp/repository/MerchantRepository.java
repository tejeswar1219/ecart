package com.Ecart.Shopsyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Ecart.Shopsyapp.dto.Merchant;


public interface MerchantRepository extends JpaRepository<Merchant, Long>
{

	Merchant findByEmailAndPassword(String email, String password);

	Merchant findByToken(String token);

}
