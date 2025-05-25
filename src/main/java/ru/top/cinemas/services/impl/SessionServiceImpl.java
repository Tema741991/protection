package ru.top.cinemas.services.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

//import ru.top.cinemas.dtos.ScheduleRequestDto;
import org.springframework.transaction.annotation.Transactional;
import ru.top.cinemas.dtos.SessionCreateDto;

import ru.top.cinemas.entities.*;
import ru.top.cinemas.mappers.SessionCreateMapper;
import ru.top.cinemas.repositories.*;


import java.time.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@Service
@RequiredArgsConstructor
public class SessionServiceImpl {

    private final SessionRepository sessionRepository;
    private final SessionCreateMapper sessionCreateMapper;

    @Transactional(readOnly = true)
    public Session findById(Long id) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Сеанс не найден"));
        return session;
    }

    @Transactional(readOnly = true)
    public Page<Session> findFilteredSessions(String filmTitle, Long hallId, LocalDate date,SessionStatus status, Pageable pageable) {
        return sessionRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filmTitle != null && !filmTitle.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("film").get("title")), "%" + filmTitle.toLowerCase() + "%"));
            }

            if (hallId != null) {
                predicates.add(cb.equal(root.get("hall").get("id"), hallId));
            }

            if (date != null) {
                LocalDateTime startOfDay = date.atStartOfDay();
                LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
                predicates.add(cb.between(root.get("startTime"), startOfDay, endOfDay));
            }

            // Поиск по статусу
            if ( status!= null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);
    }

    @Transactional(readOnly = true)
    public List<Session> getSessionsByDateAndStatus(LocalDate date,SessionStatus status) {
        return sessionRepository.findByStartTimeBetweenAndStatus(date.atStartOfDay(), date.atTime(LocalTime.MAX),status);
    }

    private boolean isStatusTransitionAllowed(SessionStatus current, SessionStatus newStatus) {
        // Черновик можно только опубликовать
        if (current == SessionStatus.DRAFT) {
            return newStatus == SessionStatus.PUBLISHED;
        }
        // Опубликованный можно завершить или отменить
        else if (current == SessionStatus.PUBLISHED) {
            return newStatus == SessionStatus.ARCHIVED || newStatus == SessionStatus.CANCELED;
        }
        // Завершенные и отмененные сеансы нельзя изменить
        return false;
    }

    @Transactional
    public void changeSessionStatus(Long idSession, SessionStatus newStatus){
        Session findSession = sessionRepository.findById(idSession).orElseThrow(() -> new EntityNotFoundException("Сеанс не найден"));
        if (!isStatusTransitionAllowed(findSession.getStatus(),newStatus)){
            throw new IllegalStateException("Недопустимый переход статуса из " +
                    findSession.getStatus() + " в " + newStatus);
        }
        findSession.setStatus(newStatus);
        sessionRepository.save(findSession);
    }

    @Transactional(readOnly = true)
    public List<Session> findByFilmIdAndDateRange(Long filmId, LocalDateTime start, LocalDateTime end) {
        return sessionRepository.findByFilmIdAndDateRangeAndStatus(filmId,start,end,SessionStatus.PUBLISHED);
    }
}
