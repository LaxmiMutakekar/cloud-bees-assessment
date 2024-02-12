package com.cloudbees.catalogAndPricing.service;

import com.cloudbees.catalogAndPricing.dao.PriceAdjustmentType;
import com.cloudbees.catalogAndPricing.dao.PriceDetails;
import com.cloudbees.catalogAndPricing.dao.Product;
import com.cloudbees.catalogAndPricing.exceptions.ProductNotFoundException;
import com.cloudbees.catalogAndPricing.repository.ProductRepository;
import com.cloudbees.catalogAndPricing.services.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    public void getAllProducts_ReturnsListOfProducts() {
        List<Product> products = Arrays.asList(new Product(), new Product());
        Mockito.when(productRepository.findAll()).thenReturn(products);
        List<Product> result = productService.getAllProducts();
        assertEquals(products, result);
    }

    @Test
    public void getProductById_ValidId_ReturnsProduct() {
        Long productId = 1L;
        Product product = new Product();
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        Product result = productService.getProductById(productId);
        assertEquals(product, result);
    }

    @Test
    public void getProductById_InvalidId_ThrowsProductNotFoundException() {
        Long productId = 2L;
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(productId));
    }

    @Test
    public void createProduct_ValidProduct_ReturnsCreatedProduct() {
        Product product = new Product();
        Mockito.when(productRepository.save(product)).thenReturn(product);
        Product result = productService.createProduct(product);
        assertEquals(product, result);
    }

    @Test
    public void updateProduct_ExistingProduct_ReturnsTrue() {
        Long productId = 1L;
        Product updatedProduct = new Product();
        Mockito.when(productRepository.existsById(productId)).thenReturn(true);
        Mockito.when(productRepository.save(updatedProduct)).thenReturn(updatedProduct);
        boolean result = productService.updateProduct(productId, updatedProduct);
        assertTrue(result);
    }

    @Test
    public void updateProduct_NonExistingProduct_ReturnsFalse() {
        Long productId = 2L;
        Product updatedProduct = new Product();
        Mockito.when(productRepository.existsById(productId)).thenReturn(false);
        boolean result = productService.updateProduct(productId, updatedProduct);
        assertFalse(result);
    }

    @Test
    public void deleteProduct_ExistingProduct_ReturnsTrue() {
        Long productId = 1L;
        Mockito.when(productRepository.existsById(productId)).thenReturn(true);
        boolean result = productService.deleteProduct(productId);
        assertTrue(result);
    }

    @Test
    public void deleteProduct_NonExistingProduct_ReturnsFalse() {
        Long productId = 2L;
        Mockito.when(productRepository.existsById(productId)).thenReturn(false);
        boolean result = productService.deleteProduct(productId);
        assertFalse(result);
    }

    @Test
    public void applyDiscountOrTax_ValidProduct_ReturnsUpdatedProduct() {
        Long productId = 1L;
        double value = 10.0;
        double initialValue=100.0;
        PriceAdjustmentType type = PriceAdjustmentType.DISCOUNT_VALUE;
        Product product = new Product();
        PriceDetails priceDetails = new PriceDetails();
        priceDetails.setPrice(initialValue);
        product.setPriceDetails(priceDetails);

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        Mockito.when(productRepository.save(product)).thenReturn(product);

        Product result = productService.applyDiscountOrTax(productId, type, value);
        double expectedFinalPrice = initialValue * (1 - value / 100);
        assertNotNull(result);
        assertEquals(expectedFinalPrice, result.getPriceDetails().getPrice(), 0.001);
    }
}
