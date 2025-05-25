package ru.top.cinemas.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.top.cinemas.dtos.main.*;
import ru.top.cinemas.entities.Film;
import ru.top.cinemas.entities.Genre;
import ru.top.cinemas.entities.Session;
import ru.top.cinemas.entities.SessionStatus;
import ru.top.cinemas.mappers.main.FilmSessionDtoMapper;
import ru.top.cinemas.repositories.FilmRepository;
import ru.top.cinemas.services.impl.SessionServiceImpl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class MainService {
    private final SessionServiceImpl sessionService;
    private final FilmSessionDtoMapper filmSessionDtoMapper;
    private final FilmRepository filmRepository;
    public List<DayInfoDto> getDayInfos(LocalDate today, int count) {

        List<DayInfoDto> dayInfos = IntStream.range(0, count)
                .mapToObj(today::plusDays)
                .map(this::convertToDayInfoDto)
                .collect(Collectors.toList());

        return dayInfos;
    }

    private DayInfoDto convertToDayInfoDto(LocalDate date) {

        String dayName = capitalizeFirstLetter(getDayName(date));
        String formattedDate = date.format(DateTimeFormatter.ofPattern("d MMMM", new Locale("ru")));
        return new DayInfoDto(date, dayName, formattedDate,false);
    }

    private String getDayName(LocalDate date) {

        LocalDate today = LocalDate.now();
        if (date.equals(today)) return "Сегодня";
        if (date.equals(today.plusDays(1))) return "Завтра";

        return date.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("ru"));
    }

    private String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public List<FilmSessionDto> getDailySchedule(LocalDate date) {
        List<Session> sessions = sessionService.getSessionsByDateAndStatus(date, SessionStatus.PUBLISHED);

        // Группируем сеансы по фильмам
        Map<Film, List<Session>> sessionsByFilm = sessions.stream()
                .collect(Collectors.groupingBy(Session::getFilm));

        // Преобразуем в DTO через маппер
        return sessionsByFilm.entrySet().stream()
                .map(entry -> filmSessionDtoMapper.toDto(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(FilmSessionDto::getTitle))
                .collect(Collectors.toList());
    }

    public List<DayScheduleDto> getFilmSchedule(Long filmId, LocalDate startDate, LocalDate endDate) {
        List<Session> sessions = sessionService.findByFilmIdAndDateRange(
                filmId,
                startDate.atStartOfDay(),
                endDate.atTime(LocalTime.MAX)
        );

        return sessions.stream()
                .collect(Collectors.groupingBy(
                        session -> session.getStartTime().toLocalDate(),
                        Collectors.mapping(
                                session -> new SessionInfoDto(session.getId(), session.getStartTime().toLocalTime()),
                                Collectors.toList()
                        )
                ))
                .entrySet().stream()
                .map(entry -> new DayScheduleDto(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(DayScheduleDto::getDate))
                .collect(Collectors.toList());
    }

    public FilmDetailsDto getFilmById(Long id) {

            Film film = filmRepository.findByIdWithDetails(id)
                    .orElseThrow(() -> new EntityNotFoundException("Film not found"));

            return FilmDetailsDto.builder()
                    .id(film.getId())
                    .title(film.getTitle())
                    .posterPath(film.getPosterPath())
                    .year(film.getYear())
                    .duration(film.getDuration())
                    .ageRating(film.getAgeRating())
                    .genres(film.getGenres().stream()
                            .map(Genre::getName)
                            .collect(Collectors.toSet()))
                    .description(film.getDescription())
                    .build();
        }
}