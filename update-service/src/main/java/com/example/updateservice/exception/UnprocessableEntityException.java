package com.example.updateservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UnprocessableEntityException extends RuntimeException
{
	private static final long serialVersionUID = 1L;
	private String message;
}
