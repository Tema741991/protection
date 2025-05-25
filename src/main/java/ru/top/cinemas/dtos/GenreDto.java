package ru.top.cinemas.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class GenreDto {

    private Long id;

    @NotBlank(message = "Название жанра обязательно")
    @Size(max = 100, message = "Название жанра не должно превышать 100 символов")
    private String name;

    @Size(max = 500, message = "Описание не должно превышать 500 символов")
    private String description;
}
