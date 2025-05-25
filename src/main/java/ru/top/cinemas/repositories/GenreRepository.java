package ru.top.cinemas.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.top.cinemas.entities.Genre;

import java.util.List;
import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long>, JpaSpecificationExecutor<Genre>, PagingAndSortingRepository<Genre,Long> {

    Page<Genre> findAll(Specification<Genre> spec, Pageable pageable);
    Optional<Genre> findByName(String name);

    boolean existsByName(String name);

    @Query("SELECT g FROM Genre g JOIN g.films f WHERE f.id = :filmId")
    List<Genre> findByFilmId(@Param("filmId") Long filmId);

    List<Genre> findByNameContainingIgnoreCase(String namePart);
}
