package com.example.shopping.controllers;

import com.example.shopping.exceptions.AlreadyExistCategoryException;
import com.example.shopping.exceptions.ResourceNotFoundException;
import com.example.shopping.models.Category;
import com.example.shopping.models.response.ApiResponse;
import com.example.shopping.services.category.ICategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    private final ICategoryService iCategoryServicec;

    public CategoryController(ICategoryService iCategoryServicec) {
        this.iCategoryServicec = iCategoryServicec;
    }

    @GetMapping()
    public ResponseEntity<ApiResponse> getAllCategories() {
        try {
            List<Category> categoryList = iCategoryServicec.getAllCategories();
            return ResponseEntity.ok(new ApiResponse("Get all categories successfully", categoryList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/add-category")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody Category category) {
        try {
            Category c = iCategoryServicec.addCategory(category);
            return ResponseEntity.ok(new ApiResponse("Add category successfully", c));
        } catch (AlreadyExistCategoryException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/update/{categoryId}")
    public ResponseEntity<ApiResponse> updateCategory(@RequestBody Category category, @PathVariable Long categoryId) {
        try {
            Category c = iCategoryServicec.updateCategory(category, categoryId);
            return ResponseEntity.ok(new ApiResponse("Update category successfully", c));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long categoryId) {
        try {
            Category c = iCategoryServicec.getCategoryById(categoryId);
            return ResponseEntity.ok(new ApiResponse("Get category successfully" + c.getId(), c));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponse> deleteCategoryById(@PathVariable Long categoryId) {
        try {
            iCategoryServicec.deleteCategoryById(categoryId);
            return ResponseEntity.ok(new ApiResponse("Delete category successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
