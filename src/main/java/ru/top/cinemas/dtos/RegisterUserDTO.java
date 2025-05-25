package ru.top.cinemas.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterUserDTO {

    @NotBlank(message = "Логин пользователя обязателен")
    @Size(min = 3, max = 20, message = "Логин пользователя должен быть от 3 до 20 символов")
    String username;

    @NotBlank(message = "Имя пользователя обязательно")
    @Size(min = 3, max = 20, message = "Имя пользователя должно быть от 3 до 20 символов")
    String name;

    @Email(message = "Не верный формат ввода Email")
    @NotBlank(message = "Email обязателен")
    String email;

    @NotBlank(message = "Пароль обязателен")
    @Size(min = 4, message = "Пароль должен содержать минимум 4 символа")
    String password;
}
