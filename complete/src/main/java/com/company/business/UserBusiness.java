package com.company.business;

import com.company.data.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;

public class UserBusiness {

    private static long userCount;

    public static String createUser(String userName, String userPass, String userEmail) {

        UserFieldValidator userFieldValidator = new UserFieldValidator();
        iUserCreationEventListener userCreationEventListener = new newUserCreationValidator();
        userFieldValidator.registerUserCreationEventListener(userCreationEventListener);

        String validationMessage = userFieldValidator.setUserFieldValidation(userName, userPass, userEmail);

        if (validationMessage.equals("true")) {
            userCount++;
            BCryptPasswordEncoder userPassEncoder = new BCryptPasswordEncoder();
            String userPassCrypted = userPassEncoder.encode(userPass);
            long userId = userCount;
            User user = new User(userId, userName, userPassCrypted, userEmail);
            EmailBusiness.sendConfirmationEmail(user);

            System.out.println(User.getUsers());
        }
        return validationMessage;
    }

    public static User getUser(String id) {
        ArrayList<User> users = User.getUsers();
        int usersSize = users.size();
        int parsedId = Integer.parseInt(id);
        for (int i = 0; i < usersSize; i++) {
            if (users.get(i).getUserId() == parsedId) {
                return users.get(i);
            }
        }
        return null;
    }

    public static Boolean isExistingUser(String userName, String userPass) {
        ArrayList<User> users = User.getUsers();
        BCryptPasswordEncoder userPassEncoder = new BCryptPasswordEncoder();
        for (User thisUser : users) {
            boolean exists =
                    userName.equals(thisUser.getUserName()) &&
                            userPassEncoder.matches(userPass, thisUser.getUserPass());
            return exists;
        }
        return false;
    }
}