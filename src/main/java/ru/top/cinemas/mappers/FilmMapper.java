package ru.top.cinemas.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.top.cinemas.dtos.FilmDTO;
import ru.top.cinemas.entities.AgeRating;
import ru.top.cinemas.entities.Film;
import ru.top.cinemas.entities.Genre;
import ru.top.cinemas.repositories.AgeRatingRepository;
import ru.top.cinemas.repositories.GenreRepository;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FilmMapper {

    private final GenreRepository genreRepository;
    private final AgeRatingRepository ageRatingRepository;

    public FilmDTO toDto(Film film) {
        FilmDTO filmDTO = new FilmDTO();
        filmDTO.setId(film.getId());
        filmDTO.setTitle(film.getTitle());
        filmDTO.setYear(film.getYear());
        filmDTO.setDescription(film.getDescription());
        filmDTO.setDuration(film.getDuration());
        filmDTO.setRating(film.getRating());
        filmDTO.setPosterPath(film.getPosterPath());
        filmDTO.setAgeRatingId(film.getAgeRating() != null ? film.getAgeRating().getId() : null);
        filmDTO.setAgeRatingCode(film.getAgeRating().getCode());
        filmDTO.setGenreIds(film.getGenres().stream()
                .map(Genre::getId)
                .collect(Collectors.toSet()));
        filmDTO.setStatus(film.getStatus());
        return filmDTO;
    }

    public Film toEntity(FilmDTO filmDTO) {
        Film entity = new Film();

        entity.setId(filmDTO.getId());
        entity.setTitle(filmDTO.getTitle());
        entity.setYear(filmDTO.getYear());
        entity.setRating(filmDTO.getRating());
        entity.setDescription(filmDTO.getDescription());
        entity.setDuration(filmDTO.getDuration());
        entity.setPosterPath(filmDTO.getPosterPath());

        if (filmDTO.getAgeRatingId() != null) {
            AgeRating ageRating = ageRatingRepository.findById(filmDTO.getAgeRatingId())
                    .orElseThrow(() -> new RuntimeException("Age rating not found"));
            entity.setAgeRating(ageRating);
        }

        Set<Genre> genres = filmDTO.getGenreIds().stream()
                .map(id -> genreRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Age rating not found")))
                .collect(Collectors.toSet());
        entity.setGenres(genres);
        entity.setStatus(filmDTO.getStatus());
        return entity;
    }
}
