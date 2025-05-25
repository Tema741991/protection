package ru.top.cinemas.mappers;

import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.top.cinemas.dtos.GenreDto;
import ru.top.cinemas.entities.Genre;

@Component
@RequiredArgsConstructor
public class GenreMapper {

    public GenreDto toDto(Genre genre) {
        if (genre == null) {
            return null;
        }
        GenreDto dto=new GenreDto();
        dto.setId(genre.getId());
        dto.setName(genre.getName());
        dto.setDescription(genre.getDescription());
        return dto;
    }

    public Genre toEntity(GenreDto genreDto) {
        if (genreDto == null) {
            return null;
        }
        Genre entity = new Genre();
        entity.setId(genreDto.getId());
        entity.setName(genreDto.getName());
        entity.setDescription(genreDto.getDescription());
        return entity;
    }
}
