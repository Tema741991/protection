package ru.top.cinemas.dtos.main;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
@Data
@AllArgsConstructor
public class DayInfoDto {
    private  LocalDate date;
    private   String dayName;
    private   String formattedDate;
    private boolean isActive;
}
