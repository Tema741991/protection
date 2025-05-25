package ru.top.cinemas.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.top.cinemas.entities.Hall;
import ru.top.cinemas.entities.Seat;
import ru.top.cinemas.entities.SessionStatus;
import ru.top.cinemas.entities.WorkTime;
import ru.top.cinemas.repositories.HallRepository;
import ru.top.cinemas.repositories.SeatRepository;
import ru.top.cinemas.repositories.SessionRepository;
import ru.top.cinemas.repositories.WorkTimeRepository;
import ru.top.cinemas.services.HallService;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class HallServiceImpl implements HallService {

    private final HallRepository hallRepository;
    private final SeatRepository seatRepository;
    private final WorkTimeRepository workTimeRepository;
    private final SessionRepository sessionRepository;


    @Override
    @Transactional
    public Hall createHallWithSeats(Hall hall) {
        hall.setCapacity(hall.getNumberOfRows() * hall.getNumberSeatsOfRows());
        if(hallRepository.existsByName(hall.getName())){
            throw new IllegalArgumentException("Зал с таким именем уже существует");
        }
        Hall savedHall = hallRepository.save(hall);
        List<WorkTime> defaultTimes = Arrays.stream(DayOfWeek.values())
                .map(day -> {
                    WorkTime wt = new WorkTime();
                    wt.setDayOfWeek(day);
                    wt.setOpenTime(LocalTime.of(9, 0));
                    wt.setCloseTime(LocalTime.of(23, 0));
                    wt.setHall(savedHall);
                    return wt;
                }).collect(Collectors.toList());

        workTimeRepository.saveAll(defaultTimes);
        List<Seat> seats = generateSeatsForHall(savedHall);
        seatRepository.saveAll(seats);
        savedHall.setSeatList(seats);
        return hallRepository.save(savedHall);
    }

    @Transactional(readOnly = true)
    public List<Hall> getAllHalls() {
        return hallRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Hall> getAllHallsByStatus(boolean status){
        return hallRepository.findAllByActiveHall(status);
    }

    @Transactional(readOnly = true)
    public Hall getHallById(Long id) {
        return hallRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Зал не найден"));
    }

    @Transactional
    public void editSeat(Long hallId, int row, int col, String action) {
        Hall hall = hallRepository.findById(hallId)
                .orElseThrow(() -> new IllegalArgumentException("Зал не найден"));

        if (hall.isActiveHall()) {
            throw new IllegalStateException("Редактирование запрещено: зал активен.");
        }

        Optional<Seat> seatOpt = seatRepository.findByHallAndRowNumberAndSeatNumber(
                hall, String.valueOf(row), String.valueOf(col)
        );

        switch (action) {
            case "delete" -> {
                if (seatOpt.isPresent()) {
                    Seat seat = seatOpt.get();

                    seatRepository.delete(seat);

                    int deactivePlaces = hall.getDeactivePlaces();
                    if(!seat.isActiveSeat()){
                        hall.setDeactivePlaces(--deactivePlaces);
                    }else {
                        int capacity = hall.getCapacity();
                        hall.setCapacity(--capacity);
                    }
                    hallRepository.save(hall);

                }
            }
            case "deactivate" -> {
                if (seatOpt.isPresent()) {
                    Seat seat = seatOpt.get();

                    seat.setActiveSeat(false);
                    seatRepository.save(seat);

                    int deactivePlaces = hall.getDeactivePlaces();
                    int capacity = hall.getCapacity();
                    hall.setDeactivePlaces(++deactivePlaces);
                    hall.setCapacity(--capacity);
                    hallRepository.save(hall);

                }
            }
            case "activate" -> {
                if (seatOpt.isPresent()) {
                    Seat seat = seatOpt.get();
                    seat.setActiveSeat(true);
                    seatRepository.save(seat);

                    int deactivePlaces = hall.getDeactivePlaces();
                    int capacity = hall.getCapacity();
                    hall.setDeactivePlaces(--deactivePlaces);
                    hall.setCapacity(++capacity);
                    hallRepository.save(hall);

                }
            }
            case "add" -> {
                if (seatOpt.isEmpty()) {
                    Seat newSeat = new Seat();
                    newSeat.setRowNumber(String.valueOf(row));
                    newSeat.setSeatNumber(String.valueOf(col));
                    newSeat.setActiveSeat(true);
                    newSeat.setHall(hall);
                    seatRepository.save(newSeat);
                    int capacity = hall.getCapacity();
                    hall.setCapacity(++capacity);
                    hallRepository.save(hall);
                }
            }
            default -> throw new IllegalArgumentException("Неизвестное действие: " + action);
        }
    }

    @Transactional
    public void deleteHall(Long hallId) {
        Hall hall = hallRepository.findById(hallId)
                .orElseThrow(() -> new IllegalArgumentException("Зал не найден"));

        if (hall.isActiveHall()) {
            throw new IllegalArgumentException("Нельзя удалить активный зал.");
        }
        seatRepository.deleteAll(hall.getSeatList());
        hallRepository.delete(hall);
    }

    @Transactional
    @Override
    public void updateHall(Hall hall) {
        hallRepository.save(hall);
    }

    @Transactional
    @Override
    public void toggleHallActiveStatus(Long hallId) {
        Hall hallById = getHallById(hallId);
        if(hallById.isActiveHall()){
            if(sessionRepository.existsByHallIdAndStatus(hallById.getId(),SessionStatus.DRAFT)){
                throw new IllegalArgumentException("Нельзя деактивировать зал пока в нем запланированы сеансы");
            };
            if(sessionRepository.existsByHallIdAndStatus(hallById.getId(),SessionStatus.PUBLISHED)){
                throw new IllegalArgumentException("Нельзя деактивировать зал пока в нем запланированы сеансы");
            }
        }
        hallById.setActiveHall(!hallById.isActiveHall());
        hallRepository.save(hallById);
    }

    private List<Seat> generateSeatsForHall(Hall hall) {
        List<Seat> seats = new ArrayList<>();
        for (int row = 1; row <= hall.getNumberOfRows(); row++) {
            for (int seatNum = 1; seatNum <= hall.getNumberSeatsOfRows(); seatNum++) {
                Seat seat = new Seat();
                seat.setRowNumber(String.valueOf(row));
                seat.setSeatNumber(String.valueOf(seatNum));
                seat.setActiveSeat(true);
                seat.setHall(hall);
                seats.add(seat);
            }
        }
        return seats;
    }

    private int parseSeatCoordinate(String coordinate, int maxValue) throws NumberFormatException {
        if (coordinate == null || coordinate.trim().isEmpty()) {
            return -1;
        }

        int value = Integer.parseInt(coordinate.trim());
        return (value > 0 && value <= maxValue) ? value : -1;
    }

    public Seat[][] getSeatMatrix(Hall hall) {
        // 1. Проверка входных данных
        if (hall == null || hall.getSeatList() == null) {
            return new Seat[0][0];
        }

        // 2. Проверка и получение размеров зала
        Integer rows = hall.getNumberOfRows();
        Integer cols = hall.getNumberSeatsOfRows();

        if (rows == null || cols == null || rows <= 0 || cols <= 0) {
            log.warn("Invalid hall dimensions: rows={}, cols={}", rows, cols);
            return new Seat[0][0];
        }

        // 3. Инициализация матрицы
        Seat[][] matrix = new Seat[rows][cols];
        int placedSeats = 0;

        // 4. Заполнение матрицы
        for (Seat seat : hall.getSeatList()) {
            if (seat == null) {
                log.debug("Found null seat in hall {}", hall.getId());
                continue;
            }

            try {
                // 5. Парсинг координат места
                int rowIndex = parseSeatCoordinate(seat.getRowNumber(), rows) - 1;
                int colIndex = parseSeatCoordinate(seat.getSeatNumber(), cols) - 1;

                if (rowIndex >= 0 && colIndex >= 0) {
                    matrix[rowIndex][colIndex] = seat;
                    placedSeats++;
                }
            } catch (NumberFormatException e) {
                log.error("Invalid seat coordinates in hall {}: row={}, seat={}",
                        hall.getId(), seat.getRowNumber(), seat.getSeatNumber());
            }
        }

        log.debug("Created matrix for hall {}: {}x{}, placed {}/{} seats",
                hall.getId(), rows, cols, placedSeats, hall.getSeatList().size());
        return matrix;
    }

}
