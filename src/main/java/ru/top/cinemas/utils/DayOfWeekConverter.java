package ru.top.cinemas.utils;

import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.util.EnumMap;
import java.util.Map;

@Component
public class DayOfWeekConverter {
    private static final Map<DayOfWeek, String> RUSSIAN_NAMES = new EnumMap<>(DayOfWeek.class);

    static {
        RUSSIAN_NAMES.put(DayOfWeek.MONDAY, "Понедельник");
        RUSSIAN_NAMES.put(DayOfWeek.TUESDAY, "Вторник");
        RUSSIAN_NAMES.put(DayOfWeek.WEDNESDAY, "Среда");
        RUSSIAN_NAMES.put(DayOfWeek.THURSDAY, "Четверг");
        RUSSIAN_NAMES.put(DayOfWeek.FRIDAY, "Пятница");
        RUSSIAN_NAMES.put(DayOfWeek.SATURDAY, "Суббота");
        RUSSIAN_NAMES.put(DayOfWeek.SUNDAY, "Воскресенье");
    }

    public String toRussian(DayOfWeek day) {
        return RUSSIAN_NAMES.get(day);
    }
}
