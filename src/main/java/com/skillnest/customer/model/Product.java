package com.skillnest.customer.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

@Entity
@Table(name = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor // Se necesita para JPA
public class Product {

    @Id // PK
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double price;

    @Column(columnDefinition = "TEXT")
    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE) // Indica que es una fecha sin hora
    @Column(updatable = false)
    private Date createdAt;

    @PrePersist // Antes de generar el registro se ejecuta
    protected  void generatedDate(){
        this.createdAt = new Date();
    }

    //@PreUpdate

}
