package com.ansbeno.books_service.api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ansbeno.books_service.domain.book.BookService;
import com.ansbeno.books_service.dto.BookDto;
import com.ansbeno.books_service.dto.PagedResultDto;

import jakarta.validation.Valid;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/books")
@Slf4j
class BookController {
      private final BookService bookService;

      @GetMapping
      ResponseEntity<PagedResultDto<BookDto>> getAllBooks(
                  @RequestParam(defaultValue = "1") int page) {
            PagedResultDto<BookDto> books = bookService.findAll(page);
            return new ResponseEntity<>(books, HttpStatus.OK);
      }

      @PostMapping
      ResponseEntity<BookDto> createBook(@Valid @RequestBody BookDto bookDto) {
            BookDto savedBook = bookService.save(bookDto);
            return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
      }

      @GetMapping("/{code}")
      ResponseEntity<BookDto> getBookByCode(@PathVariable String code) throws NotFoundException {
            BookDto book = bookService.findByCode(code);
            log.info("Get book: {}", code);
            return new ResponseEntity<>(book, HttpStatus.OK);

      }

      @GetMapping("/id/{id}")
      ResponseEntity<BookDto> getBookById(@PathVariable Long id) throws NotFoundException {
            BookDto book = bookService.findById(id);
            log.info("Get book by id: {}", id);
            return new ResponseEntity<>(book, HttpStatus.OK);

      }

      @PutMapping("/{code}")
      ResponseEntity<BookDto> updateBook(@PathVariable String code, @Valid @RequestBody BookDto bookDto)
                  throws NotFoundException {
            bookService.update(bookDto);
            log.info("Update book: {}", code);
            return new ResponseEntity<>(bookDto, HttpStatus.OK);

      }

      @DeleteMapping("/{code}")
      ResponseEntity<Void> deleteBookByCode(@PathVariable String code) {
            bookService.deleteByCode(code);
            log.info("Delete order: {}", code);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }
}
