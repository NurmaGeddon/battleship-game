package ru.timur.learning.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    private static final Logger logger =
            LoggerFactory.getLogger(HelloController.class);

    @GetMapping("/hello")
    public String printWelcome(ModelMap model) {

        logger.debug("printWelcome() is executed, value {}", HelloController.class);

        model.addAttribute("message", "Spring 3 MVC Hello World");
        return "hello";
    }
}