package com.company.presentation;

import com.company.business.UserBusiness;
import com.company.data.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

	@GetMapping("/isRegisteredUser")
	public String isRegisteredUser(
			@RequestParam(value = "userName", required = true) String userName,
			@RequestParam(value = "userPass", required = true) String userPass)
	{
		return UserBusiness.isRegisteredUser(userName, userPass);
	}

	@GetMapping("/isVerifiedUser")
	public Boolean isVerifiedUser(
			@RequestParam(value = "userName", required = true) String userName,
			@RequestParam(value = "userPass", required = true) String userPass)
	{
		return UserBusiness.isVerifiedUser(userName, userPass);
	}

	@GetMapping("/createUser")
	public String createUser(
			@RequestParam(value = "userName", required = true) String userName,
			@RequestParam(value = "userPass", required = true) String userPass,
			@RequestParam(value = "userEmail", required = true) String userEmail) {
		return UserBusiness.createUser(userName, userPass, userEmail);
	}

	@GetMapping("/requestLink")
	public Boolean requestLink(
			@RequestParam(value = "userEmail", required = true) String userEmail) {
		return UserBusiness.requestLink(userEmail);
	}

	@GetMapping("/user")
	public User user(@RequestParam(value = "id", defaultValue = "") String id) {
		return UserBusiness.getUserById(id);
	}
}
