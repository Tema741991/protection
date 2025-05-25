package ru.top.cinemas.services.impl;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.top.cinemas.dtos.main.FilmCardCaruselDto;
import ru.top.cinemas.entities.Film;
import ru.top.cinemas.entities.FilmStatus;
import ru.top.cinemas.entities.Genre;
import ru.top.cinemas.mappers.main.FilmCardCaruselDtoMapper;
import ru.top.cinemas.repositories.FilmRepository;
import ru.top.cinemas.repositories.SessionRepository;
import ru.top.cinemas.services.FileStorageService;
import ru.top.cinemas.services.FilmService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final FilmRepository filmRepository;
    private final FileStorageService fileStorageService;
    private final FilmCardCaruselDtoMapper filmCardCaruselDtoMapper;
    private final SessionRepository sessionRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Film> getAll() {
        return filmRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Film> getAllFilmsByStatuses(List<FilmStatus> statuses) {
        return filmRepository.findAllByStatusIn(statuses);
    }

    @Transactional(readOnly = true)
    @Override
    public Film getById(Long id) {
        return filmRepository.findById(id).orElseThrow(() -> new RuntimeException("Фильм не найден"));
    }

    @Transactional
    @Override
    public Film save(Film film, MultipartFile file) throws IOException {
        if (film.getId()!=null){
            Film existing = filmRepository.findById(film.getId()).orElseThrow(() -> new IllegalArgumentException("Фильм не найден"));
            if(!existing.getTitle().equals(film.getTitle())){
                if(filmRepository.existsFilmsByTitle(film.getTitle())){
                    throw new IllegalArgumentException("Фильм с таким именем уже существует");
                };
            }
            existing.setId(film.getId());
            existing.setDescription(film.getDescription());
            existing.setDuration(film.getDuration());
            existing.setRating(film.getRating());
            existing.setStatus(film.getStatus());
            existing.setTitle(film.getTitle());
            existing.setYear(film.getYear());
            existing.setGenres(film.getGenres());
            existing.setPosterPath(film.getPosterPath());
            existing.setAgeRating(film.getAgeRating());
            if(!file.isEmpty()){
                String posterPath = fileStorageService.storeFile(file);
                fileStorageService.deleteFile(existing.getPosterPath());
                existing.setPosterPath(posterPath);
            }
            return filmRepository.save(existing);
        }
        else {
            if(filmRepository.existsFilmsByTitle(film.getTitle().trim())){
                throw new IllegalArgumentException("Фильм с таким именем уже существует");
            };
            if(!file.isEmpty()){
                String posterPath = fileStorageService.storeFile(file);
                film.setPosterPath(posterPath);
            }
        }
        return filmRepository.save(film);
    }

    @Transactional
    @Override
    public void delete(Long id) throws IOException {
        Film removedFilm = getById(id);
        if(sessionRepository.existsSessionsByFilmId(removedFilm.getId())){
            throw new IllegalArgumentException("Невозможно удалить фильм - есть сеансы с этим фильмом");
        }
        if(removedFilm!=null){
            fileStorageService.deleteFile(removedFilm.getPosterPath());
            filmRepository.deleteById(id);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Film> search(String title, Integer year, Set<Long> genreIds, Long ageRatingId, FilmStatus status, Pageable pageable) {
        return filmRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Поиск по названию (LIKE)
            if (title != null && !title.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
            }

            // Поиск по году
            if (year != null) {
                predicates.add(cb.equal(root.get("year"), year));
            }

            // Поиск по жанрам (многие ко многим)
            if (genreIds != null && !genreIds.isEmpty()) {
                Join<Film, Genre> genreJoin = root.join("genres", JoinType.INNER);
                predicates.add(genreJoin.get("id").in(genreIds));
                query.distinct(true); // важно при JOIN'ах
            }

            // Поиск по возрастному рейтингу
            if (ageRatingId != null) {
                predicates.add(cb.equal(root.get("ageRating").get("id"), ageRatingId));
            }

            // Поиск по статусу
            if ( status!= null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        },pageable);
    }

    @Transactional(readOnly = true)
    public List<FilmCardCaruselDto> getNowShowingFilms() {
        List<Film> films = filmRepository.findAllByStatus(FilmStatus.NOW);
        return films.stream()
                .map(filmCardCaruselDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FilmCardCaruselDto> getComingSoonFilms() {
        List<Film> films = filmRepository.findAllByStatus(FilmStatus.SOON);
        return films.stream()
                .map(filmCardCaruselDtoMapper::toDto)
                .collect(Collectors.toList());
    }

}
