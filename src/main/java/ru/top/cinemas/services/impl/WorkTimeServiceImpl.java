package ru.top.cinemas.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.top.cinemas.dtos.WorkTimeDto;
import ru.top.cinemas.entities.Hall;
import ru.top.cinemas.entities.WorkTime;
import ru.top.cinemas.repositories.HallRepository;
import ru.top.cinemas.repositories.WorkTimeRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WorkTimeServiceImpl {
    private final WorkTimeRepository workTimeRepository;
    private final HallRepository hallRepository;

    @Transactional(readOnly = true)
    public List<WorkTime> getByHall(Long hallId) {
        return workTimeRepository.findByHallId(hallId);
    }

    @Transactional(readOnly = true)
    public WorkTime findByHallIdAndDayOfWeek(Long hallId, LocalDate date){
        WorkTime workTime = workTimeRepository.findByHallIdAndDayOfWeek(hallId, date.getDayOfWeek())
                .orElseThrow(() -> new RuntimeException("Нет расписания работы на этот день"));
        return workTime;
    }

    @Transactional
    public void saveAll(Long hallId, List<WorkTime> workTimes) {
        Hall hall = hallRepository.findById(hallId)
                .orElseThrow(() -> new RuntimeException("Зал не найден"));
        for (WorkTime wt : workTimes) {
            wt.setHall(hall);
        }
        workTimeRepository.saveAll(workTimes);
    }

    public boolean validateWorkTimes(List<WorkTimeDto> workTimes) {
        for (WorkTimeDto wt : workTimes) {
            if (wt.getOpenTime() == null || wt.getCloseTime() == null) {
                return false;
            }
            if (!wt.getCloseTime().isAfter(wt.getOpenTime())) {
                return false;
            }
        }
        return true;
    }
}
