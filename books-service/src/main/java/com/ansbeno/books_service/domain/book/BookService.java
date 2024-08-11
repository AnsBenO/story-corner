package com.ansbeno.books_service.domain.book;

import com.ansbeno.books_service.dto.BookDto;
import com.ansbeno.books_service.dto.PagedResultDto;

import javassist.NotFoundException;

public interface BookService {

      PagedResultDto<BookDto> findAll(int page);

      BookDto save(BookDto book);

      BookDto findById(long id) throws NotFoundException;

      BookDto findByCode(String code) throws NotFoundException;

      void update(BookDto book) throws NotFoundException;

      void deleteById(Long id);

      void deleteByCode(String code);
}
