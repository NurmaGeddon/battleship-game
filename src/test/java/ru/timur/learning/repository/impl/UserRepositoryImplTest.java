package ru.timur.learning.repository.impl;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.timur.learning.configuration.WebConfig;
import ru.timur.learning.model.User;
import org.springframework.test.context.ContextConfiguration;
import ru.timur.learning.repository.UserRepository;

import javax.sql.DataSource;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = WebConfig.class)
class UserRepositoryImplTest {

    private UserRepository userRepository;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        ApplicationContext testContext = new AnnotationConfigApplicationContext(TestConfig.class);

        DataSource dataSource = testContext.getBean(DataSource.class);

        userRepository = new UserRepositoryImpl(dataSource);
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @org.junit.jupiter.api.Test
    void save() {
        User newUser = new User(null, "login10", "password10");
        User savedUser = userRepository.save(newUser);

        assertEquals(newUser.getLogin(), savedUser.getLogin());
        assertEquals(newUser.getPassword(), savedUser.getPassword());

        userRepository.deleteById(savedUser.getId());
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
        User testUser = new User(null, "login10", "password10");
        User savedUser = userRepository.save(testUser);
        User updatedUser = new User(savedUser.getId(), "newlogin10", "newpassword10");
        userRepository.update(updatedUser);

        Optional<User> expected = Optional.of(updatedUser);
        Optional<User> actual = userRepository.findById(savedUser.getId());
        assertEquals(expected, actual);

        userRepository.deleteById(savedUser.getId());
    }

    @org.junit.jupiter.api.Test
    void deleteById() {
        User newUser = new User(null, "login10", "password10");
        User savedUser = userRepository.save(newUser);

        assertTrue(userRepository.deleteById(savedUser.getId()));

        Optional<User> expected = Optional.empty();
        Optional<User> actual = userRepository.findById(savedUser.getId());
        assertEquals(expected, actual);
    }
}