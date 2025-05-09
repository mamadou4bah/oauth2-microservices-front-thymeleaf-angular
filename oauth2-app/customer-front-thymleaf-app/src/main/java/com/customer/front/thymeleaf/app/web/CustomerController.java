package com.customer.front.thymeleaf.app.web;

import com.customer.front.thymeleaf.app.repository.CustomerRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller  //pas de @RestController parce qu'on génère des pages HTML côté serveur
public class CustomerController {

    private CustomerRepository customerRepository;


    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping("/customers")
    @PreAuthorize("hasAuthority('ADMIN')") // On vérifie que l'utilisateur a le droit de lire les clients
    public String getAllCustomers(Model model) { // Model est une interface de Spring MVC qui permet de passer des données à la vue
        model.addAttribute("customers", customerRepository.findAll()); // On ajoute la liste des clients (customers) au modèle
        return "customers"; // This will return the name of the HTML template to be rendered
    }

    @GetMapping("/")
    public String index() { // Model est une interface de Spring MVC qui permet de passer des données à la vue
        return "index"; // This will return the name of the HTML template to be rendered

    }

    @GetMapping("/notAuthorized")
    public String notAuthorizedPage() { // Model est une interface de Spring MVC qui permet de passer des données à la vue
        return "notAuthorized"; // This will return the name of the HTML template to be rendered

    }
}
