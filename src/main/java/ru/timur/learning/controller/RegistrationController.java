package ru.timur.learning.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.timur.learning.dto.UserDto;
import ru.timur.learning.service.SignUpService;


@RequiredArgsConstructor
@Controller
@RequestMapping("/registration")
public class RegistrationController {

    private static final Logger logger =
            LoggerFactory.getLogger(RegistrationController.class);

    private final SignUpService signUpService;

    @GetMapping
    public String getRegistrationPage(Authentication authentication, Model model) {
        logger.debug("getRegistrationPage() is executed, value {}", RegistrationController.class);

        if (authentication != null) {
            return "redirect:/profile";
        }
        model.addAttribute(new UserDto());
        return "registration";
    }

    @PostMapping
    public String registerUser(UserDto userDto) {
        logger.debug("registerUser() is executed, value {}", RegistrationController.class);

        signUpService.signUp(userDto);
        return "redirect:/login";
    }
}
