package com.ansbeno.books_service.domain.book;

import com.ansbeno.books_service.domain.exceptions.BookNotFoundException;
import com.ansbeno.books_service.domain.dto.BookDto;
import com.ansbeno.books_service.domain.dto.PagedResultDto;

public interface BookService {

      PagedResultDto<BookDto> findAll(int page);

      BookDto save(BookDto book);

      BookDto findByCode(String code) throws BookNotFoundException;

      void update(BookDto book) throws BookNotFoundException;

      void deleteById(Long id);

      void deleteByCode(String code);
}
