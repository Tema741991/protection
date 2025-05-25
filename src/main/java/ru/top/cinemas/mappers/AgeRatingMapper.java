package ru.top.cinemas.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.top.cinemas.dtos.AgeRatingDto;
import ru.top.cinemas.entities.AgeRating;


@Component
@RequiredArgsConstructor
public class AgeRatingMapper {

    public AgeRatingDto toDto(AgeRating ageRating) {
        if (ageRating == null) {
            return null;
        }
        AgeRatingDto dto = new AgeRatingDto();
        dto.setId(ageRating.getId());
        dto.setCode(ageRating.getCode());
        dto.setName(ageRating.getName());
        dto.setDescription(ageRating.getDescription());
        dto.setMinAge(ageRating.getMinAge());
        return dto;
    }

    public AgeRating toEntity(AgeRatingDto ageRatingDto) {
        if (ageRatingDto == null) {
            return null;
        }
        AgeRating entity = new AgeRating();
        entity.setId(ageRatingDto.getId());
        entity.setCode(ageRatingDto.getCode());
        entity.setName(ageRatingDto.getName());
        entity.setDescription(ageRatingDto.getDescription());
        entity.setMinAge(ageRatingDto.getMinAge());
        return entity;
    }
}
