package com.ansbeno.books_service;

import org.springframework.boot.SpringApplication;

public class TestBooksServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(BooksServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
