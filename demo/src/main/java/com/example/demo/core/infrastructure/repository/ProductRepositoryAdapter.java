package com.example.demo.core.infrastructure.repository;

import com.example.demo.core.domain.Product;
import com.example.demo.core.domain.ProductRepository;
import com.example.demo.core.infrastructure.client.ProductClient;
import com.example.demo.exception.InternalErrorException;
import com.example.demo.exception.ProductNotFoundException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Repository("productRepository")
public class ProductRepositoryAdapter implements ProductRepository {

    private final ProductClient productClient;

    public ProductRepositoryAdapter(ProductClient productClient) {
        this.productClient = productClient;
    }

    @Override
    public Set<String> findSimilarProductIds(String productId) {
        Set<String> similarProductIDs = new HashSet<>();
        try {
            similarProductIDs = productClient.findSimilarProductIds(productId);
        } catch (FeignException feignClientException) {
            handleFeignException(feignClientException);
        }

        log.info("Similar Products:{} by id:{}", similarProductIDs, productId);
        return similarProductIDs;
    }

    @Override
    public Product findProductById(String productId) {
        Product product = Product.builder().build();
        try {
            product = productClient.findProductById(productId);
        } catch (FeignException feignException) {
            handleFeignException(feignException);
        }

        log.info("Product:{}", product);
        return product;
    }

    private static void handleFeignException(FeignException feignException) {
        log.error("Error when:{}", feignException.getMessage());
        if (HttpStatus.NOT_FOUND.value() == feignException.status()) {
            throw new ProductNotFoundException();
        }
        throw new InternalErrorException(feignException.status(), feignException.getMessage());
    }
}
