package com.cloudbees.catalogAndPricing.dao;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class PriceDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double price;
    private Double discountPercentage;

    @OneToOne(mappedBy = "priceDetails", cascade = CascadeType.ALL)
    private Product product;
    private Double taxRate;

}
