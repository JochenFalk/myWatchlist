package com.company.data.model;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;

public class User {

    private long id;
    private String role;
    private String name;
    private String password;
    private String emailAddress;
    private Boolean isValidated;
    private Boolean isRegistered;
    private int validationAttempts;
    private String token;
    private Instant tokenExpiryDate;

    private static ArrayList<User> users = new ArrayList<>();

    private static final int TOKEN_EXPIRY_TIME = ApplicationConfig.getTokenExpiryTime();
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    public User(String name, String password, String emailAddress) {
        setToken();
        setTokenExpiryDate();
        this.name = name;
        this.password = password;
        this.emailAddress = emailAddress;
        this.isValidated = false;
        this.isRegistered = false;
        this.validationAttempts = 0;
        this.token = getToken();
        this.tokenExpiryDate = getTokenExpiryDate();
        System.out.println(this);
    }

    public User(String name, String password, String emailAddress, Boolean isValidated, Boolean isRegistered, int validationAttempts) {
        setToken();
        setTokenExpiryDate();
        this.name = name;
        this.password = password;
        this.emailAddress = emailAddress;
        this.isValidated = isValidated;
        this.isRegistered = isRegistered;
        this.validationAttempts = validationAttempts;
        this.token = getToken();
        this.tokenExpiryDate = getTokenExpiryDate();
    }

    public User(long id, String role, String name, String password, String emailAddress, Boolean isValidated, Boolean isRegistered, int validationAttempts, String token, Instant tokenExpiryDate) {
        this.id = id;
        this.role = role;
        this.name = name;
        this.password = password;
        this.emailAddress = emailAddress;
        this.isValidated = isValidated;
        this.isRegistered = isRegistered;
        this.validationAttempts = validationAttempts;
        this.token = token;
        this.tokenExpiryDate = tokenExpiryDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public static ArrayList<User> getUsers() {
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

    public String getToken() {
        return token;
    }

    public void setToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        this.token = base64Encoder.encodeToString(randomBytes);
    }

    public Instant getTokenExpiryDate() {
        return tokenExpiryDate;
    }

    public void setTokenExpiryDate() {
        this.tokenExpiryDate = Instant.now().plusSeconds(TOKEN_EXPIRY_TIME);
    }

    @Override
    public String toString() {
        return "\n" +
                "UserId: " + id + "; " +
                "Username: " + name + "; " +
                "Role: " + role + "; " +
                "Password: " + password + "; " +
                "Email: " + emailAddress + "; " +
                "Validated: " + getValidated() + "; " +
                "Registered: " + getRegistered() + "; " +
                "Validation attempts: " + getValidationAttempts() + "; " +
                "Token: " + getToken() + "; " +
                "Token expiry date: " + getTokenExpiryDate();
    }
}
