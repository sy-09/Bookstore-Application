package com.example.bookservice.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.example.bookservice.model.Book;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository(value = "bookRepository")
public interface BookRepository extends ReactiveMongoRepository<Book, String>
{
	public Mono<Book> findByIsbn(String isbn);
	public Flux<Book> findByAuthorAuthorName(String authorName);
	public Mono<Book> findByBookName(String bookName);
}