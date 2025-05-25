package ru.top.cinemas.dtos.Schedule;

import lombok.Data;

@Data
public class FilmSessionsDto {
    private Long id;
    private String title;
    private int duration;
    private String posterPath;
}

