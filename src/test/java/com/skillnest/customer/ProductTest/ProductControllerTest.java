package com.skillnest.customer.ProductTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillnest.customer.controller.ProductController;
import com.skillnest.customer.model.Product;
import com.skillnest.customer.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

// Extiende JUnit con Spring para ejecutar las pruebas dentro del contexto de Spring
@ExtendWith(SpringExtension.class)
// Indica que esta prueba es para el controlador ProductController
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    // Inyección de MockMvc para simular peticiones HTTP al controlador
    @Autowired
    private MockMvc mockMvc;

    // Simulación del servicio para evitar dependencias con la base de datos
    @MockBean
    private ProductService productService;

    // Objeto para convertir Java a JSON y viceversa
    @Autowired
    private ObjectMapper objectMapper;

    private Product product;

    @BeforeEach
    void setUp() {
        // Configuración de un producto de prueba antes de cada test
        product = new Product();
        product.setId(1L);
        product.setName("Producto de prueba");
        product.setPrice(100.0);
    }

    @Test
    void testGetAllProducts() throws Exception {
        // Simula el comportamiento del servicio cuando se llama a getProducts()
        when(productService.getProducts()).thenReturn(Arrays.asList(product));

        // Simula una petición GET y verifica que la respuesta sea correcta
        mockMvc.perform(MockMvcRequestBuilders.get("/api/productos"))
                .andExpect(status().isOk()) // Espera un código 200 OK
                .andExpect(jsonPath("$[0].id").value(product.getId()))
                .andExpect(jsonPath("$[0].name").value(product.getName()))
                .andExpect(jsonPath("$[0].price").value(product.getPrice()));
    }

    @Test
    void testCreateNewProduct() throws Exception {
        // Simula el guardado de un nuevo producto
        when(productService.saveProduct(any(Product.class))).thenReturn(product);

        // Simula una petición POST y verifica que la respuesta sea correcta
        mockMvc.perform(MockMvcRequestBuilders.post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated()) // Espera un código 201 Created
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.price").value(product.getPrice()));
    }

    @Test
    void testGetOneProduct_Found() throws Exception {
        // Simula la obtención de un producto por ID cuando existe
        when(productService.getProduct(anyLong())).thenReturn(Optional.of(product));

        // Simula una petición GET para obtener un solo producto
        mockMvc.perform(MockMvcRequestBuilders.get("/api/productos/1"))
                .andExpect(status().isOk()) // Espera un código 200 OK
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.price").value(product.getPrice()));
    }

    @Test
    void testGetOneProduct_NotFound() throws Exception {
        // Simula la obtención de un producto por ID cuando no existe
        when(productService.getProduct(anyLong())).thenReturn(Optional.empty());

        // Simula una petición GET y espera un código 404 Not Found
        mockMvc.perform(MockMvcRequestBuilders.get("/api/productos/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteProduct_Success() throws Exception {
        // Simula la eliminación exitosa de un producto
        when(productService.deleteProduct(anyLong())).thenReturn(true);

        // Simula una petición DELETE y espera un código 204 No Content
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/productos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteProduct_NotFound() throws Exception {
        // Simula la eliminación de un producto que no existe
        when(productService.deleteProduct(anyLong())).thenReturn(false);

        // Simula una petición DELETE y espera un código 404 Not Found
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/productos/1"))
                .andExpect(status().isNotFound());
    }
}
