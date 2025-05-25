package ru.top.cinemas.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AgeRatingDto {
    private Long id;

    @NotBlank(message = "Не может быть пустым")
    @Size(max = 10,message = "Максимальная длина 10 символов")
    private String code;

    @NotBlank(message = "Не может быть пустым")
    @Size(max = 100,message = "Максимальная длина 100 символов")
    private String name;

    @Size(max = 500,message = "Максимальная длина 500 символов")
    private String description;

    @Min(value = 0,message = "Возраст не должен быть меньше 0")
    @Max(value = 21,message = "Возраст не может быть больше 21")
    private int minAge;
}
