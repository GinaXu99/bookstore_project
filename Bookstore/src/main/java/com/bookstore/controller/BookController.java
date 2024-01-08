package com.bookstore.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.bookstore.model.Book;
import com.bookstore.service.BookService;
import com.bookstore.service.CartItemService;

@Controller
public class BookController {

	@Autowired
	private BookService bookService;

	@Autowired
	private CartItemService cartItemService;

	@GetMapping("/showFirstPage")
	public String listFirstPage(Model model) {
		return listBookByPageandKeyword(1, null, null, null, model);
	}

	@GetMapping("/bookShelf/page/{pageNumber}")
	public String listBookByPageandKeyword(@PathVariable Integer pageNumber, @Param(value = "sortDir") String sortDir,
			@Param(value = "sortField") String sortField, @Param(value = "keyword") String keyword, Model model) {
		Page<Book> bookPages = null;
		List<Book> bookList = null;

		if (sortDir == null || sortDir.isEmpty()) {
			sortDir = "asc";
		}
		if (sortField == null || sortField.isEmpty()) {
			sortField = "id";
		}

		bookPages = bookService.listBookByPageAndKeyword(pageNumber, sortDir, keyword, sortField);

		long totalItems = bookPages.getTotalElements();
		long totalPages = bookPages.getTotalPages();
		bookList = bookPages.getContent();

		model.addAttribute("totalItems", totalItems);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("sortField", sortField);
		model.addAttribute("pageNumber", pageNumber);
		model.addAttribute("currentPage", pageNumber);
		model.addAttribute("bookList", bookList);
		model.addAttribute("keyword", keyword);

		return "bookShelf";
	}

	@GetMapping("/bookDetail/{id}")
	public String bookDetail(@PathVariable("id") Long id, Model model) {

		cartItemService.emptyCartItemsByBookId(id);

		Book book = bookService.findById(id);
		model.addAttribute("book", book);

		List<Integer> qtyList = new ArrayList<>();
		int stockNum = book.getInStockNumber();

		if (stockNum <= 10) {
			for (int i = 1; i <= stockNum; i++) {
				qtyList.add(i);
			}
		} else {
			qtyList.addAll(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
		}

		model.addAttribute("qtyList", qtyList);
		model.addAttribute("qty", 1);

		return "bookDetail";
	}

}
