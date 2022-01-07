package com.example.bookservice.handler;

import java.util.ArrayList;
import java.util.List;
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
import com.example.bookservice.exception.InternalServerException;
import com.example.bookservice.exception.UnprocessableEntityException;
import com.example.bookservice.model.Constants;
import com.example.bookservice.model.ErrorMessage;
import com.example.bookservice.model.ExceptionResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * @author Shivam
 *	To handle global exceptions.
 * @Structure:
 * 	@Methods;
 * 		1. public Mono<Void> handle(ServerWebExchange exchange, Throwable ex): To route exceptions.
 * 		2. private Mono<Void> handleInternalServerException(ServerHttpResponse serverResponse, Throwable ex)
 * 		3. private Mono<Void> handleBadRequestException(ServerHttpResponse serverResponse, (BadRequestException) ex)
 * 		4. private Mono<Void> handleUnprocessableEntityException(ServerHttpResponse serverResponse,
 *																			UnprocessableEntityException ex)
 *		5. private Mono<Void> handleDuplicateKeyException(ServerHttpResponse serverResponse,
 *																				DuplicateKeyException exception)																
 *		6. private byte[] getResponseAsByte(ExceptionResponse errorResponse): To write response as byte[].
 *	@Fields:
 *		1. ObjectMapper objectMapper: to write objects as byte[].
 */
@Slf4j
@Component
public class ExceptionHandler implements ErrorWebExceptionHandler
{
	@Autowired
	private ObjectMapper objectMapper;
	
	/**
	 * This method route exception to appropriate handler.
	 */
	@Override
	public Mono<Void> handle(ServerWebExchange exchange, Throwable ex)
	{
		ServerHttpResponse serverResponse = exchange.getResponse();
		serverResponse.getHeaders().setContentType(MediaType.APPLICATION_JSON);
		
		if(ex instanceof BadRequestException)
			return this.handleBadrequestException(serverResponse,(BadRequestException) ex);
		if(ex instanceof DuplicateKeyException)
			return this.handleDuplicateKeyException(serverResponse, (DuplicateKeyException) ex);
		if(ex instanceof UnprocessableEntityException)
			return this.handleUnprocessableEntityException(serverResponse,(UnprocessableEntityException) ex);
		else
			return this.handleInternalServerException(serverResponse,ex);
	}
	
	/**
	 * Handle Internal Server Exception.
	 * @param serverResponse
	 * @param ex
	 * 	@Structure:
	 * 		1. Make ExceptionResponse
	 * 		2. Call getResponseAsByte to get ExceptionResponse in byte[].
	 * 		3. Set @status: 500
	 * 		4. Inject response byte array in server response.
	 * 		5. return serverResponse.
	 * @return
	 */
	private Mono<Void> handleInternalServerException(ServerHttpResponse serverResponse, Throwable ex)
	{
		log.error("@Book-Service:: Internal Server Error has occurred");
		log.debug("@Book-Service:: "+ex.getLocalizedMessage());
		
		ErrorMessage errorMessage = ErrorMessage.builder()
												.message(Constants.INTERNAL_SERVER_ERROR_MESSAGE)
												.moreInfo(ex.getLocalizedMessage())
												.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
												.build();
		List<ErrorMessage> messages = new ArrayList<>();
		messages.add(errorMessage);
		byte[] response = this.getResponseAsByte(new ExceptionResponse(messages));
		
		DataBuffer responseData = serverResponse.bufferFactory().wrap(response);
		serverResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
		
		return serverResponse.writeWith(Mono.just(responseData));
	}
	
	/**
	 * Handle Unprocessable Entity Exception.
	 * @param serverResponse
	 * @param ex
	 * 	@Structure:
	 * 		1. Make ExceptionResponse
	 * 		2. Call getResponseAsByte to get ExceptionResponse in byte[].
	 * 		3. Set @status: 422
	 * 		4. Inject response byte array in server response.
	 * 		5. return serverResponse.
	 * @return
	 */
	private Mono<Void> handleUnprocessableEntityException(ServerHttpResponse serverResponse,
																			UnprocessableEntityException ex) 
	{
		log.error("@Book-Service:: Unprocessable Entity has occurred");
		ErrorMessage message = ErrorMessage.builder()
										.message("Unprocessable Entity Passed")
										.moreInfo(ex.getMessage())
										.status(HttpStatus.UNPROCESSABLE_ENTITY.value())
										.build();
		List<ErrorMessage> errorMessage = new ArrayList<>();
		errorMessage.add(message);
		byte[] response = this.getResponseAsByte(new ExceptionResponse(errorMessage));
		
		DataBuffer responseMessage = serverResponse.bufferFactory().wrap(response);
		serverResponse.setStatusCode(HttpStatus.UNPROCESSABLE_ENTITY);
		
		return serverResponse.writeWith(Mono.just(responseMessage));
	}
	
	/**
	 * Handle Bad Request Exception.
	 * @param serverResponse
	 * @param ex
	 * 	@Structure:
	 * 		1. Get error messages from exceptions.
	 * 		2. Make ExceptionResponse
	 * 		3. Call getResponseAsByte to get ExceptionResponse in byte[].
	 * 		4. Set @status: 400
	 * 		5. Inject response byte array in server response.
	 * 		6. return serverResponse.
	 * @return
	 */
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
		
		byte[] response = this.getResponseAsByte(errorResponse);
		DataBuffer responseData = serverResponse.bufferFactory().wrap(response);
		
		serverResponse.setStatusCode(HttpStatus.BAD_REQUEST);
		return serverResponse.writeWith(Mono.just(responseData));
	}
	
	/**
	 * Handle Duplicate Key Exception.
	 * @param serverResponse
	 * @param ex
	 * 	@Structure:
	 * 		1. Make ExceptionResponse
	 * 		2. Call getResponseAsByte to get ExceptionResponse in byte[].
	 * 		3. Set @status: 400
	 * 		4. Inject response byte array in server response.
	 * 		5. return serverResponse.
	 * @return
	 */
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
		
		byte[] response = this.getResponseAsByte(new ExceptionResponse(errorMessages));
		DataBuffer responseData = serverResponse.bufferFactory().wrap(response);
		
		serverResponse.setStatusCode(HttpStatus.BAD_REQUEST);
		return serverResponse.writeWith(Mono.just(responseData));
	}
	
	/**
	 * To convert object to byte array.
	 * @param errorResponse
	 * @return byte[]
	 */
	private byte[] getResponseAsByte(ExceptionResponse errorResponse)
	{
		try 
		{
			return objectMapper.writeValueAsBytes(errorResponse);
		} 
		catch (JsonProcessingException e)
		{
			log.error("@Book-Service:: Can not write object value as byte");
			log.debug("@Book-Service:: "+e.getLocalizedMessage());
			throw new InternalServerException(e.getLocalizedMessage());
		}
	}
}
