package ca.sheridancollege.bhaaryan.BookstoreAssignmentAryanBhardwaj.beans;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class User {

	@NonNull
	private String username;
	@NonNull
	private String password;
	@NonNull
	private String email;

}
