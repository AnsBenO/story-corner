package com.ansbeno.books_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class BooksServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BooksServiceApplication.class, args);
	}

}
