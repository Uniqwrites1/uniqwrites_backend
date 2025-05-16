package com.uniqwrites.security;

import com.uniqwrites.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private UserDetailsService userDetailsService;    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS", "HEAD"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers"));
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Length", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder)
                .and()
                .build();
    }    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // First explicitly handle OPTIONS requests for preflight
        http
            .csrf().disable()
            .cors().and()  // This uses the corsConfigurationSource bean if defined
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
                // Handle preflight OPTIONS requests
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()                // Public endpoints
                .antMatchers("/", "/api/public/**", "/resources/**", "/static/**", "/css/**", "/js/**").permitAll()
                .antMatchers("/api/auth/**", "/auth/**").permitAll()
                // Direct auth endpoints for frontend compatibility
                .antMatchers("/signup", "/login", "/google/login", "/forgot-password").permitAll()
                .antMatchers("/api/test/public", "/api/test/echo", "/api/test/cors-test").permitAll()
                .antMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                // Role-based endpoints
                .antMatchers("/api/admin/**", "/admin/**").hasRole("ADMIN")
                .antMatchers("/api/student/**").hasRole("STUDENT")
                .antMatchers("/api/tutor/**", "/teacher/apply").hasAnyRole("TUTOR", "TEACHER")
                .antMatchers("/api/sponsor/**").hasRole("SPONSOR")
                .antMatchers("/parent/requests").hasAnyRole("STUDENT", "PARENT") 
                .antMatchers("/school/request").hasAnyRole("SCHOOL_ADMIN")
                .anyRequest().authenticated();

        // Add JWT filter before processing requests
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
