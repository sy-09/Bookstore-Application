package com.example.updateservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.updateservice.dto.BookDto;
import com.example.updateservice.dto.UpdateDto;
import com.example.updateservice.exception.UnprocessableEntityException;
import com.example.updateservice.model.Author;
import com.example.updateservice.model.Constants;
import com.example.updateservice.repository.BookRepository;
import com.example.updateservice.util.CustomObjectMapper;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service(value = "updateService")
public class UpdateServiceImpl implements UpdateService
{
	private BookRepository bookRepo;
	private CustomObjectMapper mapper;
	
	@Autowired
	public UpdateServiceImpl(BookRepository bookRepo,CustomObjectMapper mapper)
	{
		this.mapper = mapper;
		this.bookRepo = bookRepo;
	}

	@Override
	public Mono<ResponseEntity<BookDto>> updateBookQuantity(int count, String isbn)
	{
		log.info("@Update-Service:: Updating book qunatity, ISBN: {}",isbn);
		return bookRepo.findByIsbn(isbn)
					.switchIfEmpty(Mono.error(
							new UnprocessableEntityException(Constants.UNPROCESSALE_EXCEPTION_MESSAGE+isbn)))
					.flatMap(book -> {
						book.setQuantity(book.getQuantity()+count);
						return bookRepo.save(book)
									.map(newBook -> {
										log.info("@Update-Service:: Book quantity updated, ISBN: {}",isbn);
										return ResponseEntity.ok().body(mapper.bookToBookDto(newBook));
									});
					});
	}

	@Override
	public Mono<ResponseEntity<BookDto>> updateBook(UpdateDto updateDto,String isbn) 
	{
		log.info("@Update-Service:: Updating book, ISBN: {}",isbn);
		return bookRepo.findByIsbn(isbn)
					.switchIfEmpty(Mono.error(
							new UnprocessableEntityException(Constants.UNPROCESSALE_EXCEPTION_MESSAGE+isbn)))
					.flatMap(book -> {
						Author author = book.getAuthor();
						book.setBookName(updateDto.getBookName());
						book.setPrice(updateDto.getPrice());
						author.setAuthorName(updateDto.getAuthorName());
						author.setCountry(updateDto.getAuthorCountry());
						return bookRepo.save(book)
									.map(newBook -> {
										log.info("@Update-Service:: Book updated, ISBN: {}",isbn);
										return ResponseEntity.ok(mapper.bookToBookDto(newBook));
									});
					});
	}

	@Override
	public Mono<ResponseEntity<BookDto>> updateIsbn(String newIsbn,String oldIsbn)
	{
		log.info("@Update-Service:: Updating book isbn, ISBN: {}",oldIsbn);
		return bookRepo.findByIsbn(oldIsbn)
				.switchIfEmpty(Mono.error(
						new UnprocessableEntityException(Constants.UNPROCESSALE_EXCEPTION_MESSAGE+oldIsbn)))
				.flatMap(book -> {
					book.setIsbn(newIsbn);
					return bookRepo.save(book)
								.map(newBook ->{
									log.info("@Update-Service:: Book isbn updated, ISBN: {}",newIsbn);
									return ResponseEntity.ok(mapper.bookToBookDto(newBook));
								});
				});
	}
	
	@Override
	public Mono<ResponseEntity<BookDto>> updateRating(int rating,String isbn)
	{
		log.info("@Update-Service:: Updating book rating, ISBN: {}",isbn);
		return bookRepo.findByIsbn(isbn)
				.switchIfEmpty(Mono.error(
						new UnprocessableEntityException(Constants.UNPROCESSALE_EXCEPTION_MESSAGE+isbn)))
				.flatMap(book -> {
					double newRating = (book.getRating() + rating)/ 2;
					book.setRating(newRating);
					return bookRepo.save(book)
							.map(newBook -> {
								log.info("@Update-Service:: Updated book rating, ISBN: {}",isbn);
								return ResponseEntity.ok(mapper.bookToBookDto(newBook));
							});
				});
	}
}
