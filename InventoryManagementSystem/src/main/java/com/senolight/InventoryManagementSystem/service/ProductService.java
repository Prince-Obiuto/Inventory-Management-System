package com.senolight.InventoryManagementSystem.service;

import java.util.List;
import com.senolight.InventoryManagementSystem.model.Product;
import com.senolight.InventoryManagementSystem.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product addProduct(Product product) {
        //TODO: Check if SKU exists before adding
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product updateQuantity(Long id, int quantitySold) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        product.setQuantity(product.getQuantity() - quantitySold);
        return productRepository.save(product);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public Product getProductBySku(String sku) {
        return productRepository.findBySku(sku).orElseThrow(() -> new RuntimeException("Product not found with SKU: " + sku));
    }
}
