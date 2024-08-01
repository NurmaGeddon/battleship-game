package ru.timur.learning.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    private static final Logger logger =
            LoggerFactory.getLogger(LoginController.class);

    @GetMapping("/login")
    public String getLoginPage() {
        logger.debug("getLoginPage() is executed, value {}", LoginController.class);

        return "login";
    }
}
