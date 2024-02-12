package com.cloudbees.catalogAndPricing.dto;

import com.cloudbees.catalogAndPricing.dao.PriceDetails;
import com.cloudbees.catalogAndPricing.dao.Product;
import org.springframework.stereotype.Service;

import java.util.function.Function;
@Service
public class ProductDaoMapper implements Function<ProductDto, Product> {
    @Override
    public Product apply(ProductDto productDto) {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        PriceDetails priceDetails = new PriceDetails();
        priceDetails.setPrice(productDto.getPrice());
        product.setPriceDetails(priceDetails);
        return product;
    }
}
