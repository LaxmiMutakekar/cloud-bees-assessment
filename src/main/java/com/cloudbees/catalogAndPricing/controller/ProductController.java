package com.cloudbees.catalogAndPricing.controller;

import com.cloudbees.catalogAndPricing.dao.PriceAdjustmentType;
import com.cloudbees.catalogAndPricing.dao.Product;
import com.cloudbees.catalogAndPricing.dto.ProductDto;
import com.cloudbees.catalogAndPricing.services.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Slf4j
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public List<ProductDto> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long productId) {
        try {
            ProductDto product = productService.getProductById(productId);
            return new ResponseEntity<>(product, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("Error in getting the product with id : {}", productId);
            throw e;
        }
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productCreateRequest) {
        ProductDto createdProduct = productService.createProduct(productCreateRequest);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @PutMapping()
    public ResponseEntity<String> updateProduct( @RequestBody Product updatedProduct) {
        try {
            boolean success = productService.updateProduct(updatedProduct);
            if (success) {
                return ResponseEntity.ok("Product updated successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found with id: " + updatedProduct.getPid());
            }
        } catch (RuntimeException e) {
            log.error("Error in updating the product with id: {} error: {}", updatedProduct.getPid(), e.getStackTrace());
            throw e;
        }
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        try {
            boolean success = productService.deleteProduct(productId);
            if (success) {
                return ResponseEntity.ok("Product deleted successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found with id: " + productId);
            }
        } catch (RuntimeException e) {
            log.error("Error in deleting the product with id : {}  error : {}", productId, e.getStackTrace());
            throw e;
        }
    }

    @PostMapping("/{productId}/apply-tax")
    public ResponseEntity<String> applyTax(@PathVariable Long productId, @RequestParam double priceUpdateValue, @RequestParam PriceAdjustmentType priceUpdateType ) {
        try {
            boolean success = productService.applyDiscountOrTax(productId, priceUpdateType, priceUpdateValue);
            if (success) {
                return ResponseEntity.ok("Product applied with tax/ discounts successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found with id: " + productId);
            }
        } catch (RuntimeException e) {
            log.error("Error in applying tax or discount for the product with id : {}  error : {}", productId, e.getStackTrace());
            throw e;
        }
    }
}
