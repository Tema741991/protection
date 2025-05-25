package ru.top.cinemas.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import ru.top.cinemas.entities.Film;
import ru.top.cinemas.entities.FilmStatus;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface FilmService {
    List<Film> getAll();
    Film getById(Long id);
    Film save(Film film, MultipartFile posterPath) throws IOException;
    void delete(Long id) throws IOException;
    Page<Film> search(String title, Integer year, Set<Long> genreIds, Long ageRatingId, FilmStatus filmStatus, Pageable pageable);
}
