package com.adminportal.service;

import java.util.List;

import com.adminportal.model.Book;

public interface BookService {

	void saveBook(Book book);

	List<Book> findAllBooks();
	
	Book findById(Long id);
	
	void deleteById(Long id);
}
