package com.company.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class PageController {

    @RequestMapping("/")
    public String index() {
        return "homePage";
    }

    @RequestMapping("/homePage")
    public String homePage() {
        return "homePage";
    }

    @RequestMapping("/moviePage")
    public String moviePage() {
        return "moviePage";
    }

    @RequestMapping("/listPage")
    public String listPage() {
        return "listPage";
    }

    @RequestMapping("/adminPage")
    public String adminPage(HttpSession session) {

        if (session.getAttribute("role") != null) {
            if (session.getAttribute("role").toString().equals("Admin")) {
                return "adminPage";
            } else {
                return "homePage";
            }
        }
        return null;
    }
}