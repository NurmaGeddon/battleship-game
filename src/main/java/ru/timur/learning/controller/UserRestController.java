package ru.timur.learning.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.timur.learning.model.User;
import ru.timur.learning.repository.UserRepository;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserRestController {
    private final UserRepository userRepository;

    @GetMapping("/{userId}")
    public User getUser(@PathVariable("userId") Long userId) {
        System.out.println(userId);
        return userRepository.findById(userId).get();
    }

    @GetMapping
    public List<User> getUsers() {
        return userRepository.findAll();
    }
}
