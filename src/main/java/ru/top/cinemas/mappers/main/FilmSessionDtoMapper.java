package ru.top.cinemas.mappers.main;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.top.cinemas.dtos.main.FilmSessionDto;
import ru.top.cinemas.dtos.main.SessionInfoDto;
import ru.top.cinemas.entities.AgeRating;
import ru.top.cinemas.entities.Film;
import ru.top.cinemas.entities.Genre;
import ru.top.cinemas.entities.Session;
import ru.top.cinemas.repositories.AgeRatingRepository;
import ru.top.cinemas.repositories.FilmRepository;

import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FilmSessionDtoMapper {

    private final FilmRepository filmRepository;
    private final AgeRatingRepository ageRatingRepository;

    public FilmSessionDto toDto(Film film, List<Session> sessions){
        Film findFilm = filmRepository.findById(film.getId()).orElse(null);
        AgeRating ageRatingById = ageRatingRepository.findById(findFilm.getAgeRating().getId()).orElse(null);

        FilmSessionDto dto = new FilmSessionDto();
        dto.setId(findFilm.getId());
        dto.setTitle(findFilm.getTitle());
        dto.setPosterPath(findFilm.getPosterPath());
        dto.setGenres(mapGenres(findFilm.getGenres()));
        dto.setDuration(findFilm.getDuration());
        dto.setAgeRating(ageRatingById);
        dto.setDescription(findFilm.getDescription());
        dto.setSessions(sessions.stream()
                .map(session -> new SessionInfoDto(
                        session.getId(),
                        session.getStartTime().toLocalTime()))
                .sorted(Comparator.comparing(SessionInfoDto::getStartTime))
                .collect(Collectors.toList()));

        return dto;
    }
    private Set<String> mapGenres(Set<Genre> genres) {
        return genres.stream()
                .map(Genre::getName)
                .collect(Collectors.toSet());
    }

    private List<LocalTime> mapSessionTimes(List<Session> sessions) {
        return sessions.stream()
                .map(session -> session.getStartTime().toLocalTime())
                .sorted()
                .collect(Collectors.toList());
    }


}
