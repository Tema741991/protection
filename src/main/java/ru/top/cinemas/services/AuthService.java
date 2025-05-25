package ru.top.cinemas.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.top.cinemas.dtos.RegisterUserDTO;
import ru.top.cinemas.entities.Role;
import ru.top.cinemas.entities.User;
import ru.top.cinemas.repositories.RoleRepository;
import ru.top.cinemas.repositories.UserRepository;

import java.util.Set;
@Transactional
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User registerUser(RegisterUserDTO registerUser) {

        if (userRepository.existsByUsername(registerUser.getUsername())) {
            throw new IllegalArgumentException("Пользователь с таким именем существует");
        }
        if (userRepository.existsByEmail(registerUser.getEmail())) {
            throw new IllegalArgumentException("Пользователь с таким email уже зарегистрирован");
        }

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Role USER not found"));
        User user = new User();
        user.setUsername(registerUser.getUsername());
        user.setName(registerUser.getName());
        user.setEmail(registerUser.getEmail());
        user.setPassword(passwordEncoder.encode(registerUser.getPassword()));
        user.setRoles(Set.of(userRole));
        return userRepository.save(user);
    }
}
