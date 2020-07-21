package com.company.business;

public interface IUserCreationEventListener {
    String validate(String userName, String userPass, String userEmail);
}
