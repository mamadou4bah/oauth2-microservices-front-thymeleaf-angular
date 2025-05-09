package com.inventory.service.app.web;

import com.inventory.service.app.entities.Product;
import com.inventory.service.app.repository.ProductRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductRestController {

    private ProductRepository productRepository;

    public ProductRestController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/products")
    @PreAuthorize("hasAuthority('ADMIN')") // On vérifie que l'utilisateur a le droit de lire les clients
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}
