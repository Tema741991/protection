package ru.top.cinemas.dtos.main;

import lombok.Data;
import ru.top.cinemas.entities.AgeRating;

import java.math.BigDecimal;

@Data
public class FilmCardCaruselDto {
    private Long id;
    private String title;
    private String posterPath;
    private String genre;
    private int year;
    private BigDecimal rating;
    private AgeRating ageRating;
}
