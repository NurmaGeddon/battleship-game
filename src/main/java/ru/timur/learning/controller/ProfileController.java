package ru.timur.learning.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.timur.learning.configuration.security.details.CustomUserDetails;

@RequiredArgsConstructor
@Controller
public class ProfileController {

    private static final Logger logger =
            LoggerFactory.getLogger(ProfileController.class);

    @GetMapping("/")
    public String redirectToProfile() {
        return "redirect:/profile";
    }

    @GetMapping("/profile")
    public String getProfile(@AuthenticationPrincipal CustomUserDetails userDetails,
                           Model model) {

        logger.debug("getProfile() is executed, value {}", ProfileController.class);

        model.addAttribute("name", userDetails.getUsername());
        return "profile";
    }
}
