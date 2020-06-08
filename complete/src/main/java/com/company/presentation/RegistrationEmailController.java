package com.company.presentation;

import com.company.business.UserBusiness;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistrationEmailController {

    @GetMapping("/verifyRegistration")
    public String verifyRegistration(@RequestParam(value = "token", required = true) String token) {
        Boolean isVerified = UserBusiness.verifyRegistration(token);
        if (isVerified) {
            return "accountVerificationSuccess";
        }
        return "accountVerificationFailure";
    }
}