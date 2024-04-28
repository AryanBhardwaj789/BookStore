package ca.sheridancollege.bhaaryan.BookstoreAssignmentAryanBhardwaj.beans;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Book {

	private Long id;
	private String title;
	private String author;
	private Long isbn;
	private String price;
	private String description;
	private int quantity;

}
