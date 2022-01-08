package com.example.updateservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

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
@Document
public class Book
{
	@Id
	private String id;
	@Indexed(unique = true)
	@Field("book_name")
	private String bookName;
	private double price;
	@Indexed(unique = true)
	private String isbn;
	private double rating;
	private int quantity;	
	private Author author;
	
	public Book(String bookName, double price, String isbn, double rating, int quantity, Author author) 
	{
		super();
		this.bookName = bookName;
		this.price = price;
		this.isbn = isbn;
		this.rating = rating;
		this.quantity = quantity;
		this.author = author;
	}
	
	
}
