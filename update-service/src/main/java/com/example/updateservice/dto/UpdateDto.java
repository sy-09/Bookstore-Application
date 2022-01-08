package com.example.updateservice.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDto
{
	@JsonProperty("book_name")
	@NotBlank(message = "Book name can not be blank")
	private String bookName;
	@Positive(message = "Price must be a positive integer")
	private int price;
	@JsonProperty("author_name")
	@NotBlank(message = "Author name can not be blank")
	private String authorName;
	@JsonProperty("author_country")
	private String authorCountry;
}
