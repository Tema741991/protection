package ru.top.cinemas.dtos;

import lombok.Data;

import java.util.List;

@Data
public class AuthUserDTO {
    String login;
    String password;
}
