package com.example.shopping.services.product;

import com.example.shopping.models.Product;
import com.example.shopping.models.request.AddProduct;
import com.example.shopping.models.request.UpdateProduct;

import java.util.List;

public interface IProductService {
    Product addProduct(AddProduct product);
    Product getProductById(Long id);
    void deleteProduct(Long id);
    Product updateProduct(Long productId, UpdateProduct product);
    List<Product> getAllProducts();
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByBrand(String brand);
    List<Product> getProductsByCategoryAndBrand(String category, String brand);
    List<Product> getProductsByName(String name);
    List<Product> getProductsByBrandAndName(String brand, String name);
    Long countProductsByBrandAndName(String brand, String name);
}
