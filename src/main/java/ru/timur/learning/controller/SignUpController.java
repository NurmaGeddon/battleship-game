package ru.timur.learning.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.timur.learning.dto.UserDto;
import ru.timur.learning.model.User;
import ru.timur.learning.service.SignUpService;


@RequiredArgsConstructor
@Controller
@RequestMapping("/signUp")
public class SignUpController {

    private final SignUpService signUpService;

    @GetMapping
    public String getSignUpPage(Authentication authentication, Model model) {
        if (authentication != null) {
            return "redirect:/";
        }
        model.addAttribute(new UserDto());
        return "signUp_page";
    }

    @PostMapping
    public String signUpUser(UserDto userDto) {

        signUpService.signUp(userDto);
        return "redirect:/signIn";
    }
}
