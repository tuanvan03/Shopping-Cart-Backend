package com.example.shopping.services.product;

import com.example.shopping.exceptions.ProductNotFoundException;
import com.example.shopping.models.Category;
import com.example.shopping.models.Product;
import com.example.shopping.models.request.AddProduct;
import com.example.shopping.models.request.UpdateProduct;
import com.example.shopping.repositories.CategoryRepository;
import com.example.shopping.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements IProductService{
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Product addProduct(AddProduct product) {
        // it has 2 cases: if category existed in db, add product, if not creating a new category and add product
        Category category = Optional.ofNullable(categoryRepository.findByName(product.getCategory().getName()))
                .orElseGet(() -> {
                    Category newCategory = new Category(product.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });

        product.setCategory(category);
        return productRepository.save(createProduct(product, category));
    }

    private Product createProduct (AddProduct product, Category category) {
        return new Product (
                product.getName(),
                product.getBrand(),
                product.getDescription(),
                product.getPrice(),
                product.getInventory(),
                category
        );
    }
    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.findById(id)
                .ifPresentOrElse(productRepository::delete,
                        () -> {throw new ProductNotFoundException("Product not found");
                });
    }

    @Override
    public Product updateProduct(Long productId, UpdateProduct product) {
        // get product by id
        Product p = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException("Product not found"));

        p.setName(product.getName());
        p.setBrand(product.getBrand());
        p.setPrice(product.getPrice());
        p.setDescription(product.getDescription());
        p.setInventory(product.getInventory());

        Category category = categoryRepository.findByName(product.getCategory().getName());
        p.setCategory(category);

        // save
        productRepository.save(p);

        return p;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }
}
