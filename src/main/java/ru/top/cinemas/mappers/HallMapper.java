package ru.top.cinemas.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.top.cinemas.dtos.HallDto;
import ru.top.cinemas.dtos.SeatDto;
import ru.top.cinemas.entities.Hall;
import ru.top.cinemas.entities.Seat;
import ru.top.cinemas.repositories.HallRepository;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class HallMapper {

    private final HallRepository hallRepository;

    public HallDto toDTO(Hall hall){
        HallDto dto = new HallDto();
        dto.setId(hall.getId());
        dto.setName(hall.getName());
        dto.setNumberOfRows(hall.getNumberOfRows());
        dto.setNumberSeatsOfRows(hall.getNumberSeatsOfRows());
        return dto;
    }


    public Hall toEntity(HallDto dto){
        Hall entity = new Hall();
        entity.setId(dto.getId());
        entity.setNumberOfRows(dto.getNumberOfRows());
        entity.setNumberSeatsOfRows(dto.getNumberSeatsOfRows());
        entity.setName(dto.getName());
        entity.setCapacity(dto.getNumberOfRows()*dto.getNumberSeatsOfRows());
        entity.setDeactivePlaces(0);
        return entity;
    }
}
