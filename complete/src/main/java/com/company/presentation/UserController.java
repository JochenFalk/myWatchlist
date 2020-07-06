package com.company.presentation;

import com.company.business.UserBusiness;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class UserController {

    @GetMapping("/getLoginStatus")
    public Boolean getLoginStatus(HttpSession session) {
        return UserBusiness.getLoginStatus(session);
    }

    @GetMapping("/login")
    public String login(HttpSession session,
                        @RequestParam(value = "userName", required = true) String userName,
                        @RequestParam(value = "userPass", required = true) String userPass) {
        return UserBusiness.login(userName, userPass, session);
    }

    @GetMapping("/logOut")
    public Boolean logOut(HttpSession session) {
        return UserBusiness.logOut(session);
    }

    @GetMapping("/deleteAccount")
    public Boolean deleteAccount(HttpSession session) {
        return UserBusiness.deleteAccount(session);
    }

    @GetMapping("/createUser")
    public String createUser(
            @RequestParam(value = "userName", required = true) String userName,
            @RequestParam(value = "userPass", required = true) String userPass,
            @RequestParam(value = "userEmail", required = true) String userEmail) {
        return UserBusiness.createUser(userName, userPass, userEmail);
    }

    @GetMapping("/isVerifiedUser")
    public Boolean isVerifiedUser(HttpSession session,
                                  @RequestParam(value = "userName", required = true) String userName,
                                  @RequestParam(value = "userPass", required = true) String userPass) {
        return UserBusiness.isVerifiedUser(userName, userPass, session);
    }

    @GetMapping("/requestLink")
    public Boolean requestLink(
            @RequestParam(value = "userEmail", required = true) String userEmail) {
        return UserBusiness.requestLink(userEmail);
    }
}
