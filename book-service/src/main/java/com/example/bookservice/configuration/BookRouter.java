package com.example.bookservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.example.bookservice.handler.BookHandler;
import com.example.bookservice.model.Constants;

@Configuration
public class BookRouter 
{
	@Bean
	public RouterFunction<ServerResponse> bookRoutes(BookHandler bookHandler)
	{
		return RouterFunctions.route()
				.nest(RequestPredicates.path(Constants.ENDPOINT_VERSION),
					builder ->
						builder
						.POST(Constants.BOOK_ENDPOINT,bookHandler::addBook)
						.GET(Constants.BOOK_ENDPOINT+"/book/{bookName}",request -> bookHandler.findByBookName(request))
						.GET(Constants.BOOK_ENDPOINT+"/author/{authorName}",bookHandler::findByAuthorName)
						.GET(Constants.BOOK_ENDPOINT+"/isbn/{isbn}",bookHandler::findByIsbn)
						.PUT(Constants.BOOK_ENDPOINT,bookHandler::updateBook)
						.DELETE(Constants.BOOK_ENDPOINT+"/{isbn}",bookHandler::deleteBook)
						.build()
					)
				.build();
	}
}