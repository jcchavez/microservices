package com.ioSoftware.microservices.store.repository;

import com.ioSoftware.microservices.store.entity.Category;
import com.ioSoftware.microservices.store.entity.Product;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp(){
        //prepare category before test
        Category category = Category.builder()
                .id(1L)
                .name("shoes")
                .build();
        categoryRepository.save(category);
    }


    @Test
    public void whenFindByCategory_thenReturnProductList() {

        Product productNike1 = Product.builder()
                .name("Nike")
                .description("Nike shoes")
                .price(Double.parseDouble("1200.50"))
                .status("Created")
                .stock(Double.parseDouble("20"))
                .createAt(new Date())
                .category(Category.builder().id(1L).build())
                .build();
        Product productNike2 = Product.builder()
                .name("Nike")
                .description("Nike shoes")
                .price(Double.parseDouble("1200.50"))
                .status("Created")
                .stock(Double.parseDouble("20"))
                .createAt(new Date())
                .category(Category.builder().id(1L).build())
                .build();

        productRepository.save(productNike1);
        productRepository.save(productNike2);
        List<Product> founds = productRepository.findByCategory(productNike1.getCategory());
        Assertions.assertThat(founds.size()).isEqualTo(2);


    }
}