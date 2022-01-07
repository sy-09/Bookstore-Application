package com.example.bookservice.exception;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BadRequestException extends RuntimeException
{
	private static final long serialVersionUID = 1L;
	
	private List<String> violations;
	
}
