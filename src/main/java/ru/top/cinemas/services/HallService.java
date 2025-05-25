package ru.top.cinemas.services;

import ru.top.cinemas.entities.Hall;
import ru.top.cinemas.entities.Seat;

import java.util.List;
import java.util.Map;

public interface HallService {
    Hall createHallWithSeats(Hall hall);
    Hall getHallById(Long id);
    Seat[][] getSeatMatrix(Hall hall);
    void editSeat(Long hallId, int row, int col, String action);
    List<Hall> getAllHalls();
    void deleteHall(Long id);
    void updateHall(Hall hall);
    void toggleHallActiveStatus(Long hallId);
}
