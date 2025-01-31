package com.alenedaj.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF since we're using JWTs and a stateless session
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        // Permit access to login, registration, and static assets
                        .requestMatchers("/login", "/register", "/css/**", "/js/**", "/images/**").permitAll()
                        // Permit access to all API endpoints for authentication (signup, login, etc.)
                        .requestMatchers("/api/auth/**").permitAll()
                        // Restrict admin API endpoints to ADMIN authority
                        .requestMatchers("/api/admin/**").hasAuthority("ADMIN")
                        // Any other request must be authenticated
                        .anyRequest().authenticated()
                )
                // Configure form login with a custom login page and default success URL
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/dashboard", true)
                        .permitAll()
                )
                // Configure logout and specify the URL to redirect after logout
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .permitAll()
                )
                // Set session management as stateless
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Add JWT filter before the default username/password authentication filter
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
