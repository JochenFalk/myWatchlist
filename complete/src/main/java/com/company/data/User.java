package com.company.data;

import java.util.ArrayList;

public class User {

    private final long id;
    private String name;
    private String password;
    private String email;
    private Boolean isValidated;
    private Boolean isRegistered;
    private int validationAttempts;
    private VerificationEmail verificationEmail;

    private static ArrayList<User> users = new ArrayList<>();

    public User(long id, String name, String password, String email) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.isValidated = false;
        this.isRegistered = false;
        this.validationAttempts = 1;
        this.verificationEmail = new VerificationEmail(name, email);
        users.add(this);
        System.out.println(User.getUsers());
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        this.verificationEmail = new VerificationEmail(name, email);
    }

    @Override
    public String toString() {
        return "\n" +
                "UserId: " + id + "; " +
                "Username: " + name + "; " +
                "Password: " + password + "; " +
                "Email: " + email + "; " +
                "Validated: " + getValidated() + "; " +
                "Validation attempts: " + getValidationAttempts() + "; " +
                "Validation email expiry: " + getVerificationEmail().getTokenExpiryDate();
    }
}
