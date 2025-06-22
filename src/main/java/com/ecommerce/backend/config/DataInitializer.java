package com.ecommerce.backend.config;

import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final ProductRepository productRepository;

    @PostConstruct
    public void loadData() {
        insertIfNotExists("iPhone 15", "Latest Apple smartphone", 1199.00, "https://via.placeholder.com/300x200?text=iPhone15");
        insertIfNotExists("Gaming Laptop", "Powerful gaming beast", 1599.99, "https://via.placeholder.com/300x200?text=Gaming+Laptop");
        insertIfNotExists("Smartwatch", "Feature-rich smart wearable", 299.49, "https://via.placeholder.com/300x200?text=Smartwatch");
    }

    private void insertIfNotExists(String name, String description, double price, String imageUrl) {
        if (productRepository.findByName(name).isEmpty()) {
            productRepository.save(new Product(null, name, description, price, imageUrl));
        }
    }
}
