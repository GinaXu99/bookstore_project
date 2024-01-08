package com.adminportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adminportal.model.Book;

public interface BookRepository extends JpaRepository<Book, Long>{

}
