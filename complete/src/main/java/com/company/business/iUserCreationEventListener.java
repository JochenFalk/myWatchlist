package com.company.business;

public interface iUserCreationEventListener {
    String validate(String userName, String userPass, String userEmail);
}
