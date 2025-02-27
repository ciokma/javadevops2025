package com.skillnest.customer.repository;

import com.skillnest.customer.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByName(String name); // Mejor usar Optional
    List<Product> findByDescriptionContaining(String word);

}
