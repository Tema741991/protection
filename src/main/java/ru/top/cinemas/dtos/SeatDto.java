package ru.top.cinemas.dtos;

import lombok.Data;

@Data
public class SeatDto {
    private Long id;
    private int row;
    private int column;
    private Long hallId;
    private boolean active;
    private boolean booked;
}
