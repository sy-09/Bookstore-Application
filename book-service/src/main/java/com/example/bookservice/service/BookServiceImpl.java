package com.example.bookservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bookservice.dto.BookDto;
import com.example.bookservice.dto.UpdateDto;
import com.example.bookservice.exception.UnprocessableEntityException;
import com.example.bookservice.mapper.CustomObjectMapper;
import com.example.bookservice.model.Book;
import com.example.bookservice.model.Constants;
import com.example.bookservice.repository.BookRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

	@Override
	public Mono<BookDto> addBook(BookDto bookDto)
	{
		return bookRepository.save(new Book(bookDto))
							.map(book -> mapper.bookDtoFromBook(book));
	}

	@Override
	public Mono<BookDto> findByIsbn(String isbn)
	{
		return bookRepository.findByIsbn(isbn)
						.switchIfEmpty(Mono.error(
								new UnprocessableEntityException(Constants.UNPROCESSALE_EXCEPTION_MESSAGE+isbn)))
						.map(book -> mapper.bookDtoFromBook(book));
	}

	@Override
	public Flux<BookDto> findByAuthorName(String authoName)
	{
		return bookRepository.findByAuthorAuthorName(authoName)
						.switchIfEmpty(Mono.error(
								new UnprocessableEntityException(Constants.UNPROCESSALE_EXCEPTION_MESSAGE+authoName)))
						.map(book -> mapper.bookDtoFromBook(book));
	}

	@Override
	public Mono<Void> deleteBook(String isbn) 
	{
		return bookRepository.findByIsbn(isbn)
							.switchIfEmpty(Mono.error(
									new UnprocessableEntityException(Constants.UNPROCESSALE_EXCEPTION_MESSAGE+isbn)))
							.flatMap(book -> bookRepository.delete(book));
	}

	@Override
	public Mono<BookDto> updateBook(UpdateDto updateDto,String isbn)
	{
		return bookRepository.findByIsbn(isbn)
							.switchIfEmpty(Mono.error(
									new UnprocessableEntityException(Constants.UNPROCESSALE_EXCEPTION_MESSAGE+isbn)))
							.flatMap(book -> {
								book.setBookName(updateDto.getBookName());
								book.setPrice(updateDto.getPrice());
								book.setAuthor(updateDto.getAuthor());
								return bookRepository.save(book);
							})
							.map(book -> mapper.bookDtoFromBook(book));
	}

	@Override
	public Mono<BookDto> findByBookName(String bookName)
	{
		return bookRepository.findByBookName(bookName)
							.switchIfEmpty(Mono.error(
									new UnprocessableEntityException(Constants.UNPROCESSALE_EXCEPTION_MESSAGE+bookName)))
							.map(book -> mapper.bookDtoFromBook(book));
	}
	
}
