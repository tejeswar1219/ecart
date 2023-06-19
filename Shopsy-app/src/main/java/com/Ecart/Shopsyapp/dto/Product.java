package com.Ecart.Shopsyapp.dto;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product 
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long Id;
    
  private String name;
  private double price;
  private String image;
  private int quantity;


  @ManyToOne
  @JoinColumn(name = "merchant_id")
  private Merchant merchant;
  
}
