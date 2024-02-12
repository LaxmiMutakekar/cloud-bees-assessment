package com.cloudbees.catalogAndPricing.service;

import com.cloudbees.catalogAndPricing.dao.PriceAdjustmentType;
import com.cloudbees.catalogAndPricing.dao.PriceDetails;
import com.cloudbees.catalogAndPricing.dao.Product;
import com.cloudbees.catalogAndPricing.dto.ProductDaoMapper;
import com.cloudbees.catalogAndPricing.dto.ProductDto;
import com.cloudbees.catalogAndPricing.dto.ProductDtoMapper;
import com.cloudbees.catalogAndPricing.exceptions.ProductNotFoundException;
import com.cloudbees.catalogAndPricing.repository.ProductPriceRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductPriceRepository productPriceRepository;

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductDtoMapper productDtoMapper;

    @Mock
    private ProductDaoMapper productDaoMapper;

    @Test
    public void getAllProducts_ReturnsListOfProducts() {
        List<Product> products = Arrays.asList(new Product());
        ProductDto productDto = new ProductDto();
        List<ProductDto> expectedRes = Arrays.asList(productDto);
        when(productRepository.findAll()).thenReturn(products);
        when(productDtoMapper.apply(any())).thenReturn(productDto);
        List<ProductDto> result = productService.getAllProducts();
        assertEquals(expectedRes, result);
    }

    @Test
    public void getProductById_ValidId_ReturnsProduct() {
        Long productId = 1L;
        ProductDto product = new ProductDto();
        when(productRepository.findById(productId)).thenReturn(Optional.of(new Product()));
        when(productDtoMapper.apply(any())).thenReturn(product);
        ProductDto result = productService.getProductById(productId);
        assertEquals(product, result);
    }

    @Test
    public void getProductById_InvalidId_ThrowsProductNotFoundException() {
        Long productId = 2L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(productId));
    }

    @Test
    public void createProduct_ValidProduct_ReturnsCreatedProduct() {
        Product product = new Product();
        ProductDto productDto = new ProductDto();
        when(productRepository.save(product)).thenReturn(product);
        when(productDtoMapper.apply(any())).thenReturn(productDto);
        when(productDaoMapper.apply(any())).thenReturn(product);
        ProductDto result = productService.createProduct(productDto);
        assertEquals(productDto, result);
    }

    @Test
    public void updateProduct_ExistingProduct_ReturnsTrue() {
        Long productId = 1L;
        Product updatedProduct = new Product();
        updatedProduct.setPid(productId);
        when(productRepository.existsById(productId)).thenReturn(true);
        when(productRepository.save(updatedProduct)).thenReturn(updatedProduct);
        boolean result = productService.updateProduct( updatedProduct);
        assertTrue(result);
    }

    @Test
    public void updateProduct_NonExistingProduct_ReturnsFalse() {
        Long productId = 2L;
        Product updatedProduct = new Product();
        updatedProduct.setPid(productId);
        when(productRepository.existsById(productId)).thenReturn(false);
        boolean result = productService.updateProduct( updatedProduct);
        assertFalse(result);
    }

    @Test
    public void deleteProduct_ExistingProduct_ReturnsTrue() {
        Long productId = 1L;
        when(productRepository.existsById(productId)).thenReturn(true);
        boolean result = productService.deleteProduct(productId);
        assertTrue(result);
    }

    @Test
    public void deleteProduct_NonExistingProduct_ReturnsFalse() {
        Long productId = 2L;
        when(productRepository.existsById(productId)).thenReturn(false);
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
        when(productPriceRepository.getReferenceById(productId)).thenReturn(priceDetails);
        when(productPriceRepository.save(priceDetails)).thenReturn(priceDetails);
        boolean result = productService.applyDiscountOrTax(productId, type, value);
        assertNotNull(result);
        assertEquals(result,true);
    }
}
