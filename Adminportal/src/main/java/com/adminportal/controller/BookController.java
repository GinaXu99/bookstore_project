package com.adminportal.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.adminportal.model.Book;
import com.adminportal.service.BookService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/book")
public class BookController {

	@Autowired
	private BookService bookService;

	@GetMapping("/add")
	public String addBook(Model model) {
		Book book = new Book();
		model.addAttribute("book", book);
		return "addBook";
	}

	@PostMapping("/add")
	public String addBookPost(@ModelAttribute("book") Book book, HttpServletRequest request,
			RedirectAttributes redirectAttribute) {

		bookService.saveBook(book);

		MultipartFile bookImage = book.getBookImage();

		try {
			byte[] bytes = bookImage.getBytes();
			String name = book.getId() + ".png";
			BufferedOutputStream stream = new BufferedOutputStream(
					new FileOutputStream(new File("src/main/resources/static/images/book/" + name)));
			stream.write(bytes);
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		redirectAttribute.addAttribute("message", "Booked added successfully");
		return "redirect:bookList";
	}

	@GetMapping("/bookList")
	public String bookList(Model model) {
		List<Book> bookList = bookService.findAllBooks();
		model.addAttribute("bookList", bookList);
		return "bookList";
	}

	@GetMapping("/bookInfo/{id}")
	public String getBookInfo(@PathVariable("id") Long id, Model model) {
		Book book = bookService.findById(id);
		model.addAttribute("book", book);
		return "bookInfo";
	}

	@GetMapping("/updateBook/{id}")
	public String updateBook(@PathVariable("id") Long id, Model model) {
		Book book = bookService.findById(id);
		model.addAttribute("book", book);
		return "updateBook";
	}

	@PostMapping("/updateBook")
	public String updateBookPost(@ModelAttribute("book") Book book, HttpServletRequest request) {
		bookService.saveBook(book);

		MultipartFile bookImage = book.getBookImage();

		try {
			byte[] bytes = bookImage.getBytes();
			String name = book.getId() + ".png";

			Files.delete(Paths.get("src/main/resources/static/images/book/" + name));

			BufferedOutputStream stream = new BufferedOutputStream(
					new FileOutputStream(new File("src/main/resources/static/images/book/" + name)));
			stream.write(bytes);
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		String bookid = book.getId().toString();
		return "redirect:/book/bookInfo/" + bookid;
	}
	
	@GetMapping("/deleteBook/{id}")
	public String deleteBook(@PathVariable("id") Long id, Model model) {
		bookService.deleteById(id);
		return "redirect:/";
	}
}
