package ru.timur.learning.repository.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.timur.learning.model.User;
import ru.timur.learning.repository.UserRepository;
import ru.timur.learning.configuration.TestConfig;

import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class UserRepositoryImplTest {

    private final UserRepository userRepository;

    private User user;

    UserRepositoryImplTest(@Autowired UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        user = new User(null, "login", "password");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void save() {
        User expected = user;
        User actual = userRepository.save(expected);

        expected.setId(actual.getId());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void findById() {
        User expected = user;
        User saved = userRepository.save(expected);

        expected.setId(saved.getId());

        User actual = userRepository.findById(saved.getId());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void findAll() {
        User user1 = user;
        User user2 = new User(null, "login1", "password2");
        User user3 = new User(null, "login2", "password3");

        User saved1 = userRepository.save(user1);
        User saved2 = userRepository.save(user2);
        User saved3 = userRepository.save(user3);

        List<User> expected = List.of(saved1, saved2, saved3);
        List<User> actual = userRepository.findAll();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void update() {
        User savedUser = userRepository.save(user);

        User expected = new User(savedUser.getId(), "login1", "password1");
        User actual = userRepository.update(expected);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void deleteById() {
        User savedUser = userRepository.save(user);

        Assertions.assertTrue(userRepository.deleteById(savedUser.getId()));
        Assertions.assertNull(userRepository.findById(savedUser.getId()));
    }
}