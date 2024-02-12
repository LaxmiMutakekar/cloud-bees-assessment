package com.cloudbees.catalogAndPricing.dao;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class PriceDetails {
    @Id
    private Long id;

    private Double price;
    private Double discountPercentage;
    private Double taxRate;

}
