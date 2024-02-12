package com.cloudbees.catalogAndPricing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private Long pid;
    private String name;
    private String description;
    private double price;
}
