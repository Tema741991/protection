package ru.top.cinemas.services.impl;

import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.top.cinemas.entities.AgeRating;
import ru.top.cinemas.repositories.AgeRatingRepository;
import ru.top.cinemas.repositories.FilmRepository;
import ru.top.cinemas.services.AgeRatingService;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AgeRatingServiceImpl implements AgeRatingService {

    private final AgeRatingRepository ageRatingRepository;
    private final FilmRepository filmRepository;

    @Transactional(readOnly = true)
    public List<AgeRating> getAll() {
        return ageRatingRepository.findAll();
    }

    @Transactional(readOnly = true)
    public AgeRating getById(Long id) throws NotFoundException {
        return ageRatingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Age rating not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public AgeRating getByCode(String code) throws NotFoundException {
        return ageRatingRepository.findByCode(code)
                .orElseThrow(() -> new NotFoundException("Age rating not found with code: " + code));
    }

    @Transactional
    public AgeRating save(AgeRating ageRating) {

        if (ageRating.getId() != null) {

            AgeRating existing = ageRatingRepository.findById(ageRating.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Рейтинг не найден"));

            if (!existing.getCode().equals(ageRating.getCode())) {
                // Если код изменили - проверяем уникальность нового кода
                if (ageRatingRepository.existsByCode(ageRating.getCode())) {
                    throw new IllegalArgumentException("Код рейтинга уже используется");
                }
            }
            existing.setName(ageRating.getName());
            existing.setCode(ageRating.getCode());
            existing.setMinAge(ageRating.getMinAge());
            existing.setDescription(ageRating.getDescription());
            return ageRatingRepository.save(existing);
        } else {
            if (ageRatingRepository.existsByCode(ageRating.getCode())) {
                throw new IllegalArgumentException("Возрастной рейтинг с таким кодом  " + ageRating.getCode() + " уже существует");
            }
            return ageRatingRepository.save(ageRating);
        }
    }

    @Transactional
    public AgeRating update(Long id, AgeRating ageRating) throws NotFoundException {
        AgeRating ageRatingById = getById(id);

        if (!ageRatingById.getCode().equals(ageRating.getCode())) {
            if (ageRatingRepository.existsByCode(ageRating.getCode())) {
                throw new IllegalArgumentException("Возрастной рейтинг с таким кодом " + ageRating.getCode() + "уже существует");
            }
        }
        ageRatingById.setCode(ageRating.getCode());
        ageRatingById.setName(ageRating.getName());
        ageRatingById.setDescription(ageRating.getDescription());
        ageRatingById.setMinAge(ageRating.getMinAge());
        return ageRatingRepository.save(ageRating);
    }

    @Transactional
    public void delete(Long id) throws NotFoundException {
        AgeRating ageRating = ageRatingRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Жанр не найден"));
        if(filmRepository.existsFilmsByAgeRatingId(ageRating.getId())){
            throw new IllegalStateException("Удаление не возможно: есть фильмы с этим рейтингом");
        }
        ageRatingRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<AgeRating> getForAge(int age) {
        return ageRatingRepository.findByMinAgeLessThanEqual(age);
    }
}
