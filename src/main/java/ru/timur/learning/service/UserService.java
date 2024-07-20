package ru.timur.learning.service;

import ru.timur.learning.model.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    void addUser(User user);

    User getUser(Long id);

    void updateUser(Long userId, User updateData);

    void deleteUser(Long userId);
}
