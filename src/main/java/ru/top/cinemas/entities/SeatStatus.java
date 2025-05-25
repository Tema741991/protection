package ru.top.cinemas.entities;

public enum SeatStatus {
    ACTIVE,     // Доступно для бронирования/покупки
    INACTIVE,   // Отключено (недоступно)
    RESERVED,   // Временно забронировано
    SOLD
}
