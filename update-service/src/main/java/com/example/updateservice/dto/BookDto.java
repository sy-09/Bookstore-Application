package com.example.updateservice.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import org.hibernate.validator.constraints.ISBN;

import com.example.updateservice.model.Author;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BookDto
{
	@JsonProperty("book_name")
	private String bookName;
	@Positive(message = "Price must be a positive number")
	private double price;
	@ISBN(message = "Invalid ISBN")
	private String isbn;
	@PositiveOrZero(message = "Quantity can not be less than 0")
	private int quantity;
	@NotNull(message = "Author details are required")
	private Author author;
}