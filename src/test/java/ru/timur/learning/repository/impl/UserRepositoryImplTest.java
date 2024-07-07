package ru.timur.learning.repository.impl;

import ru.timur.learning.db.impl.ConnectionManagerImpl;
import ru.timur.learning.model.User;
import ru.timur.learning.repository.UserRepository;
import ru.timur.learning.repository.mapper.UserResultSetMapper;

import javax.swing.text.html.Option;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryImplTest {

    private UserRepository userRepository;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        userRepository = new UserRepositoryImpl(new ConnectionManagerImpl(), new UserResultSetMapper());
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @org.junit.jupiter.api.Test
    void save() {
        User newUser = new User(10L, "login10", "password10", 10L);

    }

    @org.junit.jupiter.api.Test
    void findById() {
        System.out.println(userRepository.findById(1L));
    }

    @org.junit.jupiter.api.Test
    void findAll() {
        System.out.println(userRepository.findAll());
    }

    @org.junit.jupiter.api.Test
    void update() {
    }

    @org.junit.jupiter.api.Test
    void deleteById() {
        User testUser = new User(10L, "login10", "password10", 10L);
        userRepository.save(testUser);

        assertTrue(userRepository.deleteById(testUser.id()));

        Optional<User> expected = Optional.empty();
        Optional<User> actual = userRepository.findById(testUser.id());
        assertEquals(expected, actual);
    }
}