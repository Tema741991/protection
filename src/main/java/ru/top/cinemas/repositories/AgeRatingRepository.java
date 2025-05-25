package ru.top.cinemas.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.top.cinemas.entities.AgeRating;

import java.util.List;
import java.util.Optional;

@Repository
public interface AgeRatingRepository extends JpaRepository<AgeRating, Long> {

    // Поиск по коду рейтинга
    Optional<AgeRating> findByCode(String code);

    // Проверка существования по коду
    boolean existsByCode(String code);

    // Поиск по названию
    Optional<AgeRating> findByName(String name);

    // Поиск всех рейтингов, подходящих для указанного возраста
    List<AgeRating> findByMinAgeLessThanEqual(int age);
}
