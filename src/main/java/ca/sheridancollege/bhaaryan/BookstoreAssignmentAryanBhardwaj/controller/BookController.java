package ca.sheridancollege.bhaaryan.BookstoreAssignmentAryanBhardwaj.controller;

import java.util.ArrayList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ca.sheridancollege.bhaaryan.BookstoreAssignmentAryanBhardwaj.beans.Book;
import ca.sheridancollege.bhaaryan.BookstoreAssignmentAryanBhardwaj.beans.User;
import ca.sheridancollege.bhaaryan.BookstoreAssignmentAryanBhardwaj.database.DatabaseAccess;
import jakarta.servlet.http.HttpSession;

@Controller
public class BookController {

	@Autowired
	private DatabaseAccess da;

	@Autowired
	@Lazy
	private BCryptPasswordEncoder passwordEncoder;

	@GetMapping("/")
	public String index() {
		return "index";
	}

	@GetMapping("/secure")
	public String secureIndex(Model model) {
		return "/secure/index";
	}

	@GetMapping("/register")
	public String getRegister() {
		return "register";
	}

	@PostMapping("/register")
	public String postRegister(@RequestParam String username, @RequestParam String email,
			@RequestParam String password) {
		String encodedPassword = passwordEncoder.encode(password);
		da.addUser(username, email, encodedPassword);
		return "index";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/permission-denied")
	public String permissionDenied() {
		return "/error/permission-denied";
	}

	@GetMapping("/secure/booksList")
	public String booksList(Model model, HttpSession session) {
		model.addAttribute("book", new Book());
		model.addAttribute("bookList", da.getBookList());
		session.setAttribute("sessionId", session.getId());
		return "/secure/booksList";
	}

	@GetMapping("/secure/viewDetails/{id}")
	public String viewDetails(Model model, @PathVariable Long id) {
		Book book = da.getBookListById(id).get(0);
		model.addAttribute("book", book);
		model.addAttribute("bookList", da.getBookListById(id));
		return "/secure/viewDetails";
	}

	@GetMapping("/secure/addToCart/{id}")
	public String addToCart(Model model, @PathVariable Long id, HttpSession session) {
		// Retrieve the book by id
		Book book = da.getBookListById(id).get(0);
		// Get the current cart from the session or create a new one
		List<Book> cart = (List<Book>) session.getAttribute("cart");
		if (cart == null) {
			cart = new ArrayList<>();
			session.setAttribute("cart", cart);
		}

		// Check if the book is already in the cart
		boolean bookExistsInCart = false;

		for (Book cartBook : cart) {
			if (cartBook.getId().equals(id)) {
				// If the book is already in the cart, increase its quantity
				cartBook.setQuantity(cartBook.getQuantity() + 1);
				bookExistsInCart = true;
				break;
			}
		}

		if (!bookExistsInCart) {
			// If the book is not in the cart, set its quantity to 1 and add it to the cart
			book.setQuantity(1);
			cart.add(book);
		}

		// Update the session with the updated cart
		session.setAttribute("cart", cart);
		// Add the cart and book to the model
		model.addAttribute("cart", cart);
		model.addAttribute("book", book);
		model.addAttribute("bookList", da.getBookListById(id));
		return "/secure/bookAdded";
	}

	@PostMapping("/secure/viewCart/updateQuantity/{id}")
	public String updateQuantity(@PathVariable Long id, @RequestParam Integer quantity, HttpSession session) {
		// Retrieve the cart from the session
		List<Book> cart = (List<Book>) session.getAttribute("cart");

		if (cart != null) {
			for (Book book : cart) {
				if (book.getId().equals(id)) {
					// Update the quantity of the specified book
					book.setQuantity(quantity);
					break;
				}
			}

			// Update the session with the updated cart
			session.setAttribute("cart", cart);
		}

		// Redirect back to the cart view
		return "redirect:/secure/viewCart";
	}

	@PostMapping("/secure/viewCart/placeOrder")
	public String placeOrder(HttpSession session, @RequestParam String username, Model model) {
		List<Book> cart = (List<Book>) session.getAttribute("cart");
		String un = username; // Get the of the logged-in user

		if (cart != null && !cart.isEmpty()) {
			da.placeOrder(cart, un); // Call the method to insert order details into the database
			session.setAttribute("cart", null); // Clear the cart after placing the order
		}
		model.addAttribute("book", new Book());
		model.addAttribute("bookList", da.getBookList());
		return "/secure/booksList"; // Redirect to the books list or another appropriate page
	}

	@GetMapping("/secure/viewCart")
	public String viewCart(Model model, HttpSession session) {
		// Retrieve the cart (List<Book>) from the session
		List<Book> cart = (List<Book>) session.getAttribute("cart");
		// Display the books in the cart
		model.addAttribute("cart", cart);
		return "/secure/viewCart";
	}

	@GetMapping("/retest")
	public String invalidate(Model model, HttpSession session) {
		session.invalidate();
		return "booksList";
	}

	@GetMapping("/secure/booksList/deleteBookById/{id}")
	public String deleteBookById(Model model, @PathVariable Long id) {
		Book book = da.getBookListById(id).get(0);
		da.deleteBookById(id);
		model.addAttribute("book", book);
		model.addAttribute("bookList", da.getBookList());
		return "/secure/booksList";
	}

	@GetMapping("/secure/booksList/editBookById/{id}")
	public String editBookById(Model model, @PathVariable Long id) {
		Book book = da.getBookListById(id).get(0);
		da.updateBook(book);
		model.addAttribute("book", book);
		model.addAttribute("bookList", da.getBookList());
		return "/secure/booksList";
	}

	@PostMapping("/secure/booksList/insertBook")
	public String insertStudent(Model model, @ModelAttribute Book book) {
		List<Book> existingBooks = da.getBookListById(book.getId());
		if (existingBooks.isEmpty()) {
			// If the book doesn't exist (based on TITLE), insert a new book data
			da.insertBook(book);
		} else {
			// If the book exists, update the existing book data
			da.updateBook(book);
		}
		model.addAttribute("bookList", da.getBookList());
		model.addAttribute("book", new Book());
		return "/secure/booksList";
	}

	@GetMapping("/secure/booksList/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "/login";
	}

}
