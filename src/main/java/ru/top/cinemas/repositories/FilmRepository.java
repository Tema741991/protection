package ru.top.cinemas.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.top.cinemas.entities.Film;
import ru.top.cinemas.entities.FilmStatus;
import ru.top.cinemas.entities.Genre;

import java.util.List;
import java.util.Optional;


@Repository
public interface FilmRepository extends JpaRepository<Film,Long>, JpaSpecificationExecutor<Film> {

    Page<Film> findAll(Specification<Film> spec, Pageable pageable);

    List<Film> findAllByStatus(FilmStatus status);

    List<Film> findAllByStatusIn(List<FilmStatus> statuses);

    @EntityGraph(attributePaths = {"genres", "ageRating"})
    @Query("SELECT f FROM Film f WHERE f.id = :id")
    Optional<Film> findByIdWithDetails(@Param("id") Long id);

    boolean existsFilmsByAgeRatingId(Long id);
    boolean existsFilmsByGenresId(Long id);
    boolean existsFilmsByTitle(String title);
}
