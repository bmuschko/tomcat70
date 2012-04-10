package org.apache.tomcat.spring.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.apache.tomcat.spring.example.IndexController;

@Configuration
public class WebMvcContextConfiguration {
    @Bean
    public IndexController indexController() {
        return new IndexController();
    }
}