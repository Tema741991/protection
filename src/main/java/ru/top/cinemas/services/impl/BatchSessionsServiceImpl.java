package ru.top.cinemas.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.top.cinemas.entities.Session;
import ru.top.cinemas.entities.SessionStatus;
import ru.top.cinemas.repositories.SessionRepository;
import ru.top.cinemas.services.BatchSessionsService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BatchSessionsServiceImpl implements BatchSessionsService {
    private final SessionRepository sessionRepository;

    @Override
    public void publishSessions(List<Long> sessionIds) {
        List<Session> sessions = sessionRepository.findAllById(sessionIds);

              if (sessions.stream().anyMatch(s -> s.getStatus() != SessionStatus.DRAFT)) {
            throw new IllegalStateException("Не все сеансы являются черновиками");
        }
        sessions.forEach(session -> {
            session.setStatus(SessionStatus.PUBLISHED);
            sessionRepository.save(session);
        });
    }

    @Override
    public void deleteSessions(List<Long> sessionIds) {
        List<Session> sessions = sessionRepository.findAllById(sessionIds);

        if (sessions.stream().anyMatch(s -> s.getStatus() != SessionStatus.DRAFT)) {
            throw new IllegalStateException("Можно удалять только черновики");
        }

        sessionRepository.deleteAllById(sessionIds);
    }

    @Override
    public void completeSessions(List<Long> sessionIds) {
        List<Session> sessions = sessionRepository.findAllById(sessionIds);

              if (sessions.stream().anyMatch(s -> s.getStatus() != SessionStatus.PUBLISHED)) {
            throw new IllegalStateException("Не все сеансы опубликованы");
        }
        sessions.forEach(session -> {
            session.setStatus(SessionStatus.ARCHIVED);
            sessionRepository.save(session);
        });
    }

    @Override
    public void cancelSessions(List<Long> sessionIds) {
        List<Session> sessions = sessionRepository.findAllById(sessionIds);

        if (sessions.stream().anyMatch(s -> s.getStatus() != SessionStatus.PUBLISHED)) {
            throw new IllegalStateException("Не все сеансы опубликованы");
        }
        sessions.forEach(session -> {
            session.setStatus(SessionStatus.CANCELED);
            sessionRepository.save(session);
        });
    }
}
