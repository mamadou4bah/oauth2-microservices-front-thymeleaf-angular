package com.customer.front.thymeleaf.app.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class AuthenticationSecurityController {

    private final ClientRegistrationRepository clientRegistrationRepository;

    public AuthenticationSecurityController(ClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
    }


    @GetMapping("/auth")
    @ResponseBody
    // L'objet Authentication est injecté par Spring Security, il contient les informations sur l'utilisateur authentifié
    public Authentication authentication(Authentication authentication) {
        return authentication;
    }

    @GetMapping("/oauth2Login")
    public String oauth2Login(Model model) {
        String authorizationRequestBaseUri = "oauth2/authorization";
        Map<String, String> oauth2AuthenticationUrls = new HashMap();
        Iterable<ClientRegistration> clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
                clientRegistrations.forEach(clientRegistration -> {
                    oauth2AuthenticationUrls.put(clientRegistration.getClientName(), authorizationRequestBaseUri + "/" + clientRegistration.getRegistrationId());

        });
        model.addAttribute("urls", oauth2AuthenticationUrls);
        return "oauth2Login";
    }

}
