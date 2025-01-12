package com.example.shopping.controllers;

import com.example.shopping.exceptions.ProductNotFoundException;
import com.example.shopping.exceptions.ResourceNotFoundException;
import com.example.shopping.models.Product;
import com.example.shopping.models.request.AddProduct;
import com.example.shopping.models.request.UpdateProduct;
import com.example.shopping.models.response.ApiResponse;
import com.example.shopping.services.product.IProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    private final IProductService iProductService;

    public ProductController(IProductService iProductService) {
        this.iProductService = iProductService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllProducts() {
        try {
            List<Product> products = iProductService.getAllProducts();
            return ResponseEntity.ok(new ApiResponse("Get all products successfully", products));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId) {
        try {
            Product p = iProductService.getProductById(productId);
            return ResponseEntity.ok(new ApiResponse("Get product successfully", p));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{categoryName}")
    public ResponseEntity<ApiResponse> getProductByCategory(@PathVariable String categoryName) {
        try {
            List<Product> ps = iProductService.getProductsByCategory(categoryName);
            return ResponseEntity.ok(new ApiResponse("Get products by category successfully", ps));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/add-product")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProduct product) {
        try {
            Product p = iProductService.addProduct(product);
            return ResponseEntity.ok(new ApiResponse("Create product successfully", p));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable Long productId, @RequestBody UpdateProduct updateProduct) {
        try {
            Product p = iProductService.updateProduct(productId, updateProduct);
            return ResponseEntity.ok(new ApiResponse("Update product successfully", p));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId) {
        try {
            iProductService.deleteProduct(productId);
            return ResponseEntity.ok(new ApiResponse("Delete product successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
