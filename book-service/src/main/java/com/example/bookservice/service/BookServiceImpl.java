package com.example.bookservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bookservice.dto.BookDto;
import com.example.bookservice.exception.UnprocessableEntityException;
import com.example.bookservice.mapper.CustomObjectMapper;
import com.example.bookservice.model.Book;
import com.example.bookservice.model.Constants;
import com.example.bookservice.repository.BookRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Shivam
 * This class is used to write business logic and communicate with repository layer.
 * @Structure:
 * 	@Methods:
 * 		1. public Mono<ServerResponse> addBook(ServerRequest request): to add a book.
 * 		2. public Mono<ServerResponse> findByBookName(ServerRequest request): to find book by book name.
 * 		3. public Mono<ServerResponse> findByAuthorName(ServerRequest request): to find book by author name.
 * 		4. public Mono<ServerResponse> findByIsbn(ServerRequest request): find book by ISBN.
 * 		5. public Mono<ServerResponse> deleteBook(ServerRequest request): to delete book by ISBN.
 * 	@Fields:
 * 		1. private BookRepository bookRepository: To communicate with book repository.
 *		2. private CustomObjectMapper mapper: To map different objects.
 */
@Service(value = "bookService")
public class BookServiceImpl implements BookService
{
	private BookRepository bookRepository;
	private CustomObjectMapper mapper;
	
	@Autowired
	public BookServiceImpl(BookRepository bookRepository, CustomObjectMapper mapper)
	{
		super();
		this.bookRepository = bookRepository;
		this.mapper = mapper;
	}
	
	/**
	 * To add a new book.
	 * @Structure:
	 * 	1. Call Repository layer.
	 * 	2. Map to bookDto.
	 * 	3. @return Mono<BookDto>.
	 */
	@Override
	public Mono<BookDto> addBook(BookDto bookDto)
	{
		return bookRepository.save(new Book(bookDto))
							.map(book -> mapper.bookDtoFromBook(book));
	}
	
	/**
	 * To find book by ISBN.
	 * @Structure:
	 * 	1. Call repository method.
	 * 	2. If empty response:
	 * 		throw UnprocessableEntityException
	 * 	3. @return Mono<BookDto>
	 */
	@Override
	public Mono<BookDto> findByIsbn(String isbn)
	{
		return bookRepository.findByIsbn(isbn)
						.switchIfEmpty(Mono.error(
								new UnprocessableEntityException(Constants.UNPROCESSALE_EXCEPTION_MESSAGE+isbn)))
						.map(book -> mapper.bookDtoFromBook(book));
	}
	
	/**
	 * To find book by author name.
	 * @Structure:
	 * 	1. Call repository method.
	 * 	2. If empty response:
	 * 		throw UnprocessableEntityException
	 * 	3. @return Mono<BookDto>
	 */
	@Override
	public Flux<BookDto> findByAuthorName(String authoName)
	{
		return bookRepository.findByAuthorAuthorName(authoName)
						.switchIfEmpty(Mono.error(
								new UnprocessableEntityException(Constants.UNPROCESSALE_EXCEPTION_MESSAGE+authoName)))
						.map(book -> mapper.bookDtoFromBook(book));
	}
	
	/**
	 * To delete book by ISBN.
	 * @Structure:
	 * 	1. Call repository findByIsbn method.
	 * 	2. if empty response:
	 * 		throw UnprocessableEntityException
	 * 	3. Call repository layer to delete book.
	 * 	4. @return Mono<Void>.
	 */
	@Override
	public Mono<Void> deleteBook(String isbn) 
	{
		return bookRepository.findByIsbn(isbn)
							.switchIfEmpty(Mono.error(
									new UnprocessableEntityException(Constants.UNPROCESSALE_EXCEPTION_MESSAGE+isbn)))
							.flatMap(book -> bookRepository.delete(book));
	}
		
	/**
	 * To find book by book name.
	 * @Structure:
	 * 	1. Call repository method.
	 * 	2. If empty response:
	 * 		throw UnprocessableEntityException
	 * 	3. @return Mono<BookDto>
	 */
	@Override
	public Mono<BookDto> findByBookName(String bookName)
	{
		return bookRepository.findByBookName(bookName)
							.switchIfEmpty(Mono.error(
									new UnprocessableEntityException(Constants.UNPROCESSALE_EXCEPTION_MESSAGE+bookName)))
							.map(book -> mapper.bookDtoFromBook(book));
	}
	
}
