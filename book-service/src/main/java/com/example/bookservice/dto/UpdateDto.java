package com.example.bookservice.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.data.mongodb.core.index.Indexed;

import com.example.bookservice.model.Author;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateDto
{
	@JsonProperty("book_name")
	@Indexed(unique = true)
	private String bookName;
	@Positive(message = "Price must be positive value")
	private double price;
	@NotNull(message = "Author can not be null")
	private Author author;
}
