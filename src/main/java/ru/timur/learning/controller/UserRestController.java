package ru.timur.learning.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.timur.learning.model.User;
import ru.timur.learning.repository.UserRepository;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserRestController {

    private static final Logger logger =
            LoggerFactory.getLogger(RegistrationController.class);

    private final UserRepository userRepository;

    @GetMapping("/{userId}")
    public User getUser(@PathVariable("userId") Long userId) {
        logger.debug(
                "getUser() is executed with userId: {}, value {}",
                userId,
                UserRestController.class
        );

        return userRepository.findById(userId).get();
    }

    @GetMapping
    public List<User> getUsers() {
        logger.debug("getUsers() is executed, value {}", UserRestController.class);

        return userRepository.findAll();
    }
}
