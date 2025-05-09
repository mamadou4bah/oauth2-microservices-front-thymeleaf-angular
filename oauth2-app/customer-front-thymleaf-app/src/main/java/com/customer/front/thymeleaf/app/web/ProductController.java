package com.customer.front.thymeleaf.app.web;

import com.customer.front.thymeleaf.app.dto.ProductDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestClient;

import org.springframework.http.HttpHeaders;
import java.util.List;

@Controller  //pas de @RestController parce qu'on génère des pages HTML côté serveur
public class ProductController {

    @GetMapping("/products")
    public String getAllProducts(Model model) { // Model est une interface de Spring MVC qui permet de passer des données à la vue
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        DefaultOidcUser defaultOidcUser = (DefaultOidcUser) oAuth2AuthenticationToken.getPrincipal();
        String jwtTokenValue = defaultOidcUser.getIdToken().getTokenValue();
        RestClient restClient = RestClient.create("http://localhost:8098"); // On peut aussi utiliser openFeign (solution declarative)
                                                                            // Ici on utilise RestClient: solution programmative
        List<ProductDTO> products = restClient.get()
                        .uri("/products")
                        .headers(httpHeaders -> httpHeaders.set(HttpHeaders.AUTHORIZATION, "Bearer "+jwtTokenValue))
                                .retrieve()
                                        .body(new ParameterizedTypeReference<>() {});
        model.addAttribute("products", products);
        return "products"; // This will return the name of the HTML template to be rendered

    }
}
