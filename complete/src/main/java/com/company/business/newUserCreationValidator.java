package com.company.business;

import com.company.data.User;
import net.minidev.json.JSONObject;
import org.apache.commons.validator.routines.EmailValidator;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class newUserCreationValidator implements iUserCreationEventListener {

    private static final String USERNAME_REGEX = "^[aA-zZ]\\w{5,11}$";
    private static final String USERPASS_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{5,11}$";

    @Override
    public String validate(String userNameValidated, String userPassValidated, String userEmailValidated) {
        if (isExistingUserName(userNameValidated)) {
            JSONObject msg = new JSONObject();
            msg.put("msg", "The username already exists");
            return msg.toString();
        } else if (!validateUserName(userNameValidated)) {
            JSONObject msg = new JSONObject();
            msg.put("msg", "The provided username is not valid. Please check the tooltip for requirements");
            return msg.toString();
        } else if (!validateUserPass(userPassValidated)) {
            JSONObject msg = new JSONObject();
            msg.put("msg", "The provided password is not valid. Please check the tooltip for requirements");
            return msg.toString();
        } else if (isExistingUserEmail(userEmailValidated)) {
            JSONObject msg = new JSONObject();
            msg.put("msg", "The provided email is already registered with another user");
            return msg.toString();
        } else if (!validateUserEmail(userEmailValidated)) {
            JSONObject msg = new JSONObject();
            msg.put("msg", "The provided email is not valid. Please check the tooltip for requirements");
            return msg.toString();
        }else {
            return "true";
        }
    }

    public static Boolean isExistingUserName(String userNameValidated) {
        ArrayList<User> users = User.getUsers();
        for (User thisUser : users) {
            if (userNameValidated.equals(thisUser.getUserName())) {
                return true;
            }
        }
        return false;
    }

    public static Boolean validateUserName(String userNameValidated) {
        Pattern pattern = Pattern.compile(USERNAME_REGEX);
        Matcher matcher = pattern.matcher(userNameValidated);
        return  matcher.matches();
    }

    public static Boolean validateUserPass(String userPassValidated) {
        Pattern pattern = Pattern.compile(USERPASS_REGEX);
        Matcher matcher = pattern.matcher(userPassValidated);
        return  matcher.matches();
    }

    public static Boolean isExistingUserEmail(String userEmailValidated) {
        ArrayList<User> users = User.getUsers();
        for (User thisUser : users) {
            if (userEmailValidated.equals(thisUser.getUserEmail())) {
                return true;
            }
        }
        return false;
    }

    public static Boolean validateUserEmail(String userEmailValidated) {
        EmailValidator validator = EmailValidator.getInstance();
        if (!validator.isValid(userEmailValidated)) {
            System.out.println();
            return false;
        }
        return true;
    }
}
