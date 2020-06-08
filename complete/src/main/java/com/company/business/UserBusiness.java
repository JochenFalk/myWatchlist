package com.company.business;

import com.company.data.User;
import net.minidev.json.JSONObject;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class UserBusiness {

    private static long userCount;
    private static final int STRENGTH = 16;

    public static String createUser(String userName, String userPass, String userEmail) {

        UserFieldValidator userFieldValidator = new UserFieldValidator();
        iUserCreationEventListener userCreationEventListener = new newUserCreationValidator();
        userFieldValidator.registerUserCreationEventListener(userCreationEventListener);

        String validationMessage = userFieldValidator.setUserFieldValidation(userName, userPass, userEmail);

        if (validationMessage.equals("true")) {
            userCount++;
            BCryptPasswordEncoder userPassEncoder = new BCryptPasswordEncoder(STRENGTH);
            String userPassCrypted = userPassEncoder.encode(userPass);
            long userId = userCount;
            User user = new User(userId, userName, userPassCrypted, userEmail);
            EmailBusiness.sendConfirmationEmail(user);
        }
        return validationMessage;
    }

    public static Boolean verifyRegistration(String token) {
        ArrayList<User> users = User.getUsers();
        Date now = Calendar.getInstance().getTime();
        for (User thisUser : users) {
            boolean verified =
                    token.equals(thisUser.getVerificationEmail().getUserToken()) &&
                            now.before(thisUser.getVerificationEmail().getExpiryDate());
            if (verified) {
                thisUser.setValidated(true);
                return true;
            }
        }
        return false;
    }

    public static Boolean requestLink(String userEmail) {
        if (isRegisteredUser(userEmail)) {
            User user = getUserByEmail(userEmail);
            if (user != null) {
                EmailBusiness.resendConfirmationEmail(user);
                return true;
            }
        }
        return false;
    }

    public static User getUserById(String id) {
        ArrayList<User> users = User.getUsers();
        int parsedId = Integer.parseInt(id);
        for (User thisUser : users) {
            if (thisUser.getUserId() == parsedId) {
                return thisUser;
            }
        }
        return null;
    }

    public static User getUserByEmail(String userEmail) {
        ArrayList<User> users = User.getUsers();
        for (User thisUser : users) {
            if (thisUser.getUserEmail().equals(userEmail)) {
                return thisUser;
            }
        }
        return null;
    }

    public static Boolean isVerifiedUser(String userName, String userPass) {
        ArrayList<User> users = User.getUsers();
        BCryptPasswordEncoder userPassEncoder = new BCryptPasswordEncoder(STRENGTH);
        for (User thisUser : users) {
            boolean validated =
                    userName.equals(thisUser.getUserName()) &&
                            userPassEncoder.matches(userPass, thisUser.getUserPass()) &&
                            thisUser.getValidated();
            if (validated) {
                thisUser.setRegistered(true);
                return true;
            }
        }
        return false;
    }

    public static String isRegisteredUser(String userName, String userPass) {
        ArrayList<User> users = User.getUsers();
        BCryptPasswordEncoder userPassEncoder = new BCryptPasswordEncoder(STRENGTH);
        for (User thisUser : users) {
            boolean exists =
                    userName.equals(thisUser.getUserName()) &&
                            userPassEncoder.matches(userPass, thisUser.getUserPass());
            if (exists) {
                JSONObject msg = new JSONObject();
                if (!thisUser.getValidated()) {
                    msg.put("msg", "Your account has not been verified. Please follow the link in the provided email.");
                    return msg.toString();
                } else if (!thisUser.getRegistered()) {
                    msg.put("msg", "The registration of your account has not been completed. Please follow the link in the provided email.");
                    return msg.toString();
                } else {
                    boolean registered = thisUser.getValidated() && thisUser.getRegistered();
                    if (registered) {
                        return "true";
                    }
                }
            }
        }
        return "false";
    }

    public static Boolean isRegisteredUser(String userEmail) {
        ArrayList<User> users = User.getUsers();
        for (User thisUser : users) {
            if (thisUser.getUserEmail().equals(userEmail)) {
                return true;
            }
        }
        return false;
    }
}