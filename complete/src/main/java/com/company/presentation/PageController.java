package com.company.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @RequestMapping("/reactTest")
    public String reactTest() {
        return "reactTest";
    }
}