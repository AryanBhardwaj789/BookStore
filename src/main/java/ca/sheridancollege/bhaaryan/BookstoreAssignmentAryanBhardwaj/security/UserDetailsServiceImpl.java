package ca.sheridancollege.bhaaryan.BookstoreAssignmentAryanBhardwaj.security;

import java.util.ArrayList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import ca.sheridancollege.bhaaryan.BookstoreAssignmentAryanBhardwaj.database.DatabaseAccess;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	@Lazy
	private DatabaseAccess da;
	
	@Autowired
	@Lazy
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// Find the user based on the username (read email)
		 ca.sheridancollege.bhaaryan.BookstoreAssignmentAryanBhardwaj.beans.User user = da.findUserByUsername(username);
		// If the user doesn't exist, throw an exception
		if (user == null) {
			System.out.println("User not found:" + username);
			throw new UsernameNotFoundException("User " + username + " was not found in the database");
		}
		
		// Encode the password
	    String encodedPassword = passwordEncoder.encode(user.getPassword());
		
        // Create a user based on the information above
        UserDetails userDetails = User.withUsername(user.getUsername())
                .password(encodedPassword)
                .roles("USER") // Assuming a role for the user, modify as needed
                .build();
        return userDetails;
    }
}
