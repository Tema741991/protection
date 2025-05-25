package ru.top.cinemas.services;

import java.util.List;

public interface BatchSessionsService {
    void publishSessions(List<Long> sessionIds);

    void deleteSessions(List<Long> sessionIds);

    void completeSessions(List<Long> sessionIds);

    void cancelSessions(List<Long> sessionIds);
}
