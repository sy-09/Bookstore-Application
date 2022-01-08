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
import com.example.bookservice.exception.BadRequestException;
import com.example.bookservice.service.BookService;

import reactor.core.publisher.Mono;

/**
 * @author Shivam
 * This class is used to handle the routed requests and communicates with appropriate service class methods.
 * @Structure
 * 	@methods: 
 * 		1. public Mono<ServerResponse> addBook(ServerRequest request): to add a book.
 * 		2. public Mono<ServerResponse> findByBookName(ServerRequest request): to find book by book name.
 * 		3. public Mono<ServerResponse> findByAuthorName(ServerRequest request): to find book by author name.
 * 		4. public Mono<ServerResponse> findByIsbn(ServerRequest request): find book by ISBN.
 * 		5. public Mono<ServerResponse> deleteBook(ServerRequest request): to delete book by ISBN.
 * 		6. private void validate(BookDto bookDto): to validate book object.
 * 	@Fields:
 * 		1. BookService bookService: BookService object to call service layer methods.
 * 		2. Validator validator: Javax validator object to validate book object.
 */
@Component
public class BookHandler
{
	private BookService bookService;
	private Validator validator;
	
	@Autowired
	public BookHandler(BookService bookService,Validator validator)
	{
		this.bookService = bookService;
		this.validator = validator;
	}
	
	/**
	 * @param request
	 * @Structure:
	 * 	1. Get BookDto object from request.
	 * 	2. Validate object.
	 * 	3. Call service layer method.
	 * 	4. @return server response with saved object.
	 */
	public Mono<ServerResponse> addBook(ServerRequest request)
	{
		URI createdUri = URI.create(request.path());
		return request.bodyToMono(BookDto.class)
					.doOnNext(this::validate)
					.flatMap(bookDto -> bookService.addBook(bookDto))
					.flatMap(book -> ServerResponse.created(createdUri).bodyValue(book));
					
	}
	
	/**
	 * @param request
	 * @Structure:
	 * 	1. Get path variable.
	 * 	2. Call service layer method.
	 * 	@return server response with bookDto object.
	 */
	public Mono<ServerResponse> findByBookName(ServerRequest request)
	{
		String bookName = request.pathVariable("bookName");
		return bookService.findByBookName(bookName)
					.flatMap(book -> ServerResponse.ok().bodyValue(book));
	}
	
	/**
	 * @param request
	 * @Structure:
	 * 	1. Get path variable.
	 * 	2. Call service layer method
	 * 	3. @return return server response with bookDto object.
	 */
	public Mono<ServerResponse> findByAuthorName(ServerRequest request)
	{
		String authorName = request.pathVariable("authorName");
		return ServerResponse.ok().body(bookService.findByAuthorName(authorName), BookDto.class);
	}
	
	/**
	 * @param request
	 * @Structure:
	 * 	1. Get path variable.
	 * 	2. Call service layer method.
	 * 	@return server response with bookDto object.
	 */
	public Mono<ServerResponse> findByIsbn(ServerRequest request)
	{
		String isbn = request.pathVariable("isbn");
		return bookService.findByIsbn(isbn)
						.flatMap(book -> ServerResponse.ok().bodyValue(book));
	}
	
	/**
	 * @param request
	 * @Structure:
	 * 	1. Get path variable.
	 * 	2. Call service layer method.
	 * 	@return server response with bookDto object.
	 */
	public Mono<ServerResponse> deleteBook(ServerRequest request)
	{
		String isbn = request.pathVariable("isbn");
		return ServerResponse.ok().bodyValue(bookService.deleteBook(isbn));
	}
	
	/**
	 * Validate BookDto
	 * @param bookDto
	 * @Structure:
	 * 	1. Validate bookDto.
	 * 	2. If Set<ConstraintViolation<BookDto>> has size > 0
	 * 		2.1 Extract violation messages.
	 * 		2.2 throw BadRequestException(violation messages)
	 */
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
