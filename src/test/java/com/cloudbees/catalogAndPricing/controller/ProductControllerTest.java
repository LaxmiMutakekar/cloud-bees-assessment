package com.cloudbees.catalogAndPricing.controller;

import com.cloudbees.catalogAndPricing.controller.ProductController;
import com.cloudbees.catalogAndPricing.dao.PriceAdjustmentType;
import com.cloudbees.catalogAndPricing.dao.Product;
import com.cloudbees.catalogAndPricing.dto.ProductDto;
import com.cloudbees.catalogAndPricing.dto.ProductDtoMapper;
import com.cloudbees.catalogAndPricing.exceptions.ProductNotFoundException;
import com.cloudbees.catalogAndPricing.services.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @Test
    public void getAllProducts_ReturnsListOfProducts() {
        List<ProductDto> products = Arrays.asList(new ProductDto(), new ProductDto());
        Mockito.when(productService.getAllProducts()).thenReturn(products);
        List<ProductDto> result = productController.getAllProducts();
        assertEquals(products, result);
    }

    @Test
    public void getProductById_ValidId_ReturnsProduct() {
        Long productId = 1L;
        ProductDto product = new ProductDto();
        Mockito.when(productService.getProductById(productId)).thenReturn(product);
        ResponseEntity<ProductDto> response = productController.getProductById(productId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(product, response.getBody());
    }

    @Test
    public void getProductById_InvalidId_ReturnsNotFound() {
        Long productId = 2L;
        Mockito.when(productService.getProductById(productId)).thenThrow(new ProductNotFoundException(productId));
        Executable executable = () -> productController.getProductById(productId);
        assertThrows(ProductNotFoundException.class, executable);
    }

    @Test
    public void createProduct_ValidProduct_ReturnsCreated() {
        ProductDto productDto = new ProductDto();
        Mockito.when(productService.createProduct(productDto)).thenReturn(productDto);
        ResponseEntity<ProductDto> response = productController.createProduct(productDto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(productDto, response.getBody());
    }

    @Test
    public void updateProduct_ValidIdAndProduct_ReturnsOk() {
        Long productId = 1L;
        Product updatedProduct = new Product();
        updatedProduct.setPid(productId);
        Mockito.when(productService.updateProduct( updatedProduct)).thenReturn(true);
        ResponseEntity<String> response = productController.updateProduct( updatedProduct);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Product updated successfully", response.getBody());
    }

    @Test
    public void updateProduct_InvalidId_ReturnsNotFound() {
        Long productId = 2L;
        Product updatedProduct = new Product();
        updatedProduct.setPid(productId);
        Mockito.when(productService.updateProduct( updatedProduct)).thenReturn(false);
        ResponseEntity<String> response = productController.updateProduct( updatedProduct);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Product not found with id: " + productId, response.getBody());
    }

    @Test
    public void deleteProduct_ValidId_ReturnsOk() {
        Long productId = 1L;
        Mockito.when(productService.deleteProduct(productId)).thenReturn(true);
        ResponseEntity<String> response = productController.deleteProduct(productId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Product deleted successfully", response.getBody());
    }

    @Test
    public void deleteProduct_InvalidId_ReturnsNotFound() {
        Long productId = 2L;
        Mockito.when(productService.deleteProduct(productId)).thenReturn(false);
        ResponseEntity<String> response = productController.deleteProduct(productId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Product not found with id: " + productId, response.getBody());
    }

    @Test
    public void applyTax_ValidId_ReturnsOk() {
        Long productId = 1L;
        double priceUpdateValue = 5.0;
        PriceAdjustmentType priceUpdateType = PriceAdjustmentType.TAX_VALUE;
        Mockito.when(productService.applyDiscountOrTax(productId, priceUpdateType, priceUpdateValue)).thenReturn(true);
        ResponseEntity<String> response=productController.applyTax(productId,priceUpdateValue,priceUpdateType);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }}
