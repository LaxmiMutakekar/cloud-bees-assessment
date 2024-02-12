package com.cloudbees.catalogAndPricing.services;


import com.cloudbees.catalogAndPricing.dao.PriceAdjustmentType;
import com.cloudbees.catalogAndPricing.dao.PriceDetails;
import com.cloudbees.catalogAndPricing.dao.Product;
import com.cloudbees.catalogAndPricing.dto.ProductDaoMapper;
import com.cloudbees.catalogAndPricing.dto.ProductDto;
import com.cloudbees.catalogAndPricing.dto.ProductDtoMapper;
import com.cloudbees.catalogAndPricing.exceptions.ProductNotFoundException;
import com.cloudbees.catalogAndPricing.repository.ProductPriceRepository;
import com.cloudbees.catalogAndPricing.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductPriceRepository productPriceRepository;
    @Autowired
    private ProductDtoMapper productDtoMapper;

    @Autowired
    private ProductDaoMapper productDaoMapper;

    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream().map(productDtoMapper).collect(Collectors.toList());
    }

    public ProductDto getProductById(Long productId) {
        return productRepository.findById(productId).map(productDtoMapper).orElseThrow(() -> new ProductNotFoundException(productId));
    }

    public ProductDto createProduct(ProductDto product) {
        return productDtoMapper.apply(productRepository.save(productDaoMapper.apply(product)));
    }

    public boolean updateProduct(Product updatedProduct) {
        if (productRepository.existsById(updatedProduct.getPid())) {
            productRepository.save(updatedProduct);
            return true;
        }
       return false;
    }

    public boolean deleteProduct(Long productId) {
        if (productRepository.existsById(productId)) {
            productRepository.deleteById(productId);
            return true;
        }
       return false;
    }

    public boolean applyDiscountOrTax(Long productId, PriceAdjustmentType type, double value) {
        PriceDetails priceDetails = productPriceRepository.getReferenceById(productId);
        if (priceDetails != null) {
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
                productPriceRepository.save(priceDetails);
                return true;
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
