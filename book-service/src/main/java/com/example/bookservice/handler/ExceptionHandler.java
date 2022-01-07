package com.example.bookservice.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.example.bookservice.exception.BadRequestException;
import com.example.bookservice.model.ErrorMessage;
import com.example.bookservice.model.ExceptionResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class ExceptionHandler implements ErrorWebExceptionHandler
{
	@Autowired
	private ObjectMapper objectMapper;
	
	@Override
	public Mono<Void> handle(ServerWebExchange exchange, Throwable ex)
	{
		ServerHttpResponse serverResponse = exchange.getResponse();
		serverResponse.getHeaders().setContentType(MediaType.APPLICATION_JSON);
		
		if(ex instanceof BadRequestException)
			return this.handleBadrequestException(serverResponse,(BadRequestException) ex);
		if(ex instanceof DuplicateKeyException)
			return this.handleDuplicateKeyException(serverResponse, (DuplicateKeyException) ex);
		return null;
	}

	private Mono<Void> handleBadrequestException(ServerHttpResponse serverResponse, BadRequestException ex)
	{
		log.error("@Book-Service:: Constraint Violation Exception has occurred");
		List<ErrorMessage> errorMessages = ex.getViolations().parallelStream()
													.map(errorMsg -> {
														return ErrorMessage.builder()
																		.message("Invalid Mandatory Parameter")
																		.moreInfo(errorMsg)
																		.status(HttpStatus.BAD_REQUEST.value())
																		.build();
													})
													.collect(Collectors.toList());
		ExceptionResponse errorResponse = new ExceptionResponse(errorMessages);
		Optional<byte[]> response = Optional.empty();
		try 
		{
			response = Optional.of(objectMapper.writeValueAsBytes(errorResponse));
		} 
		catch (JsonProcessingException e)
		{
			e.printStackTrace();
		}
		
		DataBuffer responseData = response.map(serverResponse.bufferFactory()::wrap)
									.orElse(serverResponse.bufferFactory().wrap("".getBytes()));
		serverResponse.setStatusCode(HttpStatus.BAD_REQUEST);
		
		return serverResponse.writeWith(Mono.just(responseData));
	}
	
	private Mono<Void> handleDuplicateKeyException(ServerHttpResponse serverResponse,DuplicateKeyException exception)
	{
		log.error("@Book-Service:: DuplicateKeyException has occurred");
		ErrorMessage error = ErrorMessage.builder()
								.message("Invalid Mandatory Parameter")
								.moreInfo(exception.getMessage())
								.status(HttpStatus.BAD_REQUEST.value())
								.build();
		List<ErrorMessage> errorMessages = new ArrayList<>();
		errorMessages.add(error);
		Optional<byte[]> response = Optional.empty();
		
		try 
		{
			response = Optional.of(objectMapper.writeValueAsBytes(new ExceptionResponse(errorMessages)));
		} 
		catch (JsonProcessingException e)
		{
			e.printStackTrace();
		}
		
		DataBuffer responseData = response.map(serverResponse.bufferFactory()::wrap)
				.orElse(serverResponse.bufferFactory().wrap("".getBytes()));
		
		serverResponse.setStatusCode(HttpStatus.BAD_REQUEST);
		
		return serverResponse.writeWith(Mono.just(responseData));
	}
}
