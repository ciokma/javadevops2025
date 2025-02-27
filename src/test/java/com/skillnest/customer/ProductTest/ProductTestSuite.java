package com.skillnest.customer.service;

import com.skillnest.customer.model.Product;
import com.skillnest.customer.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Permite que Mockito maneje las inyecciones de dependencias para la prueba.
class ProductServiceTest {

    @Mock // Mockeamos el repositorio para simular su comportamiento sin necesidad de una base de datos real.
    private ProductRepository productRepository;

    @InjectMocks // Inyectamos el mock del repositorio dentro de ProductService para probarlo de manera aislada.
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        // Inicializamos un producto de prueba antes de cada prueba.
        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
    }

    @Test
    void getProducts_ShouldReturnListOfProducts() {
        // Simulamos que el repositorio devuelve una lista con un producto.
        when(productRepository.findAll()).thenReturn(List.of(product));
        List<Product> products = productService.getProducts();

        // Verificamos que la lista no esté vacía y tenga el producto esperado.
        assertFalse(products.isEmpty());
        assertEquals(1, products.size());
    }

    @Test
    void saveProduct_ShouldReturnSavedProduct() {
        // Simulamos el guardado de un producto en el repositorio.
        when(productRepository.save(any(Product.class))).thenReturn(product);
        Product savedProduct = productService.saveProduct(product);

        // Verificamos que el producto guardado no sea nulo y tenga el ID esperado.
        assertNotNull(savedProduct);
        assertEquals(product.getId(), savedProduct.getId());
    }

    @Test
    void getProduct_ShouldReturnProductWhenExists() {
        // Simulamos la búsqueda de un producto que existe en el repositorio.
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        Optional<Product> foundProduct = productService.getProduct(1L);

        // Verificamos que el producto se encuentre correctamente.
        assertTrue(foundProduct.isPresent());
        assertEquals(product.getId(), foundProduct.get().getId());
    }

    @Test
    void getProduct_ShouldReturnEmptyWhenNotExists() {
        // Simulamos la búsqueda de un producto que no existe en el repositorio.
        when(productRepository.findById(2L)).thenReturn(Optional.empty());
        Optional<Product> foundProduct = productService.getProduct(2L);

        // Verificamos que el resultado sea un Optional vacío.
        assertFalse(foundProduct.isPresent());
    }

    @Test
    void updateProduct_ShouldReturnUpdatedProductWhenExists() {
        // Simulamos la actualización de un producto existente en el repositorio.
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        Product updatedProduct = productService.updateProduct(1L, product);

        // Verificamos que el producto actualizado tenga el mismo ID que el original.
        assertNotNull(updatedProduct);
        assertEquals(product.getId(), updatedProduct.getId());
    }

    @Test
    void updateProduct_ShouldReturnNullWhenNotExists() {
        // Simulamos el intento de actualizar un producto que no existe.
        when(productRepository.findById(2L)).thenReturn(Optional.empty());
        Product updatedProduct = productService.updateProduct(2L, product);

        // Verificamos que el resultado sea null.
        assertNull(updatedProduct);
    }

    @Test
    void deleteProduct_ShouldReturnTrueWhenExists() {
        // Simulamos que el producto existe antes de eliminarlo.
        when(productRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productRepository).deleteById(1L);

        // Verificamos que la eliminación devuelva true.
        assertTrue(productService.deleteProduct(1L));
    }

    @Test
    void deleteProduct_ShouldReturnFalseWhenNotExists() {
        // Simulamos que el producto no existe en el repositorio.
        when(productRepository.existsById(2L)).thenReturn(false);

        // Verificamos que el intento de eliminar devuelva false.
        assertFalse(productService.deleteProduct(2L));
    }


    @Test
    void testGetProduct_ShouldFail_WhenProductNotFound() {
        // Llamamos al metodo directamente sin un repositorio funcional
        Optional<Product> result = productService.getProduct(1L);

        // Esto fallará porque el producto no existe
        assertTrue(result.isPresent(), "Esperábamos que el producto existiera, pero no fue encontrado.");
    }



}
