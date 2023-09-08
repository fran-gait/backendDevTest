package com.example.demo.core.domain;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Product {

    public static final String TO_STRING_FORMAT = "{id='%s', name='%s', price=%2f, availability=%s}";
    private String id;
    private String name;
    private double price;
    private boolean availability;

    @Override
    public String toString() {
        return String.format(TO_STRING_FORMAT, id, name, price, availability);
    }
}
