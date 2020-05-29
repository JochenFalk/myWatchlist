package com.company.myWatchlist;

import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class UserController {

	private String firstName;
	private String lastName;
	private static  AtomicLong counter;

	public static final User[] users = {
			new User(1, "Jochen", "Falk"),
	};

	@GetMapping("/validateUser")
	public Boolean validateUser(
			@RequestParam(value = "firstName", required = true) String firstName,
			@RequestParam(value = "lastName", required = true) String lastName)
	{
		return User.validateUser(firstName, lastName);
	}

	@GetMapping("/user")
	public User user(@RequestParam(value = "id", defaultValue = "") String id) {
		int parsedId = Integer.parseInt(id);
		for (User user : users){
			if (user.getId() == parsedId){
				return user;
			}
		}
		return null;
	}
}
