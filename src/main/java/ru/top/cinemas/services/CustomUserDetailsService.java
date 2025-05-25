package ru.top.cinemas.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.top.cinemas.entities.User;
import ru.top.cinemas.repositories.RoleRepository;
import ru.top.cinemas.repositories.UserRepository;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("User not found with username: " + username));
    }

    @Transactional
    public void updateProfile(User user, String name, String email) {
        if (name != null && !name.isBlank()) {
            user.setName(name);
        }
        if (email != null && !email.isBlank() && !email.equals(user.getEmail())) {
            user.setEmail(email);
        }
        userRepository.save(user);
    }

    @Transactional
    public void changePassword(User user, String currentPassword, String newPassword, String confirmPassword) {
        User findUser = userRepository.findById(user.getId()).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
        if (!passwordEncoder.matches(currentPassword, findUser.getPassword())) {
            throw new IllegalArgumentException("Введен не верный текущий пароль");
        }
        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("Пароли не совпадают");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
