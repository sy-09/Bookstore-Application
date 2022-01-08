package com.example.updateservice.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.updateservice.dto.BookDto;
import com.example.updateservice.dto.UpdateDto;
import com.example.updateservice.model.Constants;
import com.example.updateservice.service.UpdateService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping(Constants.ENDPOINT_VERSION)
@Validated
public class UpdateController 
{
	@Autowired
	private UpdateService updateService;
	
	@PutMapping(Constants.UPDATE_ENDPOINT+"/book/{isbn}")
	public Mono<ResponseEntity<BookDto>> updateBook(@RequestBody @Valid UpdateDto updateDto,
								@PathVariable @NotBlank(message = "ISBN can not be blank") String isbn)
	{
		log.info("@Update-Service:: Got request to update book with ISBN: {}",isbn);
		return updateService.updateBook(updateDto, isbn).log();
	}
	
	@PatchMapping(Constants.UPDATE_ENDPOINT+"/isbn/{newIsbn}/{oldIsbn}")
	public Mono<ResponseEntity<BookDto>> updateIsbn(
			@PathVariable @NotBlank(message = "New ISBN can not be blank") String newIsbn,
			@PathVariable @NotBlank(message = "Old ISBN can not be blank") String oldIsbn)
	{
		log.info("@Update-Service:: Got request to update isbn of book with ISBN: {}",oldIsbn);
		return updateService.updateIsbn(newIsbn, oldIsbn).log();
	}
	
	@PatchMapping(Constants.UPDATE_ENDPOINT+"/rating/{rating}/{isbn}")
	public Mono<ResponseEntity<BookDto>> updateRating(
			@PathVariable @Positive(message = "Rating must be a positive number") int rating,
			@PathVariable @NotBlank(message = "Old ISBN can not be blank") String isbn)
	{
		log.info("@Update-Service:: Got request to update rating of book with ISBN: {}",isbn);
		return updateService.updateRating(rating, isbn).log();
	}
	
	@PatchMapping(Constants.UPDATE_ENDPOINT+"/quantity/{count}")
	public Mono<ResponseEntity<BookDto>> updateQuantity(
			@PathVariable @NotBlank(message = "Old ISBN can not be blank") String isbn,
			@PathVariable int count)
	{
		log.info("@Update-Service:: Got request to update quantity of book with ISBN: {}",isbn);
		return updateService.updateBookQuantity(count, isbn).log();
	}
}
