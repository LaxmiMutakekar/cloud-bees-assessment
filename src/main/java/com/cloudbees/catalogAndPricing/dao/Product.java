package com.cloudbees.catalogAndPricing.dao;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pid;
    private String name;
    private String description;
    @OneToOne( targetEntity = PriceDetails.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_fk")
    private PriceDetails priceDetails;
    private int quantityAvailable;
}
