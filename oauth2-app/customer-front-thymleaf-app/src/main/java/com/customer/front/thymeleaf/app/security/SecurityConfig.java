package com.customer.front.thymeleaf.app.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.web.SecurityFilterChain;

import java.util.*;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final ClientRegistrationRepository clientRegistrationRepository;

    public SecurityConfig(ClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @Bean
    public SecurityFilterChain SecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // On va devoir utiliser une authenticaqtion statefull car rendu html côté serveur
        return httpSecurity
                .csrf(Customizer.withDefaults())
                .authorizeHttpRequests((authorize -> authorize
                        .requestMatchers("/", "webjars/**","/h2-console/**", "/oauth2Login/**").permitAll()
                ))
                .authorizeHttpRequests((authorize -> authorize.anyRequest().authenticated()))
                // Pour accéder à h2-console, on ajoute:
                .headers(header -> header.frameOptions(frameOption -> frameOption.disable()))
                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
                .oauth2Login(al->al
                        .loginPage("/oauth2Login")
                        .defaultSuccessUrl("/")
                )
                .logout(logout -> logout
                .logoutSuccessHandler(oidcLogoutSuccessHandler())
                    .logoutSuccessUrl("/").permitAll()
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                    .deleteCookies("JSESSIONID")
                )
                .exceptionHandling(eh -> eh.accessDeniedPage("/notAuthorized"))
                .build();
    }

    private OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler() {
        OidcClientInitiatedLogoutSuccessHandler oidcLougoutSuccessHandler =
                new OidcClientInitiatedLogoutSuccessHandler(this.clientRegistrationRepository);
        oidcLougoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}?logoutSuccess=true");
        return oidcLougoutSuccessHandler;
    }

    @Bean
    public GrantedAuthoritiesMapper userAuthorithiesMapper() {
        return (authorities) -> {
            final Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
            authorities.forEach((authority) -> { // Parcourrir le jwt token (pour gmail et keycloak -> on a OIDC_USER: openIdConnect authority
                                                                            // Pour Github on a : OAUTH2_USER  )
                if(authority instanceof OidcUserAuthority oidcUserAuthority) {  // Gmail et Keycloak
                    mappedAuthorities.addAll(mapAuthorities(oidcUserAuthority.getIdToken().getClaims()));
                    System.out.println(oidcUserAuthority.getAttributes());
                } else if(authority instanceof OAuth2UserAuthority oAuth2UserAuthority) { // Github
                    mappedAuthorities.addAll(mapAuthorities(oAuth2UserAuthority.getAttributes()));

                }
            });
            return mappedAuthorities;
        };
    }

    private List<SimpleGrantedAuthority> mapAuthorities(final Map<String, Object> attributes) {
        final Map<String, Object> realmAccess = ((Map<String, Object>)attributes.getOrDefault("realm_acces", Collections.emptyMap()));
        final Collection<String> roles = ((Collection<String>)realmAccess.getOrDefault("roles", Collections.emptyList()));
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .toList();
    }

}
