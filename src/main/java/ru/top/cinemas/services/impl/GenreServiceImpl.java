package ru.top.cinemas.services.impl;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.top.cinemas.dtos.GenreDto;
import ru.top.cinemas.entities.Film;
import ru.top.cinemas.entities.Genre;
import ru.top.cinemas.mappers.GenreMapper;
import ru.top.cinemas.repositories.FilmRepository;
import ru.top.cinemas.repositories.GenreRepository;
import ru.top.cinemas.services.GenreService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final FilmRepository filmRepository;


    @Transactional(readOnly = true)
    public List<Genre> getAll() {
        return genreRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Genre getById(Long id) throws NotFoundException {
        return genreRepository.findById(id).orElseThrow(() -> new NotFoundException("Жанр не найден"));
    }

    @Transactional(readOnly = true)
    public Genre getByName(String name) throws NotFoundException {
        return genreRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Жанр '" + name + "' не найден"));
    }

    @Transactional(readOnly = true)
    public List<Genre> getByFilmId(Long filmId) {
        return genreRepository.findByFilmId(filmId);
    }

    @Transactional(readOnly = true)
    public List<Genre> searchGenres(String namePart) {
        return genreRepository.findByNameContainingIgnoreCase(namePart);
    }

    @Transactional
    public Genre save(Genre genre) {
        if(genre.getId() != null) {
            Genre existing = genreRepository.findById(genre.getId()).orElseThrow(() -> new IllegalArgumentException("Жанр не найден"));
            if(!existing.getName().equals(genre.getName())){
                if(genreRepository.existsByName(genre.getName())){
                    throw new IllegalArgumentException("Имя жанра уже используется");
                }
            }
            existing.setId(genre.getId());
            existing.setName(genre.getName());
            existing.setDescription(genre.getDescription());
            return genreRepository.save(existing);
        }else {
            if(genreRepository.existsByName(genre.getName())){
                throw new IllegalArgumentException("Имя жанра уже используется");
            }
        }
        return genreRepository.save(genre);
    }

    @Transactional
    public Genre update(Long id, Genre genre) throws NotFoundException {

        Genre genreById = getById(id);
        genreById.setName(genre.getName());
        genreById.setDescription(genre.getDescription());
        genreById.setFilms(genre.getFilms());
        return genreRepository.save(genreById);

    }

    @Transactional
    public void delete(Long id) {
        Genre genre = genreRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Жанр не найден"));
        if(filmRepository.existsFilmsByGenresId(genre.getId())){
            throw new IllegalStateException("Удаление не возможно: есть фильмы с этим жанром");
        }
        genreRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Genre> search(String name, String description, Pageable pageable) {
        return genreRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Поиск по названию (LIKE)
            if (name != null && !name.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            // Поиск по описанию (LIKE)
            if (description != null && !description.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("description")), "%" + description.toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);
    }
}
