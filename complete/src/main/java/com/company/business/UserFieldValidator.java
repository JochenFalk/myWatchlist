package com.company.business;

import com.github.ankurpathak.password.bean.constraints.ContainDigit;
import com.github.ankurpathak.password.bean.constraints.NotContainWhitespace;
import com.github.ankurpathak.password.bean.constraints.PasswordMatches;
import com.github.ankurpathak.username.bean.constraints.*;

import javax.validation.constraints.NotBlank;

@PasswordMatches
public class UserFieldValidator {

    @UsernamePattern
    @NotContainConsecutiveUnderscore
    @NotContainConsecutivePeriod
    @NotContainPeriodFollowedByUnderscore
    @NotContainUnderscoreFollowedByPeriod
    @NotContainWhitespace
    @NotBlank
    private static String userNameValidated;

    @NotContainWhitespace
    @ContainDigit
    @NotBlank
    private static String userPassValidated;
    private static String userEmailValidated;

    private iUserCreationEventListener userCreationEventListener;

    public void registerUserCreationEventListener(iUserCreationEventListener userCreationEventListener)
    {
        this.userCreationEventListener = userCreationEventListener;
    }

    public String setUserFieldValidation(String userName, String userPass, String userEmail) {
        userNameValidated = userName;
        userPassValidated = userPass;
        userEmailValidated = userEmail;
        if (this.userCreationEventListener != null) {
            return userCreationEventListener.validate(userNameValidated, userPassValidated, userEmailValidated);
        }
        return null;
    }
}
