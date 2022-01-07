package com.example.bookservice.dto;

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
	private String bookName;
	private double price;
	private Author author;
}
