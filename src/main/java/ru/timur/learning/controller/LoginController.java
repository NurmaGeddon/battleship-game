package ru.timur.learning.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String getSignInPage() {
        return "login";
    }

//    @PostMapping
//    public String singIn() {
//        return "redirect:/battleship";
//    }
}
