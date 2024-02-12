package com.cloudbees.catalogAndPricing.dao;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(
        name = "product",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "product_name_unique",
                        columnNames = "name"
                ),
                @UniqueConstraint(
                        name = "product_description_unique",
                        columnNames = "description"
                )
        }
)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pid;
    private String name;
    private String description;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "price_details_id", referencedColumnName = "id")
    private PriceDetails priceDetails;
    private int quantityAvailable;

}
