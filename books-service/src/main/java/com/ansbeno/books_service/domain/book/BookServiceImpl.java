package com.ansbeno.books_service.domain.book;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ansbeno.books_service.ApplicationProperties;
import com.ansbeno.books_service.dto.BookDto;
import com.ansbeno.books_service.dto.PagedResultDto;

import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
class BookServiceImpl implements BookService {
      private final BookRepository bookRepository;
      private final ApplicationProperties properties;

      @Override
      public PagedResultDto<BookDto> findAll(int pageNumber) {
            Sort sort = Sort.by("name").ascending();
            pageNumber = pageNumber <= 1 ? 0 : pageNumber - 1;
            Pageable pageable = PageRequest.of(pageNumber, properties.pageSize(), sort);
            Page<BookEntity> booksPage = bookRepository.findAll(pageable);
            return PagedResultDto.<BookDto>builder()
                        .data(booksPage.toList()
                                    .stream()
                                    .map(BookMapper::mapToBookDto).toList())
                        .totalElements(booksPage.getTotalElements())
                        .pageNumber(pageNumber + 1)
                        .totalPages(booksPage.getTotalPages())
                        .isFirst(booksPage.isFirst())
                        .isLast(booksPage.isLast())
                        .hasNext(booksPage.hasNext())
                        .hasPrevious(booksPage.hasPrevious())
                        .build();
      }

      @Override
      public BookDto save(BookDto bookDto) {
            BookEntity book = BookMapper.mapToBookEntity(bookDto);
            BookEntity addedBook = bookRepository.save(book);
            return BookMapper.mapToBookDto(addedBook);
      }

      @Override
      public BookDto findById(long id) throws NotFoundException {
            Optional<BookEntity> foundBook = bookRepository.findById(id);
            if (foundBook.isPresent()) {
                  BookEntity book = foundBook.get();
                  return BookMapper.mapToBookDto(book);
            }
            throw new NotFoundException("Book Not Found");
      }

      @Override
      public BookDto findByCode(String code) throws NotFoundException {
            Optional<BookEntity> foundBook = bookRepository.findByCode(code);
            if (foundBook.isPresent()) {
                  BookEntity book = foundBook.get();
                  return BookMapper.mapToBookDto(book);
            }
            throw new NotFoundException("Book Not Found");
      }

      @Override
      public void update(BookDto bookDto) throws NotFoundException {
            BookEntity book = BookMapper.mapToBookEntity(bookDto);
            bookRepository.save(book);
      }

      @Override
      public void deleteById(Long id) {
            Optional<BookEntity> bookOptional = bookRepository.findById(id);
            if (bookOptional.isPresent()) {
                  bookRepository.deleteById(id);
            }

      }

      @Override
      public void deleteByCode(String code) {
            Optional<BookEntity> bookOptional = bookRepository.findByCode(code);
            if (bookOptional.isPresent()) {
                  bookRepository.deleteByCode(code);
            }
      }

}
