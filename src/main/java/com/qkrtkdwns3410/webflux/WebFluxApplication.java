package com.qkrtkdwns3410.webflux;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import java.time.Duration;
import java.util.Arrays;

@SpringBootApplication
public class WebFluxApplication {
      
      public static void main(String[] args) {
            SpringApplication.run(WebFluxApplication.class, args);
      }
      
}
