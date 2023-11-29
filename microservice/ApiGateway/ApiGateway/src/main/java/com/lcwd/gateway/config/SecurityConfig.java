package com.lcwd.gateway.config;


import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;


@EnableAutoConfiguration //Configuration
@EnableWebFluxSecurity
public class SecurityConfig {



    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange(exchanges ->
                        exchanges
                                .anyExchange().authenticated()
                )
                .httpBasic(withDefaults())
                .oauth2Login(withDefaults())  // Enable OAuth 2.0 login
                .oauth2ResourceServer(oauth2ResourceServer ->
                        oauth2ResourceServer
                                .jwt(withDefaults())
                );

        return http.build();
    }
}


//    @Bean
//    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
//
//        httpSecurity
//                .authorizeExchange()
//                .anyExchange()
//                .authenticated()
//                .and()
//                .oauth2Client()
//                .and()
//                .oauth2ResourceServer()
//                .jwt();
//
//
//        return httpSecurity.build();
//    }