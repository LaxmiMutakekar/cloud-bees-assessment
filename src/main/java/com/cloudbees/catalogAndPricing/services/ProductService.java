package com.cloudbees.catalogAndPricing.services;


import com.cloudbees.catalogAndPricing.dao.PriceAdjustmentType;
import com.cloudbees.catalogAndPricing.dao.PriceDetails;
import com.cloudbees.catalogAndPricing.dao.Product;
import com.cloudbees.catalogAndPricing.exceptions.ProductNotFoundException;
import com.cloudbees.catalogAndPricing.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId));
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public boolean updateProduct(Long productId, Product updatedProduct) {
        if (productRepository.existsById(productId)) {
            productRepository.save(updatedProduct);
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteProduct(Long productId) {
        if (productRepository.existsById(productId)) {
            productRepository.deleteById(productId);
            return true;
        } else {
            return false;
        }
    }

    public Product applyDiscountOrTax(Long productId, PriceAdjustmentType type, double value) {
        Product product = getProductById(productId);
        if (product != null) {
            PriceDetails priceDetails = product.getPriceDetails();
            if (priceDetails != null) {
                switch (type) {
                    case DISCOUNT_VALUE:
                        applyDiscount(priceDetails, value);
                        break;
                    case TAX_VALUE:
                        applyTax(priceDetails, value);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid adjustment type for discount or tax apply  " + type);
                }
                recalculateFinalPrice(priceDetails);
                productRepository.save(product);
                return product;
            }
        }
        log.error("Error while applying discount / tax to product , product not found : {}", productId);
        throw new ProductNotFoundException(productId);
    }

    private void applyDiscount(PriceDetails priceDetails, double discountPercentage) {
        priceDetails.setDiscountPercentage(discountPercentage);
    }

    private void applyTax(PriceDetails priceDetails, double taxRate) {
        priceDetails.setTaxRate(taxRate);
    }

    private void recalculateFinalPrice(PriceDetails priceDetails) {
        double discountedPrice = priceDetails.getPrice() * (1 - (priceDetails.getDiscountPercentage() != null ? priceDetails.getDiscountPercentage() / 100 : 0));
        double finalPriceWithTax = discountedPrice * (1 + (priceDetails.getTaxRate() != null ? priceDetails.getTaxRate() / 100 : 0));
        priceDetails.setPrice(finalPriceWithTax);
    }


}
