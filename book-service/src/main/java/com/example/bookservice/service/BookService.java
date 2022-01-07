package com.example.bookservice.service;

import com.example.bookservice.dto.BookDto;
import com.example.bookservice.dto.UpdateDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BookService 
{
	public Mono<BookDto> addBook(BookDto bookDto);
	public Mono<BookDto> findByIsbn(String isbn);
	public Flux<BookDto> findByAuthorName(String authoName);
	public Mono<Void> deleteBook(String isbn);
	public Mono<BookDto> updateBook(UpdateDto updateDto,String isbn);
	public Mono<BookDto> findByBookName(String bookName);
}
