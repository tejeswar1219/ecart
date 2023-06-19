package com.Ecart.Shopsyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Ecart.Shopsyapp.dto.Product;


public interface ProductRepository extends JpaRepository<Product, Long>
{

}
