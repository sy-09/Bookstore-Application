package com.example.updateservice.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

import com.example.updateservice.exception.UnprocessableEntityException;
import com.example.updateservice.model.Constants;
import com.example.updateservice.model.ErrorMessage;
import com.example.updateservice.model.ExceptionResponse;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@ControllerAdvice
public class ExceptionController 
{
	@ExceptionHandler(ConstraintViolationException.class)
	public Mono<ResponseEntity<ExceptionResponse>> handleConstrainViolation(ConstraintViolationException exception)
	{
		log.error("@Update-Service:: Handling Constraint Violation Exception");
		List<ErrorMessage> messages = exception.getConstraintViolations()
										.parallelStream()
										.map(violation -> 
											ErrorMessage.builder()
													.message(Constants.BAD_REQUEST_MESSAGE)
													.status(HttpStatus.BAD_REQUEST.value())
													.moreInfo(violation.getMessage())
													.build()
										)
										.collect(Collectors.toList());
		return Mono.just(ResponseEntity.badRequest().body(new ExceptionResponse(messages)));
	}
	
	@ExceptionHandler(WebExchangeBindException.class)
	public Mono<ResponseEntity<ExceptionResponse>> handleWebExchangeBind(WebExchangeBindException exception)
	{
		log.error("@Update-Service:: Handling Web Exchange Bind Exception");
		List<ErrorMessage> messages = exception.getAllErrors()
										.parallelStream()
										.map(error -> 
											ErrorMessage.builder()
												.message(Constants.BAD_REQUEST_MESSAGE)
												.status(HttpStatus.BAD_REQUEST.value())
												.moreInfo(error.getDefaultMessage())
												.build()
										)
										.collect(Collectors.toList());
		
		return Mono.just(ResponseEntity.badRequest().body(new ExceptionResponse(messages)));
	}
	
	@ExceptionHandler(UnprocessableEntityException.class)
	public Mono<ResponseEntity<ExceptionResponse>> handleUnprocessableEntity(UnprocessableEntityException exception)
	{
		log.error("@Update-Service:: Handling Unpprocessable Entity Exception");
		List<ErrorMessage> messages = new ArrayList<>();
		messages.add(
				ErrorMessage.builder()
						.message(Constants.UNPROCESSALE_EXCEPTION_MESSAGE)
						.moreInfo(exception.getMessage())
						.status(HttpStatus.UNPROCESSABLE_ENTITY.value())
						.build());
		
		return Mono.just(ResponseEntity.unprocessableEntity().body(new ExceptionResponse(messages)));
	}
}
