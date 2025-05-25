package ru.top.cinemas.mappers;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.top.cinemas.dtos.TechnicalBreakDto;
import ru.top.cinemas.entities.Hall;
import ru.top.cinemas.entities.TechnicalBreak;
import ru.top.cinemas.repositories.HallRepository;

@Component
@Data
@RequiredArgsConstructor
public class TechnicalBreakMapper {

    private final HallRepository hallRepository;

    public TechnicalBreakDto toDto(TechnicalBreak entity){
        TechnicalBreakDto dto = new TechnicalBreakDto();
        dto.setId(entity.getId());
        dto.setStartTime(entity.getDate());
        dto.setStart(entity.getStart());
        dto.setEnd(entity.getEnd());
        dto.setHallId(entity.getHall().getId());
        return dto;
    }

    public TechnicalBreak toEntity(TechnicalBreakDto dto){
        Hall hall = hallRepository.findById(dto.getHallId()).orElse(null);
        TechnicalBreak entity = new TechnicalBreak();
        entity.setId(dto.getId());
        entity.setDate(dto.getStartTime());
        entity.setStart(dto.getStart());
        entity.setEnd(dto.getEnd());
        entity.setHall(hall);
        return entity;
    }
}
