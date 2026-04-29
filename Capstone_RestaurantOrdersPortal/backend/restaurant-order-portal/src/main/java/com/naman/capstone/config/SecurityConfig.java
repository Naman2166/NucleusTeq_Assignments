package com.naman.capstone.config;

import com.naman.capstone.security.JwtAuthenticationEntryPoint;
import com.naman.capstone.security.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.naman.capstone.constant.AppConstants.*;

/**
 * this class configures application security, authentication and authorization rules.
 */
@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    //constructor injection
    public SecurityConfig(JwtFilter jwtFilter, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.jwtFilter = jwtFilter;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    //configures security rules
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> {})
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        //API anyone can access
                        .requestMatchers(USER_BASE_URL + REGISTER).permitAll()
                        .requestMatchers(USER_BASE_URL + LOGIN).permitAll()
                        .requestMatchers(HttpMethod.GET, RESTAURANT_BASE_URL + "/**").permitAll()
                        .requestMatchers(HttpMethod.GET, CATEGORY_BASE_URL + "/**").permitAll()
                        .requestMatchers(HttpMethod.GET, MENU_ITEM_BASE_URL + "/**").permitAll()

                        //API only restaurant owner can access
                        .requestMatchers(HttpMethod.POST, RESTAURANT_BASE_URL + "/**").hasRole(ROLE_OWNER)
                        .requestMatchers(HttpMethod.PUT, RESTAURANT_BASE_URL + "/**").hasRole(ROLE_OWNER)
                        .requestMatchers(HttpMethod.DELETE, RESTAURANT_BASE_URL + "/**").hasRole(ROLE_OWNER)

                        .requestMatchers(HttpMethod.POST, CATEGORY_BASE_URL + "/**").hasRole(ROLE_OWNER)
                        .requestMatchers(HttpMethod.PUT, CATEGORY_BASE_URL + "/**").hasRole(ROLE_OWNER)
                        .requestMatchers(HttpMethod.DELETE, CATEGORY_BASE_URL + "/**").hasRole(ROLE_OWNER)

                        .requestMatchers(HttpMethod.POST, MENU_ITEM_BASE_URL + "/**").hasRole(ROLE_OWNER)
                        .requestMatchers(HttpMethod.PUT, MENU_ITEM_BASE_URL + "/**").hasRole(ROLE_OWNER)
                        .requestMatchers(HttpMethod.DELETE, MENU_ITEM_BASE_URL + "/**").hasRole(ROLE_OWNER)

                        .requestMatchers(HttpMethod.GET, ORDER_BASE_URL + "/restaurant/**").hasRole(ROLE_OWNER)
                        .requestMatchers(HttpMethod.PUT, ORDER_BASE_URL + "/*/status").hasRole(ROLE_OWNER)
                        .requestMatchers(HttpMethod.PUT, ORDER_BASE_URL + "/*/cancel-by-owner").hasRole(ROLE_OWNER)

                        //API only user can access
                        .requestMatchers(CART_BASE_URL + "/**").hasRole(ROLE_USER)
                        .requestMatchers(HttpMethod.POST, ORDER_BASE_URL).hasRole(ROLE_USER)
                        .requestMatchers(HttpMethod.GET, ORDER_BASE_URL).hasRole(ROLE_USER)
                        .requestMatchers(HttpMethod.PUT, ORDER_BASE_URL + "/*/cancel").hasRole(ROLE_USER)

                        //all other API need authentication
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}