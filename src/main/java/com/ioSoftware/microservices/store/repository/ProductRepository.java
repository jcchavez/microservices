package com.ioSoftware.microservices.store.repository;

import com.ioSoftware.microservices.store.entity.Category;
import com.ioSoftware.microservices.store.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    public List<Product> findByCategory(Category category);
}
