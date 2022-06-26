package com.ioSoftware.microservices.store.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ioSoftware.microservices.store.entity.Category;
import com.ioSoftware.microservices.store.entity.Product;
import com.ioSoftware.microservices.store.error.ErrorMessage;
import com.ioSoftware.microservices.store.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/products")
@Slf4j
public class ProductController {

    @Autowired
    private ProductService productService;

//    private final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    /**
     * If categoryId is not provided, then we list all products, otherwise we filter by Category
     * @param categoryId optional to get by Category, otherwise list all products
     * @return List all products, or filter by Category if provided
     */
    @GetMapping
    public ResponseEntity<List<Product>> listAllProduct(@RequestParam(name = "categoryId", required = false) Long categoryId  ){
        log.debug("On /listAllProduct ---> categoryId: "+ categoryId);
        List<Product> products = new ArrayList<>();
        if(null == categoryId){
            products = productService.listAllProducts();
        }else {
            products = productService.findByCategory(Category.builder().id(categoryId).build());
        }
        if(products.isEmpty()){
            return ResponseEntity.noContent().build();//204 http status
        }

        return ResponseEntity.ok(products);

    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable("id") Long id){
        Product product = productService.getProduct(id);
        if(null == product){
            return ResponseEntity.noContent().build();//204 http status
        }
        return ResponseEntity.ok(product);
    }

    //annotate @Valid to active validations, and add result of validations on result
    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product
            , BindingResult result) {
        if(result.hasErrors()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, formatMessageToJson(result));
        }
        Product product1 = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(product1);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") Long id, @RequestBody Product product){
        Product found = productService.getProduct(id);
        if(null == found){
            return ResponseEntity.notFound().build();// http status not found
        }
        product.setId(found.getId());//product don't have ID, we pass it from found
        return ResponseEntity.ok(productService.updateProduct(product));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable("id") Long id){
        Product found = productService.getProduct(id);
        if(null == found){
            return ResponseEntity.notFound().build();// http status not found
        }
        return ResponseEntity.ok(productService.deleteProduct(id));
    }

    @GetMapping("/{id}/stock")
    public ResponseEntity<Product> updateStock(@PathVariable("id") Long id, @RequestParam(value = "quantity", required = true) Double quantity){
        Product found = productService.getProduct(id);
        if(null == found){
            return ResponseEntity.notFound().build();// http status not found
        }
        return ResponseEntity.ok(productService.updateStock(id, quantity));
    }

    /**
     * BindingResult contains several error messages, with this function
     * we extract all error messages and return them formatted as a Json object
     * @param result BindingResult
     * @return String containing all error messages from BindingResult as a formatted Json Object
     */
    private String formatMessageToJson(BindingResult result){
        List<Map<String, String>> errors = result.getFieldErrors().stream()
                .map(err ->{
                    Map<String, String> error = new HashMap<>();
                    error.put(err.getField(), err.getDefaultMessage());
                    return error;
                }).collect(Collectors.toList());
        ErrorMessage errorMessage = ErrorMessage.builder()
                .code("01")
                .messages(errors)
                .build();
        //Format the messages to Json
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "";
        try{
            jsonString = mapper.writeValueAsString(errorMessage);
        }catch (JsonProcessingException jsonProcessingException){
            jsonProcessingException.printStackTrace();
        }
        return jsonString;
    }

}
