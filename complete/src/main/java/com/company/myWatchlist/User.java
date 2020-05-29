package com.company.myWatchlist;

import java.util.ArrayList;

public class User {

	private final long id;
	private final String firstName;
	private final String lastName;
	private Boolean isValidated;

//	private ArrayList<User> users = new ArrayList<>();

	public User(long id, String firstName, String lastName) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.isValidated = false;
//		users.add(this);
	}

	public long getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

//	public ArrayList getUsers() {
//		return users;
//	}
//
//	public void setUsers(ArrayList users) {
//		this.users = users;
//	}

	public Boolean getValidated() {
		return isValidated;
	}

	public void setValidated(Boolean validated) {
		this.isValidated = validated;
	}

	public static Boolean validateUser(String firstName, String lastName) {

//		long userId;
		for (User thisUser : UserController.users) {
			boolean validated =
				lastName.equals(thisUser.getLastName()) ||
				firstName.equals(thisUser.getFirstName());


			String firstName1 = thisUser.getFirstName();
			boolean	b = firstName.compareTo(firstName1)  == 0;
			if (firstName.compareTo(firstName1)  == 0) {
				System.out.println(thisUser.getFirstName());
//				userId = thisUser.id;
				if (lastName.equals(thisUser.getLastName())) {
					thisUser.setValidated(true);
					return thisUser.getValidated();
				}
			}
		}
		return null;
	}
}
