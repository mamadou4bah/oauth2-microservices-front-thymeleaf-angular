package com.customer.front.thymeleaf.app;

import com.customer.front.thymeleaf.app.entities.Customer;
import com.customer.front.thymeleaf.app.repository.CustomerRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class CustomerFrontThymeleafApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerFrontThymeleafApplication.class, args);
    }

    // Insert some data in the database
    @Bean
    CommandLineRunner  commandLineRunner(CustomerRepository customerRepository) {
        return args -> {
            System.out.println("CommandLineRunner is running");
            customerRepository.save(Customer.builder()
                    .name("Mamadou")
                    .email("mamadou.bah21000@gmaail.com")
                    .build());

            customerRepository.save(Customer.builder()
                    .name("Wansan")
                    .email("bahmamadouwansan@gmail.com")
                    .build());

            customerRepository.save(Customer.builder()
                    .name("BAH")
                    .email("mamadou.bah@gmaail.com")
                    .build());

        };
    }
}
