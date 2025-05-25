package ru.top.cinemas.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class TechnicalBreakDto {

    @NotNull(message = "Пожалуйста, выберите зал")
    private Long hallId;

    private Long id;

    @NotNull(message = "Пожалуйста, укажите дату")
    private LocalDate startTime;

    @NotNull(message = "Пожалуйста укажите время начала")
    private LocalTime start;

    @NotNull(message = "Пожалуйста укажите время окончания")
    private LocalTime end;

}
