package com.ioSoftware.microservices.store.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Date;

@Entity
@Table(name = "tbl_product")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(message = "Product name cannot be empty")
    private String name;
    private String description;
    @Positive(message = "Stock must be greater than 0")
    private Double stock;
    private Double price;
    private String status;

    @Column(name = "create_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    @NotNull(message = "Must specify a category")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    //to avoid message java.util.ArrayList[0]->com.ioSoftware.microservices.store.entity.Product["category"] hibernateLazyInitializer
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    private Category category;
}
