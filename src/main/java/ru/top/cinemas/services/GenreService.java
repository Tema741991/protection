package ru.top.cinemas.services;

import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.top.cinemas.entities.Film;
import ru.top.cinemas.entities.Genre;

import java.util.List;

public interface GenreService {
    List<Genre> getAll();

    Genre getById(Long id) throws NotFoundException;

    Genre getByName(String name) throws NotFoundException;

    List<Genre> getByFilmId(Long filmId);

    Genre save(Genre genre);

    Genre update(Long id, Genre genre) throws NotFoundException;

    void delete(Long id);

    Page<Genre> search(String name, String description, Pageable pageable);
}
