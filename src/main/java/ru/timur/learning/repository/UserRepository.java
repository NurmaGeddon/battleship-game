package ru.timur.learning.repository;

import ru.timur.learning.model.User;

import java.util.Optional;

public interface UserRepository extends Repository<User, Long> {
    Optional<User> findByEmail(String login);
}
