package com.ioSoftware.microservices.store.service;

import com.ioSoftware.microservices.store.entity.Category;
import com.ioSoftware.microservices.store.entity.Product;
import com.ioSoftware.microservices.store.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductServiceImpTest {

    @Mock //to specify that we don't affect db, instead use a Mock
    private ProductRepository productRepository;

    //Instead of @Autowire we instantiate the Service with a ProductRepository Mock by dependency injection by constructor
    private ProductService productService;

    @BeforeEach
    public void setUp(){ // on setUp create a product and return it when find by id 1L
        MockitoAnnotations.openMocks(this);
        productService = new ProductServiceImp(productRepository); //just in this case we take control instead to "Inversion of Control"
        Product productNike1 = Product.builder()
                .id(1L)
                .name("Nike")
                .price(Double.parseDouble("1200.50"))
                .status("Created")
                .stock(Double.parseDouble("20"))
                .category(Category.builder().id(1L).build())
                .build();
        Product productNike2 = Product.builder()
                .id(2L)
                .name("Nike 2")
                .price(Double.parseDouble("1670.50"))
                .status("Created")
                .stock(Double.parseDouble("70"))
                .category(Category.builder().id(1L).build())
                .build();

        Mockito.when(productRepository.findById(1L))
                .thenReturn(Optional.of(productNike1));
        //when update product, then return the updated object, since this is a Mock
        Mockito.when(productRepository.save(productNike1))
                .thenReturn(productNike1);
        Mockito.when(productRepository.findByCategory(Category.builder().id(1L).build()))
                .thenReturn(List.of(productNike1, productNike2));
    }

    @Test
    public void whenValidGetId_thenReturnProduct(){
        Product found = productService.getProduct(1L);
        Assertions.assertThat(found.getName()).isEqualTo("Nike");
    }

    @Test
    public void whenValidUpdateStock_thenReturnNewStock(){
        Product productNotExist = Product.builder()
                .id(2L).build();
        Product notExistent = productService.updateProduct(productNotExist);
        Assertions.assertThat(notExistent).isNull();
        Product addStock = productService.updateStock(1L, Double.parseDouble("8")); //we test add stock
        Assertions.assertThat(addStock.getStock()).isEqualTo(Double.parseDouble("28"));
    }

    @Test
    public void whenFindByCategory_thenReturnProducts(){
        List<Product> founds = productService.findByCategory(Category.builder().id(1L).build());
        Assertions.assertThat(founds.size()).isEqualTo(2);
        Assertions.assertThat(founds.get(0).getCategory().getId()).isEqualTo(1L);

    }

    @Test
    public void whenUpdatedProduct_thenReturnUpdatedValues(){
        Product productAboutToUpdate = Product.builder()
                .id(1L)
                .name("Nike updated")
                .price(Double.parseDouble("1800.40"))
                .status("Updated")
                .category(Category.builder().id(1L).build())
                .stock(Double.parseDouble("10")) // 10 to final stock
                .build();

        Product updated = productService.updateProduct(productAboutToUpdate);
        Assertions.assertThat(updated).isNotNull();
        Assertions.assertThat(updated.getName()).isEqualTo("Nike updated");
        Assertions.assertThat(updated.getPrice()).isEqualTo(Double.parseDouble("1800.40"));
        Assertions.assertThat(updated.getStatus()).isEqualTo("Updated");
        Assertions.assertThat(updated.getCategory().getId()).isEqualTo(1L);
        Assertions.assertThat(updated.getStock()).isEqualTo(Double.parseDouble("10"));

        Product productNotExist = Product.builder()
                .id(2L).build();

        Product notExistent = productService.updateProduct(productNotExist);
        Assertions.assertThat(notExistent).isNull();

    }

    @Test
    public void whenDeletedProduct_thenReturnDeletedStatus(){
        Product productNotExist = Product.builder()
                .id(2L).build();

        Product notExistent = productService.updateProduct(productNotExist);
        Assertions.assertThat(notExistent).isNull();

        Product deletedProduct = productService.deleteProduct(1L);
        Assertions.assertThat(deletedProduct.getStatus()).isEqualTo("Deleted");
    }



}