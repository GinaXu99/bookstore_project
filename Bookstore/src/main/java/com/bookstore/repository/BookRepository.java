package com.bookstore.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bookstore.model.Book;

public interface BookRepository extends JpaRepository<Book, Long>{
	Page<Book> findAllByTitleContaining(String keyword, Pageable pageable);
	Page<Book> findAll(Pageable pageable);
}
