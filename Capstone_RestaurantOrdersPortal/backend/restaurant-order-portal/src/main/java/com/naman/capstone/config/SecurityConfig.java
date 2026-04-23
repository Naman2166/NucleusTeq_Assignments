package com.naman.capstone.config;

import com.naman.capstone.constant.AppConstants;
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
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        //API anyone can access
                        .requestMatchers(
                                AppConstants.USER_BASE_URL + AppConstants.REGISTER,
                                AppConstants.USER_BASE_URL + AppConstants.LOGIN
                        ).permitAll()
                        .requestMatchers(HttpMethod.GET, AppConstants.RESTAURANT_BASE_URL + "/**").permitAll()

                        //API only restaurant owner can access
                        .requestMatchers(HttpMethod.POST, AppConstants.RESTAURANT_BASE_URL + "/**").hasRole("RESTAURANT_OWNER")
                        .requestMatchers(HttpMethod.PUT, AppConstants.RESTAURANT_BASE_URL + "/**").hasRole("RESTAURANT_OWNER")
                        .requestMatchers(HttpMethod.DELETE, AppConstants.RESTAURANT_BASE_URL + "/**").hasRole("RESTAURANT_OWNER")

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