package com.example.demo.http.controller;

import com.example.demo.core.domain.Product;
import com.example.demo.core.domain.ProductRepository;
import com.example.demo.exception.ProductNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SimilarProductsControllerTest {

    private static final String URL_FORMAT = "/product/%s/similar";
    @MockBean
    private ProductRepository productRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetSimilarProductsOK() throws Exception {
        String productId = "1";
        String urlTemplate = String.format(URL_FORMAT, productId);
        Set<String> similarProducts = Set.of("1", "2", "3");
        String jsonExpected = "{\"similarProducts\":[" +
                "{\"id\":\"2\",\"name\":\"product 1\",\"price\":100.2,\"availability\":true}," +
                "{\"id\":\"3\",\"name\":\"product 1\",\"price\":100.2,\"availability\":true}," +
                "{\"id\":\"1\",\"name\":\"product 1\",\"price\":100.2,\"availability\":true}" +
                "]}";

        when(productRepository.findSimilarProductIds("1")).thenReturn(similarProducts);
        when(productRepository.findProductById("1")).thenReturn(aProduct("1"));
        when(productRepository.findProductById("2")).thenReturn(aProduct("2"));
        when(productRepository.findProductById("3")).thenReturn(aProduct("3"));

        mockMvc.perform(MockMvcRequestBuilders.get(urlTemplate)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonExpected));
    }

    @Test
    public void testGetSimilarProductsNotFound() throws Exception {
        String productId = "999";
        String urlTemplate = String.format(URL_FORMAT, productId);
        when(productRepository.findSimilarProductIds(productId)).thenThrow(ProductNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get(urlTemplate)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    private static Product aProduct(String id) {
        return Product
                .builder()
                .id(id)
                .name("product 1")
                .availability(true)
                .price(100.20)
                .build();
    }
}
