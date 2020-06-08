package com.company.data;

import java.util.ArrayList;

public class User {

    private final long userId;
    private String userName;
    private String userPass;
    private String userEmail;
    private Boolean isValidated;
    private Boolean isRegistered;
    private int validationAttempts;
    private VerificationEmail verificationEmail;

    private static ArrayList<User> users = new ArrayList<>();

    public User(long userId, String userName, String userPass, String userEmail) {
        this.userId = userId;
        this.userName = userName;
        this.userPass = userPass;
        this.userEmail = userEmail;
        this.isValidated = false;
        this.isRegistered = false;
        this.validationAttempts = 1;
        this.verificationEmail = new VerificationEmail(userName, userEmail);
        users.add(this);
        System.out.println(User.getUsers());
    }

    public long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public static ArrayList getUsers() {
        return users;
    }

    public static void setUsers(ArrayList<User> users) {
        User.users = users;
    }

    public Boolean getValidated() {
        return isValidated;
    }

    public void setValidated(Boolean validated) {
        this.isValidated = validated;
    }

    public Boolean getRegistered() {
        return isRegistered;
    }

    public void setRegistered(Boolean registered) {
        isRegistered = registered;
    }

    public int getValidationAttempts() {
        return validationAttempts;
    }

    public void setValidationAttempts(int validationAttempts) {
        this.validationAttempts = validationAttempts;
    }

    public VerificationEmail getVerificationEmail() {
        return verificationEmail;
    }

    public void setVerificationEmail() {
        this.verificationEmail = new VerificationEmail(userName, userEmail);
    }

    @Override
    public String toString() {
        return "\n" +
                "UserId: " + userId + "; " +
                "Username: " + userName + "; " +
                "Password: " + userPass + "; " +
                "Email: " + userEmail + "; " +
                "Validated: " + getValidated() + "; " +
                "Validation attempts: " + getValidationAttempts() + "; " +
                "Validation email expiry: " + getVerificationEmail().getExpiryDate();
    }
}
