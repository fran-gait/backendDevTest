package com.example.demo.core.domain;

import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ProductRepository {

    Set<String> findSimilarProductIds(String productId);

    Product findProductById(String productId);
}
