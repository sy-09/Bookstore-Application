package com.example.bookservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.example.bookservice.dto.BookDto;

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
	
	public Book(BookDto bookDto)
	{
		super();
		this.bookName = bookDto.getBookName();
		this.price = bookDto.getPrice();
		this.quantity = bookDto.getQuantity();
		this.author = bookDto.getAuthor();
		this.isbn = bookDto.getIsbn();
		this.rating = 0.0;
	}
}
