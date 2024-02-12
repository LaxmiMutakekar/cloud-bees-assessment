package com.cloudbees.catalogAndPricing.repository;

import com.cloudbees.catalogAndPricing.dao.PriceDetails;
import com.cloudbees.catalogAndPricing.dao.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface ProductPriceRepository extends JpaRepository<PriceDetails, Long> {
}
