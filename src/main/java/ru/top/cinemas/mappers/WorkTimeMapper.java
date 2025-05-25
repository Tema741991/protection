package ru.top.cinemas.mappers;

import org.springframework.stereotype.Controller;
import ru.top.cinemas.dtos.WorkTimeDto;
import ru.top.cinemas.entities.WorkTime;

@Controller
public class WorkTimeMapper {

    public WorkTimeDto toDto(WorkTime entity){
        WorkTimeDto dto = new WorkTimeDto();
        dto.setId(entity.getId());
        dto.setHall(entity.getHall());
        dto.setDayOfWeek(entity.getDayOfWeek());
        dto.setOpenTime(entity.getOpenTime());
        dto.setCloseTime(entity.getCloseTime());
        return dto;
    }

    public WorkTime toEntity(WorkTimeDto dto){
        WorkTime entity = new WorkTime();
        entity.setId(dto.getId());
        entity.setHall(dto.getHall());
        entity.setDayOfWeek(dto.getDayOfWeek());
        entity.setOpenTime(dto.getOpenTime());
        entity.setCloseTime(dto.getCloseTime());
        return entity;
    }
}
