package com.example.updateservice.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.example.updateservice.model.Book;

import reactor.core.publisher.Mono;

@Repository(value = "bookRepository")
public interface BookRepository extends ReactiveMongoRepository<Book, String>
{
	public Mono<Book> findByIsbn(String isbn);
}