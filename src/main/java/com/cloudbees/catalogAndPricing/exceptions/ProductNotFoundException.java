package com.cloudbees.catalogAndPricing.exceptions;

public class ProductNotFoundException extends RuntimeException{
    public ProductNotFoundException(Long productId) {
        super("Product not found with id: " + productId);
    }
}
