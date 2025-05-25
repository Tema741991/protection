package ru.top.cinemas.dtos.main;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.top.cinemas.entities.AgeRating;

import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Data
public class FilmSessionDto {
    private Long id;
    private String title;
    private String posterPath;
    private Set<String> genres;
    private int duration;
    private String description;
    private Long sessionId;
    private AgeRating ageRating;
    private List<SessionInfoDto> sessions;

}
