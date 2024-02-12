package com.cloudbees.catalogAndPricing.dto;

import com.cloudbees.catalogAndPricing.dao.Product;
import org.springframework.stereotype.Service;

import java.util.function.Function;
@Service
public class ProductDtoMapper implements Function<Product, ProductDto> {
    @Override
    public ProductDto apply(Product product) {
        return new ProductDto(product.getPid(),
                product.getName(),
                product.getDescription(),
                product.getPriceDetails().getPrice());
    }
}
