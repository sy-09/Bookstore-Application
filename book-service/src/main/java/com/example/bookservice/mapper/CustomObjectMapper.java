package com.example.bookservice.mapper;

import org.mapstruct.Mapper;

import com.example.bookservice.dto.BookDto;
import com.example.bookservice.model.Book;

@Mapper(componentModel = "spring")
public interface CustomObjectMapper
{
	BookDto bookDtoFromBook(Book book);
}
