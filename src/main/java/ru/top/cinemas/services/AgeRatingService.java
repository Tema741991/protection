package ru.top.cinemas.services;


import javassist.NotFoundException;
import ru.top.cinemas.entities.AgeRating;

import java.util.List;

public interface AgeRatingService {

    List<AgeRating> getAll();
    AgeRating getById(Long id) throws NotFoundException;
    AgeRating getByCode(String code) throws NotFoundException;
    AgeRating save(AgeRating ageRating) throws Exception;
    AgeRating update(Long id, AgeRating ageRating) throws Exception;
    void delete(Long id) throws NotFoundException;
    List<AgeRating> getForAge(int age);
}
