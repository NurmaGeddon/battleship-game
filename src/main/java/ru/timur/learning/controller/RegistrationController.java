package ru.timur.learning.controller;

import lombok.RequiredArgsConstructor;
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

    private final SignUpService signUpService;

    @GetMapping
    public String getSignUpPage(Authentication authentication, Model model) {
        if (authentication != null) {
            return "redirect:/";
        }
        model.addAttribute(new UserDto());
        return "registration";
    }

    @PostMapping
    public String signUpUser(UserDto userDto) {

        signUpService.signUp(userDto);
        return "redirect:/login";
    }
}
