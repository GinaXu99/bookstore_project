package com.bookstore.serviceImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.model.Book;
import com.bookstore.model.BookToCartItem;
import com.bookstore.model.CartItem;
import com.bookstore.model.Order;
import com.bookstore.model.ShoppingCart;
import com.bookstore.model.User;
import com.bookstore.repository.BookToCartItemRepository;
import com.bookstore.repository.CartItemRepository;
import com.bookstore.service.CartItemService;

@Service
public class CartItemServiceImpl implements CartItemService {

	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private BookToCartItemRepository bookToCartItemRepository;

	@Override
	public List<CartItem> findByShoppingCart(ShoppingCart shoppingCart) {
		return cartItemRepository.findByShoppingCart(shoppingCart);
	}

	@Override
	public CartItem updateCartItem(CartItem cartItem) {
		List<BookToCartItem> BookToCartItemLists = cartItem.getBookToCartItemList();

		BigDecimal subTotalAmount = null;

		for (BookToCartItem item : BookToCartItemLists) {
			subTotalAmount = new BigDecimal(item.getBook().getOurPrice()).multiply(new BigDecimal(cartItem.getQty()));
		}

		subTotalAmount = subTotalAmount.setScale(2, RoundingMode.HALF_UP);
		cartItem.setSubtotal(subTotalAmount);
		cartItemRepository.save(cartItem);
		return cartItem;
	}

	@Override
	public void addBookToCartItem(Book book, User user, int qty) {

		List<CartItem> cartItemList = findByShoppingCart(user.getShoppingCart());
		for (CartItem cartItem : cartItemList) {
			if (book.getId() == cartItem.getBook().getId()) {
				cartItem.setQty(cartItem.getQty() + qty);
				cartItem.setSubtotal(new BigDecimal(book.getOurPrice()).multiply(new BigDecimal(qty)));
				cartItemRepository.save(cartItem);
				return;
			}
		}

		CartItem cartItem = new CartItem();
		cartItem.setShoppingCart(user.getShoppingCart());
		cartItem.setBook(book);

		cartItem.setQty(qty);
		cartItem.setSubtotal(new BigDecimal(book.getOurPrice()).multiply(new BigDecimal(qty)));
		cartItem = cartItemRepository.save(cartItem);

		BookToCartItem bookToCartItem = new BookToCartItem();
		bookToCartItem.setBook(book);
		bookToCartItem.setCartItem(cartItem);
		bookToCartItemRepository.save(bookToCartItem);

	}

	@Override
	public void removeCartItem(Long id) {
		bookToCartItemRepository.deleteCartItemById(id);
		cartItemRepository.deleteById(id);
	}

	@Override
	public CartItem findById(Long id) {
		return cartItemRepository.findById(id).get();
	}

	@Override
	public void save(CartItem cartItem) {
		cartItemRepository.save(cartItem);

	}

	@Override
	public List<CartItem> findByOrder(Order order) {
		return cartItemRepository.findByOrder(order);
	}

	@Override
	public void emptyCartItemsByBookId(Long bookid) {
		List<CartItem> cartItems = cartItemRepository.findByBookId(bookid);
		bookToCartItemRepository.deleteByBookId(bookid);

		for (CartItem item : cartItems) {
			cartItemRepository.deleteById(item.getId());
		}
	}
}
