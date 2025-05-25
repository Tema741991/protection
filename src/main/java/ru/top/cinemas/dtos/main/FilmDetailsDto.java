package ru.top.cinemas.dtos.main;

import lombok.Builder;
import lombok.Data;
import ru.top.cinemas.entities.AgeRating;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Builder
public class FilmDetailsDto {
    private Long id;
    private String title;
    private String posterPath;
    private int year;
    private int duration;
    private BigDecimal rating;
    private AgeRating ageRating;
    private Set<String> genres;
    private String description;
}
