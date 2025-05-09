package com.customer.front.thymeleaf.app.repository;

import com.customer.front.thymeleaf.app.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // Custom query methods can be defined here if needed
}

