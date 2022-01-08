package com.example.updateservice.service;

import org.springframework.http.ResponseEntity;

import com.example.updateservice.dto.BookDto;
import com.example.updateservice.dto.UpdateDto;

import reactor.core.publisher.Mono;

public interface UpdateService
{
	public Mono<ResponseEntity<BookDto>> updateBookQuantity(int count,String isbn);
	public Mono<ResponseEntity<BookDto>> updateBook(UpdateDto updateDto,String isbn);
	public Mono<ResponseEntity<BookDto>> updateIsbn(String newIsbn,String oldIsbn);
	public Mono<ResponseEntity<BookDto>> updateRating(int rating, String isbn);
}
