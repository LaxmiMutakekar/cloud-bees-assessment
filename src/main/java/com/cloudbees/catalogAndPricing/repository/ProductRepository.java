package com.cloudbees.catalogAndPricing.repository;

import com.cloudbees.catalogAndPricing.dao.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
