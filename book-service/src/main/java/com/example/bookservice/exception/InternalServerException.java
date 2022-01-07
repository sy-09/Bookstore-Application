package com.example.bookservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InternalServerException extends RuntimeException
{
	private static final long serialVersionUID = 1L;
	
	private String message;
	
}
