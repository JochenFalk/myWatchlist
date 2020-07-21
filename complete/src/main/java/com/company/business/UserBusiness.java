package com.company.business;

import com.company.data.UserQueries;
import com.company.data.model.User;
import net.minidev.json.JSONObject;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.http.HttpSession;
import java.time.Instant;
import java.util.ArrayList;

public class UserBusiness {

    private static final int COST = 16;
    private static final int SESSION_TIME = 15 * 60;

    public static Boolean getLoginStatus(HttpSession session) {
        User user = UserBusiness.getUserFromSession(session);
        return user != null;
    }

    public static JSONObject getRole(HttpSession session) {
        if (session.getAttribute("username") != null && session.getAttribute("role") != null) {
            String userName = session.getAttribute("username").toString();
            String userRole = session.getAttribute("role").toString();
            User user = UserQueries.getUserByName(userName);
            if (user != null) {
                if (user.getRole().equals(userRole)) {
                    JSONObject role = new JSONObject();
                    role.put("role", userRole);
                    return role;
                }
            } else {
                return null;
            }
        }
        return null;
    }

    public static Boolean logOut(HttpSession session) {
        User user = UserBusiness.getUserFromSession(session);
        if (user != null) {
            session.invalidate();
            System.out.println("Log out by: " + user.getName());
            return true;
        } else {
            return false;
        }
    }

    public static Boolean deleteAccount(HttpSession session) {
        User user = UserBusiness.getUserFromSession(session);
        if (user != null) {
            System.out.println("Account deleted: " + session.getAttribute("username"));
            session.invalidate();
            UserQueries.deleteUserById(user.getId());
            return true;
        } else {
            return false;
        }
    }

    public static String createUser(String userName, String userPass, String userEmail) {

        UserFieldValidator userFieldValidator = new UserFieldValidator();
        IUserCreationEventListener userCreationEventListener = new NewUserCreationValidator();
        userFieldValidator.registerUserCreationEventListener(userCreationEventListener);

        String validationMessage = userFieldValidator.setUserFieldValidation(userName, userPass, userEmail);

        if (validationMessage.equals("true")) {
            String userPassCrypted = encryptPassword(userPass);
            User user = new User(userName, userPassCrypted, userEmail);
            UserQueries.insertUser(user);
            EmailBusiness.sendConfirmationEmail(user);
        }
        return validationMessage;
    }

    public static Boolean verifyRegistration(String token) {
        ArrayList<User> users = UserQueries.getAllUsers();
        Instant now = Instant.now();
        for (User thisUser : users) {
            boolean verified =
                    token.equals(thisUser.getToken()) &&
                            now.isBefore(thisUser.getTokenExpiryDate());
            if (verified) {
                thisUser.setValidated(true);
                UserQueries.updateUser(thisUser);
                return true;
            }
        }
        return false;
    }

    public static Boolean requestLink(String emailAddress) {
        if (login(emailAddress)) {
            User user = getUserByEmail(emailAddress);
            if (user != null) {
                EmailBusiness.resendConfirmationEmail(user);
                return true;
            }
        }
        return false;
    }

    public static User getUserByEmail(String emailAddress) {
        ArrayList<User> users = UserQueries.getAllUsers();
        for (User thisUser : users) {
            if (thisUser.getEmailAddress().equals(emailAddress)) {
                return thisUser;
            }
        }
        return null;
    }

    public static Boolean isVerifiedUser(String userName, String userPass, HttpSession session) {
        ArrayList<User> users = UserQueries.getAllUsers();
        BCryptPasswordEncoder userPassEncoder = new BCryptPasswordEncoder(COST);
        for (User thisUser : users) {
            boolean exists =
                    userName.equals(thisUser.getName()) &&
                            userPassEncoder.matches(userPass, thisUser.getPassword()) &&
                            thisUser.getValidated();
            if (exists) {
                thisUser.setRegistered(true);
                UserQueries.updateUser(thisUser);
                session.setAttribute("username", thisUser.getName());
                session.setAttribute("role", thisUser.getRole());
                session.setMaxInactiveInterval(SESSION_TIME);
                System.out.println("Login by: " + session.getAttribute("username"));
                return true;
            }
        }
        return false;
    }

    public static String login(String userName, String userPass, HttpSession session) {
        ArrayList<User> users = UserQueries.getAllUsers();
        BCryptPasswordEncoder userPassEncoder = new BCryptPasswordEncoder(COST);
        for (User thisUser : users) {
            boolean exists =
                    userName.equals(thisUser.getName()) &&
                            userPassEncoder.matches(userPass, thisUser.getPassword());
            if (exists) {

                net.minidev.json.JSONObject msg = new net.minidev.json.JSONObject();

                if (!thisUser.getValidated()) {
                    msg.put("msg", "Your account has not been verified. Please follow the link in the provided email.");
                    return msg.toString();
                } else if (!thisUser.getRegistered()) {
                    msg.put("msg", "The registration of your account has not been completed. Please follow the link in the provided email.");
                    return msg.toString();
                } else {
                    boolean registered = thisUser.getValidated() && thisUser.getRegistered();
                    if (registered) {
                        session.setAttribute("username", thisUser.getName());
                        session.setAttribute("role", thisUser.getRole());
                        session.setMaxInactiveInterval(SESSION_TIME);
                        System.out.println("Login by: " + session.getAttribute("username"));
                        return "true";
                    }
                }
            }
        }
        return "false";
    }

    public static User getUserFromSession(HttpSession session) {
        if (session.getAttribute("username") != null) {
            String userName = session.getAttribute("username").toString();
            return UserQueries.getUserByName(userName);
        } else {
            return null;
        }

    }

    public static Boolean login(String userEmail) {
        ArrayList<User> users = UserQueries.getAllUsers();
        for (User thisUser : users) {
            if (thisUser.getEmailAddress().equals(userEmail)) {
                return true;
            }
        }
        return false;
    }

    public static String encryptPassword(String password) {
        BCryptPasswordEncoder userPassEncoder = new BCryptPasswordEncoder(COST);
        return userPassEncoder.encode(password);
    }
}