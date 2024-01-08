package com.adminportal.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adminportal.model.Book;
import com.adminportal.repository.BookRepository;
import com.adminportal.repository.BookToCartItemRepository;
import com.adminportal.service.BookService;

@Service
public class BookServiceImpl implements BookService {

	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private BookToCartItemRepository bookToCartItemRepository; 

	@Override
	public void saveBook(Book book) {
		bookRepository.save(book);
	}

	@Override
	public List<Book> findAllBooks() {
		return bookRepository.findAll();
	}

	@Override
	public Book findById(Long id) {
		return bookRepository.findById(id).get();
	}

	@Override
	public void deleteById(Long id) {
		bookToCartItemRepository.deleteById(id);
		bookRepository.deleteById(id);	
	}

}
