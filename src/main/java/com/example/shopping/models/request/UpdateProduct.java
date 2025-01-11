package com.example.shopping.models.request;

import com.example.shopping.models.Category;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateProduct {
    private String name;
    private String brand;
    private String description;
    private BigDecimal price;
    private int inventory;
    private Category category;
}
