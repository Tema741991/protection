package ru.top.cinemas.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class HallWorkTimeDto {
    private List<WorkTimeDto> workTimes = new ArrayList<>();
}
