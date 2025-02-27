package com.skillnest.customer.service;

import com.skillnest.customer.model.Product;
import com.skillnest.customer.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    //crear y gestionar la instancia de la clase que necesitas sin que tengas que hacerlo manualmente con new.
    @Autowired
    private final ProductRepository productRepository;

    public List<Product> getProducts(){
        return productRepository.findAll();
    }

    public Product saveProduct(Product product){
        return productRepository.save(product);
    }
    public Optional<Product> getProduct(Long id) { // Devolver Optional en lugar de null
        return productRepository.findById(id);
    }
    public Product updateProduct(Long id, Product product){
        Optional<Product> productExists = productRepository.findById(id);
        if(productExists.isPresent()){
            return productRepository.save(product);
        }
        return null;
    }
    /*
    public boolean deleteProduct(Long id){
        Optional<Product> productExists = productRepository.findById(id);
        if(productExists.isPresent()){
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }
    */


    public boolean deleteProduct(Long id) {
        if (productRepository.existsById(id)) { // Mejor manera de verificar existencia
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
