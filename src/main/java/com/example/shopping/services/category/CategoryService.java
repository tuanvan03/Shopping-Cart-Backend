package com.example.shopping.services.category;

import com.example.shopping.exceptions.AlreadyExistCategoryException;
import com.example.shopping.exceptions.ResourceNotFoundException;
import com.example.shopping.models.Category;
import com.example.shopping.repositories.CategoryRepository;

import java.util.List;

public class CategoryService implements ICategoryService{
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category addCategory(Category category) {
        // check if category's name exists
        if (categoryRepository.existsByName(category.getName())) {
            throw new AlreadyExistCategoryException(category.getName() + "existed");
        }

        Category c = new Category(category.getName());
        categoryRepository.save(c);

        return c;
    }

    @Override
    public Category updateCategory(Category category, Long id) {
        // if category does not exist
        Category c = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        c.setName(category.getName());
        categoryRepository.save(c);

        return c;
    }

    @Override
    public void deleteCategoryById(Long id) {
        categoryRepository.findById(id)
                .ifPresentOrElse(categoryRepository::delete,
                    () -> {
                        throw new ResourceNotFoundException("Category not found");
                });
    }
}

