package ru.top.cinemas.services.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.top.cinemas.dtos.TechnicalBreakDto;
import ru.top.cinemas.entities.Hall;
import ru.top.cinemas.entities.TechnicalBreak;
import ru.top.cinemas.entities.WorkTime;
import ru.top.cinemas.repositories.HallRepository;
import ru.top.cinemas.repositories.SessionRepository;
import ru.top.cinemas.repositories.TechnicalBreakRepository;
import ru.top.cinemas.repositories.WorkTimeRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TechnicalBreakServiceImpl {

    private final TechnicalBreakRepository technicalBreakRepository;
    private final HallRepository hallRepository;
    private final WorkTimeRepository workTimeRepository;
    private final SessionRepository sessionRepository;

    @Transactional(readOnly = true)
    public TechnicalBreak findById(Long id){
        return technicalBreakRepository.findById(id).orElseThrow(() -> new RuntimeException("Технический перерыв не найден"));
    }

    @Transactional(readOnly = true)
    public List<TechnicalBreak> getByHallAndDate(Long hallId, LocalDate date){
        return technicalBreakRepository.findAllByHallIdAndDate(hallId, date);
    }

    @Transactional(readOnly = true)
    public Page<TechnicalBreak> findFilteredTechnicalBreaks(Long hallId, LocalDate date, Pageable pageable) {
        return technicalBreakRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (hallId != null) {
                predicates.add(cb.equal(root.get("hall").get("id"), hallId));
            }

            if (date != null) {
                predicates.add(cb.equal(root.get("date"), date));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);
    }

    @Transactional
    public void createTechnicalBreak(TechnicalBreak technicalBreak) {

        if (technicalBreak.getStart().isAfter(technicalBreak.getEnd())) {
            throw new IllegalArgumentException("Время начала должно быть раньше времени окончания");
        }

        Hall hall = hallRepository.findById(technicalBreak.getHall().getId())
                .orElseThrow(() -> new IllegalArgumentException("Зал не найден"));

        WorkTime workTime = workTimeRepository.findByHallIdAndDayOfWeek(hall.getId(), technicalBreak.getDate().getDayOfWeek())
                .orElseThrow(() -> new IllegalArgumentException("Рабочее время для зала на указанную дату не установлено"));

        if (technicalBreak.getStart().isBefore(workTime.getOpenTime())) {
            throw new IllegalArgumentException("Время начала перерыва раньше времени открытия зала");
        }
        if (technicalBreak.getEnd().isAfter(workTime.getCloseTime())) {
            throw new IllegalArgumentException("Время окончания перерыва позже времени закрытия зала");
        }

        List<TechnicalBreak> existingBreaks = technicalBreakRepository
                .findByHallIdAndDate(hall.getId(), technicalBreak.getDate());

        if (existingBreaks.stream()
                .filter(b -> technicalBreak.getId() == null || !b.getId().equals(technicalBreak.getId()))
                .anyMatch(b -> isTimeOverlap(
                        b.getStart(), b.getEnd(),
                        technicalBreak.getStart(), technicalBreak.getEnd()))) {
            throw new IllegalArgumentException("Перерыв пересекается с уже существующим");
        }

        if(sessionRepository.existsByHallAndDateTimeRange(hall,technicalBreak.getStart().atDate(technicalBreak.getDate()),technicalBreak.getEnd().atDate(technicalBreak.getDate()))){
            throw new IllegalArgumentException("В указанный период уже есть сеансы");
        }

        technicalBreakRepository.save(technicalBreak);
    }

    @Transactional
    public void deleteTechnicalBrake(Long id) {
        TechnicalBreak findById = technicalBreakRepository.findById(id).orElse(null);
        if(findById!=null){
            technicalBreakRepository.deleteById(findById.getId());
        }
    }

    private boolean isTimeOverlap(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return start1.isBefore(end2) && end1.isAfter(start2);
    }
}
