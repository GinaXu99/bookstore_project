package com.bookstore.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.bookstore.model.Book;

public interface BookService {

	void saveBook(Book book);

	List<Book> findAllBooks();

	Book findById(Long id);
	
	Page<Book> listBookByPageAndKeyword(int pageNum, String sortDir, String keyword, String sortField);
	
}
