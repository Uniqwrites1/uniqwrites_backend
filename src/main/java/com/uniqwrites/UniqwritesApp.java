package com.uniqwrites;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import java.util.Properties;

@SpringBootApplication
public class UniqwritesApp {

    public static void main(String[] args) {
        // Load environment variables from .env file
        Dotenv dotenv = Dotenv.configure().load();
        
        // Create Spring application
        SpringApplication app = new SpringApplication(UniqwritesApp.class);
        
        // Create and configure environment
        ConfigurableEnvironment environment = new StandardEnvironment();
        MutablePropertySources propertySources = environment.getPropertySources();
        
        // Convert Dotenv to Properties
        Properties props = new Properties();
        dotenv.entries().forEach(entry -> props.put(entry.getKey(), entry.getValue()));
        
        // Add properties to environment
        propertySources.addFirst(new PropertiesPropertySource("dotenv", props));
        
        // Set environment
        app.setEnvironment(environment);
        
        // Run application
        app.run(args);
    }
}
