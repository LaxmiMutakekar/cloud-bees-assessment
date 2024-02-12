package com.cloudbees.catalogAndPricing.controller;

import com.cloudbees.catalogAndPricing.dao.PriceAdjustmentType;
import com.cloudbees.catalogAndPricing.dao.Product;
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
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long productId) {
        try {
            Product product = productService.getProductById(productId);
            return new ResponseEntity<>(product, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("Error in getting the product with id : {}", productId);
            throw e;
        }
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<String> updateProduct(@PathVariable Long productId, @RequestBody Product updatedProduct) {
        try {
            boolean success = productService.updateProduct(productId, updatedProduct);
            if (success) {
                return ResponseEntity.ok("Product updated successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found with id: " + productId);
            }
        } catch (RuntimeException e) {
            log.error("Error in updating the product with id: {} error: {}", productId, e.getStackTrace());
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
    public ResponseEntity<Product> applyTax(@PathVariable Long productId, @RequestParam double priceUpdateValue, @RequestParam PriceAdjustmentType priceUpdateType ) {
        Product updatedProduct = productService.applyDiscountOrTax(productId, priceUpdateType, priceUpdateValue);
        return ResponseEntity.ok(updatedProduct);
    }
}
