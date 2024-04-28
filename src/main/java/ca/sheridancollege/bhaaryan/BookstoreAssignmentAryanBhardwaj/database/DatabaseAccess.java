package ca.sheridancollege.bhaaryan.BookstoreAssignmentAryanBhardwaj.database;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.bhaaryan.BookstoreAssignmentAryanBhardwaj.beans.Book;
import ca.sheridancollege.bhaaryan.BookstoreAssignmentAryanBhardwaj.beans.User;

@Repository
public class DatabaseAccess {
	
	@Autowired
	@Lazy
	protected NamedParameterJdbcTemplate jdbc;
	
	@Autowired
	@Lazy
	private BCryptPasswordEncoder passwordEncoder;
	
	public List<Book> getBookList() {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "SELECT * FROM bookdata";
		return jdbc.query(query, namedParameters, new
		BeanPropertyRowMapper<Book>(Book.class));
		}
	
	public List<Book> getSoftCodedBookList() {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "SELECT * FROM bookdata WHERE id > 3";
		return jdbc.query(query, namedParameters, new
		BeanPropertyRowMapper<Book>(Book.class));
		}
	
	public List<Book> getThiefBookList() {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "SELECT * FROM bookdata WHERE title = 'The Book Thief'";
		return jdbc.query(query, namedParameters, new
		BeanPropertyRowMapper<Book>(Book.class));
		}
	
	public List<Book> getFrankensteinBookList() {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "SELECT * FROM bookdata WHERE title = 'Frankenstein'";
		return jdbc.query(query, namedParameters, new
		BeanPropertyRowMapper<Book>(Book.class));
		}
	
	public List<Book> getHitchhikerBookList() {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "SELECT * FROM bookdata WHERE author = 'Douglas Adams'";
		return jdbc.query(query, namedParameters, new
		BeanPropertyRowMapper<Book>(Book.class));
		}
	
	public List<Book> getBookListByTitle(String title) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "SELECT * FROM bookdata WHERE title = :title";
		namedParameters.addValue("title", title);
		return jdbc.query(query, namedParameters, new
		BeanPropertyRowMapper<Book>(Book.class));
	}
	
	public List<Book> getBookListById(Long id) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "SELECT * FROM bookdata WHERE id = :id";
		namedParameters.addValue("id", id);
		return jdbc.query(query, namedParameters, new
		BeanPropertyRowMapper<Book>(Book.class));
	}
	
	public List<Book> deleteBookById(Long id) {
	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    String query = "DELETE FROM bookdata WHERE id = :id";
	    namedParameters.addValue("id", id);
	    jdbc.update(query, namedParameters);
	    int rowsAffected = jdbc.update(query, namedParameters);
	    if (rowsAffected > 0) {
	        System.out.println("Deleted Book from the database !");
	    }
	    return getBookList();
	}

	public void insertBook(Book book) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("id", book.getId());
		namedParameters.addValue("title", book.getTitle());
		namedParameters.addValue("author", book.getAuthor());
		namedParameters.addValue("isbn", book.getIsbn());
		namedParameters.addValue("price", book.getPrice());
		namedParameters.addValue("description", book.getDescription());
		// Adding a named parameter
		String query = "INSERT INTO bookdata (title, author, isbn, price, description) "
				+ "VALUES (:title, :author, :isbn, :price, :description)";
		// Using the named parameter
		int rowsAffected = jdbc.update(query, namedParameters);
		if (rowsAffected > 0) {
			System.out.println("New Book Data inserted into the Database!");
		}
	}
	
	public void updateBook(Book updatedBook) {
	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("id", updatedBook.getId());
	    namedParameters.addValue("title", updatedBook.getTitle());
	    namedParameters.addValue("author", updatedBook.getAuthor());
	    namedParameters.addValue("isbn", updatedBook.getIsbn());
	    namedParameters.addValue("price", updatedBook.getPrice());
	    namedParameters.addValue("description", updatedBook.getDescription());
	    String query = "UPDATE bookdata SET title = :title, author = :author,"
	    		+ " isbn = :isbn, price = :price, description = :description "
	    		+ "WHERE id = :id";
	    int rowsAffected = jdbc.update(query, namedParameters);
	    if (rowsAffected > 0) {
	        System.out.println("Updated Book in the database !");
	    }
	}
	
	public void insertUser(User user) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("username", user.getUsername());
		namedParameters.addValue("email", user.getEmail());
		namedParameters.addValue("password", user.getPassword());
		// Adding a named parameter
		String query = "INSERT INTO userdata (username, email, password) "
				+ "VALUES (:username, :email, :password)";
		// Using the named parameter
		int rowsAffected = jdbc.update(query, namedParameters);
		if (rowsAffected > 0) {
			System.out.println("New User Data inserted into the Database!");
		}
	}
	
	public List<User> getUserList() {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "SELECT * FROM userdata";
		return jdbc.query(query, namedParameters, new
		BeanPropertyRowMapper<User>(User.class));
		}
	
	public List<User> getUserListByUsername(String username) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "SELECT * FROM userdata where username=:username";
		namedParameters.addValue("username", username);
		return jdbc.query(query, namedParameters, new
		BeanPropertyRowMapper<User>(User.class));
		}
	
	public void placeOrder(List<Book> cart, String username) {
	    // Iterate through the cart and insert order details into the database
	    for (Book book : cart) {
	        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	        namedParameters.addValue("bookTitle", book.getTitle());
	        namedParameters.addValue("username", username);
	        namedParameters.addValue("quantity", book.getQuantity()); // Assuming Book class has a getQuantity() method

	        // Adding named parameters
	        String query = "INSERT INTO orderdata (book_title, username, quantity) "
	                + "VALUES (:bookTitle, :username, :quantity)";

	        // Print the SQL query and parameter values for debugging
	        System.out.println("Query: " + query);
	        System.out.println("Parameters: " + namedParameters.getValues());

	        // Using the named parameters
	        int rowsAffected = jdbc.update(query, namedParameters);

	        if (rowsAffected > 0) {
	            System.out.println("Order Data inserted into the Database!");
	        }
	    }
	}
	
	public void updateBookQuantity(Long id, int quantity) {
	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("id", id);
	    namedParameters.addValue("quantity", quantity);

	    String query = "UPDATE bookdata SET quantity = :quantity WHERE id = :id";

	    int rowsAffected = jdbc.update(query, namedParameters);

	    if (rowsAffected > 0) {
	        System.out.println("Updated Book Quantity in the database!");
	    }
	}
	
	public User findUserByUsername(String username) {
	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    String query = "SELECT * FROM userdata where username = :username";
	    namedParameters.addValue("username", username);
	    try {
	        return jdbc.queryForObject(query, namedParameters, new BeanPropertyRowMapper<User>(User.class));
	    } catch (EmptyResultDataAccessException erdae) {
	        return null;
	    }
	}
	
	// Method to add a user account
	public void addUser(String username, String email, String password) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "INSERT INTO userdata "
		+ "(username, email, password) "
		+ "VALUES (:username, :email, :password)";
		namedParameters.addValue("username", username);
		namedParameters.addValue("email", email);
		namedParameters.addValue("password",
		passwordEncoder.encode(password));
		jdbc.update(query, namedParameters);
		}
	
	
}
