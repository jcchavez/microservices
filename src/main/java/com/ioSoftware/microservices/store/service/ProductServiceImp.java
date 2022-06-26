package com.ioSoftware.microservices.store.service;

import com.ioSoftware.microservices.store.entity.Category;
import com.ioSoftware.microservices.store.entity.Product;
import com.ioSoftware.microservices.store.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImp implements  ProductService{

//    @Autowired
//    private ProductRepository productRepository;

    //instead to @Autowire ProductRepository, we pass it on constructor
    //this is useful when we Mock a repository with Mockito, this needs @RequiredArgsConstructor
    //and this is called Dependency Injection by Constructor
    private final ProductRepository productRepository;

    @Override
    public List<Product> listAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProduct(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public Product createProduct(Product product) {
        product.setStatus("Created");
        product.setCreateAt(new Date());
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Product product) {
        Product found = getProduct(product.getId());
        if(null == found){
            return null;
        }
        found.setName(product.getName());
        found.setStatus(product.getStatus());
        found.setCreateAt(product.getCreateAt());
        found.setDescription(product.getDescription());
        found.setCategory(product.getCategory());
        found.setStock(product.getStock());
        found.setPrice(product.getPrice());
        return productRepository.save(found);//always save object obtained from DB
    }

    @Override
    public Product deleteProduct(Long id) {
        Product product = getProduct(id);
        if(null == product)  return null;
        product.setStatus("Deleted");
        return productRepository.save(product);
    }

    @Override
    public List<Product> findByCategory(Category category) {
        return productRepository.findByCategory(category);
    }

    @Override
    public Product updateStock(Long id, Double quantity) {
        Product product = getProduct(id);
        if(null == product) return null;
        product.setStock(product.getStock() + quantity);

        return productRepository.save(product);
    }
}
