package com.example.bookservice.handler;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.example.bookservice.dto.BookDto;
import com.example.bookservice.dto.UpdateDto;
import com.example.bookservice.exception.BadRequestException;
import com.example.bookservice.service.BookService;

import reactor.core.publisher.Mono;

@Component
public class BookHandler
{
	@Autowired
	private BookService bookService;
	@Autowired
	private Validator validator;
	
	public Mono<ServerResponse> addBook(ServerRequest request)
	{
		URI createdUri = URI.create(request.path());
		return request.bodyToMono(BookDto.class)
					.doOnNext(this::validate)
					.flatMap(bookDto -> bookService.addBook(bookDto))
					.flatMap(book -> ServerResponse.created(createdUri).bodyValue(book));
					
	}
	
	public Mono<ServerResponse> findByBookName(ServerRequest request)
	{
		String bookName = request.pathVariable("bookName");
		return bookService.findByBookName(bookName)
					.flatMap(book -> ServerResponse.ok().bodyValue(book));
	}
	
	public Mono<ServerResponse> findByAuthorName(ServerRequest request)
	{
		String authorName = request.pathVariable("authorName");
		return ServerResponse.ok().body(bookService.findByAuthorName(authorName), BookDto.class);
	}
	
	public Mono<ServerResponse> findByIsbn(ServerRequest request)
	{
		String isbn = request.pathVariable("isbn");
		return bookService.findByIsbn(isbn)
						.flatMap(book -> ServerResponse.ok().bodyValue(book));
	}
	
	public Mono<ServerResponse> updateBook(ServerRequest request)
	{
		return request.bodyToMono(UpdateDto.class)
					.flatMap(updateDto -> bookService.updateBook(updateDto, request.pathVariable("isbn")))
					.flatMap(book -> ServerResponse.ok().bodyValue(book));
	}
	
	public Mono<ServerResponse> deleteBook(ServerRequest request)
	{
		String isbn = request.pathVariable("isbn");
		return ServerResponse.ok().bodyValue(bookService.deleteBook(isbn));
	}
	
	private void validate(BookDto bookDto) 
	{
		Set<ConstraintViolation<BookDto>> violation = validator.validate(bookDto);
		if(!violation.isEmpty())
		{
			List<String> errorMessages = violation.parallelStream()
											.map(ConstraintViolation::getMessage)
											.collect(Collectors.toList());
			throw new BadRequestException(errorMessages);
		}
	}
}
