package com.example.updateservice.util;

import org.mapstruct.Mapper;

import com.example.updateservice.dto.BookDto;
import com.example.updateservice.model.Book;

@Mapper(componentModel = "spring")
public interface CustomObjectMapper 
{
	public BookDto bookToBookDto(Book book);
}
