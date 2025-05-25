package ru.top.cinemas.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class HallDto {
    private Long id;

    @NotBlank(message = "Имя не может быть пустым")
    private String name;
    @Min(value = 1,message = "Количество рядом в зале не может быть меньше 1")
    private int numberOfRows;
    @Min(value = 1,message = "Количество мест в ряде не может быть меньше 1")
    private int numberSeatsOfRows;


}
