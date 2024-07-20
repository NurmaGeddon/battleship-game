package ru.timur.learning.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.timur.learning.model.User;
import ru.timur.learning.repository.UserRepository;
import ru.timur.learning.service.UserService;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void addUser(User user) {
        String passwordHash = passwordEncoder.encode(user.getPassword());
        User newUser = new User(user.getId(), user.getLogin(), passwordHash);
        userRepository.save(newUser);
    }

    @Override
    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    @Override
    public void updateUser(Long userId, User updateData) {
        User foundUser = userRepository.findById(userId).orElseThrow();
        User updatedUser = new User(
                userId,
                foundUser.getLogin().isEmpty()
                ? foundUser.getLogin()
                : updateData.getLogin(),
                foundUser.getPassword().isEmpty()
                ? foundUser.getPassword()
                : updateData.getPassword());
        userRepository.update(updatedUser);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
