package ru.top.cinemas.utils;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.top.cinemas.entities.Role;
import ru.top.cinemas.entities.User;
import ru.top.cinemas.repositories.RoleRepository;
import ru.top.cinemas.repositories.UserRepository;

import java.util.HashSet;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @PostConstruct
    public void init() {
        // Создание ролей
        Role adminRole = createRoleIfNotFound("ADMIN");
        Role kassRole = createRoleIfNotFound("CASHIER");
        Role userRole = createRoleIfNotFound("USER");

        // Создание пользователей
        createUserIfNotFound("user", "root", "petr@mail.ru", "Петр", List.of(userRole));
        createUserIfNotFound("admin", "root", "adm_premier@mail.ru", "Артем", List.of(adminRole));
        createUserIfNotFound("kassa", "root", "kassa_premier@mail.ru", "Ольга", List.of(kassRole));
    }

    private Role createRoleIfNotFound(String name) {
        return roleRepository.findByName(name)
                .orElseGet(() -> roleRepository.save(new Role(name)));
    }

    private void createUserIfNotFound(String username, String password, String email, String name, List<Role> roles) {
        if (!userRepository.existsByUsername(username)) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setEmail(email);
            user.setName(name);
            user.setRoles(new HashSet<>(roles));
            user.isEnabled();
            userRepository.save(user);
        }
    }
}