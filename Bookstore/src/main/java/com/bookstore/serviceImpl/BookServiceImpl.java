package com.bookstore.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.bookstore.model.Book;
import com.bookstore.repository.BookRepository;
import com.bookstore.service.BookService;

@Service
public class BookServiceImpl implements BookService {

	public static final int BOOK_PER_PAGE = 2;

	@Autowired
	private BookRepository bookRepository;

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
	public Page<Book> listBookByPageAndKeyword(int pageNum, String sortDir, String keyword, String sortField) {

		Sort sort = Sort.by(sortField);
		if (sortDir.equals("asc")) {
			sort = sort.ascending();
		} else if (sortDir.equals("desc")) {
			sort = sort.descending();
		}

		Pageable pageable = PageRequest.of(pageNum - 1, BOOK_PER_PAGE, sort);
		Page<Book> bookPage = null;
		if (keyword != null && !keyword.isEmpty()) {
			bookPage = bookRepository.findAllByTitleContaining(keyword, pageable);
		} else {
			bookPage = bookRepository.findAll(pageable);
		}

		return bookPage;
	}

}
