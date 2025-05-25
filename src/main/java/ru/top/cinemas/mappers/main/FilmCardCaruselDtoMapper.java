package ru.top.cinemas.mappers.main;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.top.cinemas.dtos.main.FilmCardCaruselDto;
import ru.top.cinemas.entities.AgeRating;
import ru.top.cinemas.entities.Film;
import ru.top.cinemas.entities.Genre;
import ru.top.cinemas.repositories.AgeRatingRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FilmCardCaruselDtoMapper {

    private final AgeRatingRepository ageRatingRepository;
    public FilmCardCaruselDto toDto(Film film){
        AgeRating ageRatingById = ageRatingRepository.findById(film.getAgeRating().getId()).orElse(null);
        FilmCardCaruselDto dto = new FilmCardCaruselDto();
        dto.setId(film.getId());
        dto.setTitle(film.getTitle());
        dto.setPosterPath(film.getPosterPath());
        dto.setYear(film.getYear());
        dto.setGenre(getFirstGenreName(film));
        dto.setRating(film.getRating());
        dto.setAgeRating(ageRatingById);
        return dto;
    }

    private String getFirstGenreName(Film film) {
        return film.getGenres().stream()
                .findFirst()
                .map(Genre::getName)
                .orElse("");
    }
}
