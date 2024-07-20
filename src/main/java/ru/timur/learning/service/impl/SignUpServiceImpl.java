package ru.timur.learning.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.timur.learning.dto.UserDto;
import ru.timur.learning.model.User;
import ru.timur.learning.repository.UserRepository;
import ru.timur.learning.service.SignUpService;

@RequiredArgsConstructor
@Service
public class SignUpServiceImpl implements SignUpService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void signUp(UserDto userDto) {
        String passwordHash = passwordEncoder.encode(userDto.getPassword());
        User newUser = new User(null, userDto.getLogin(), passwordHash);

        userRepository.save(newUser);
    }
}
