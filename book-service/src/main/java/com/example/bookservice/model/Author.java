package com.example.bookservice.model;

import javax.validation.constraints.NotEmpty;

import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Author 
{
	@JsonProperty("author_name")
	@NotEmpty(message = "Author name can not be empty")
	@Field("author_name")
	private String authorName;
	private String country;
}
