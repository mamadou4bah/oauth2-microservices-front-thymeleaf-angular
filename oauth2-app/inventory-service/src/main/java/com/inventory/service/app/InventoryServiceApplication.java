package com.inventory.service.app;

import com.inventory.service.app.entities.Product;
import com.inventory.service.app.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.UUID;

@SpringBootApplication
public class InventoryServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }

    // Insert some data in the database
    @Bean
    CommandLineRunner commandLineRunner(ProductRepository productRepository) {
        return args -> {
            System.out.println("CommandLineRunner is running");
            productRepository.save(Product.builder()
                    .id(UUID.randomUUID().toString())
                    .name("Iphone 14")
                    .price(1200.0)
                    .quantity(20)
                    .build());

            productRepository.save(Product.builder()
                    .id(UUID.randomUUID().toString())
                    .name("Samsung S23")
                    .price(1000.0)
                    .quantity(0)
                    .build());

            productRepository.save(Product.builder()
                    .id(UUID.randomUUID().toString())
                    .name("Iphone 15")
                    .price(1500.0)
                    .quantity(5)
                    .build());
            };
        }
}
