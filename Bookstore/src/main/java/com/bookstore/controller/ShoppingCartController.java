package com.bookstore.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bookstore.model.Book;
import com.bookstore.model.CartItem;
import com.bookstore.model.ShoppingCart;
import com.bookstore.model.User;
import com.bookstore.service.BookService;
import com.bookstore.service.CartItemService;
import com.bookstore.service.ShoppingCartService;
import com.bookstore.service.UserService;

@Controller
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

	@Autowired
	private UserService userService;

	@Autowired
	private CartItemService cartItemService;

	@Autowired
	private ShoppingCartService shoppingCartService;

	@Autowired
	private BookService bookService;

	@GetMapping("/cart")
	public String shoppingCart(Model model, Principal principal) {

		User user = userService.findByUserName(principal.getName());
		ShoppingCart shoppingCart = user.getShoppingCart();
		List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
		shoppingCartService.updateShoppingCart(shoppingCart);

		model.addAttribute("cartItemList", cartItemList);
		model.addAttribute("shoppingCart", shoppingCart);

		return "shoppingCart";

	}

	@PostMapping("/addItem")
	public String addItem(@ModelAttribute("book") Book book, @ModelAttribute("qty") String qty, Model model,
			Principal principal) {

		User user = userService.findByUserName(principal.getName());
		book = bookService.findById(book.getId());

		if (Integer.parseInt(qty) > book.getInStockNumber()) {
			model.addAttribute("notEnoughStock", true);
			return "forward:/bookDetail?id=" + book.getId();
		}

		cartItemService.addBookToCartItem(book, user, Integer.parseInt(qty));
		model.addAttribute("addBookSuccess", true);

		return "redirect:/shoppingCart/cart";
	}

	@PostMapping("/updateCartItem")
	public String updateCartItem(@ModelAttribute("id") Long id, @ModelAttribute("qty") int qty, Model model) {

		CartItem cartItem = cartItemService.findById(id);
		cartItem.setQty(qty);
		cartItemService.updateCartItem(cartItem);

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

		return "redirect:/shoppingCart/cart";
	}

	@GetMapping("/removeItem/{id}")
	public String removeItem(@ModelAttribute("id") Long id) {
		cartItemService.removeCartItem(id);
		return "redirect:/shoppingCart/cart";
	}

}
