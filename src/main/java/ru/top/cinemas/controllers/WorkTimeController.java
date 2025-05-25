package ru.top.cinemas.controllers;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.top.cinemas.entities.WorkTime;
import ru.top.cinemas.services.impl.WorkTimeServiceImpl;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class WorkTimeController {


    private final WorkTimeServiceImpl workTimeServiceImpl;

    @GetMapping("/{hallId}")
    public String showForm(@PathVariable Long hallId, Model model) {
        List<WorkTime> workTimes = workTimeServiceImpl.getByHall(hallId);
        if (workTimes.isEmpty()) {
            workTimes = Arrays.stream(DayOfWeek.values())
                    .map(day -> {
                        WorkTime wt = new WorkTime();
                        wt.setDayOfWeek(day);
                        wt.setOpenTime(LocalTime.of(10, 0));
                        wt.setCloseTime(LocalTime.of(22, 0));
                        return wt;
                    }).collect(Collectors.toList());
        }
        model.addAttribute("workTimes", workTimes);
        model.addAttribute("hallId", hallId);
        return "/admin/work-time/work-time";
    }

    @PostMapping("/{hallId}")
    public String save(@PathVariable Long hallId, @ModelAttribute("workTimes") WorkTimeFormWrapper wrapper) {
        workTimeServiceImpl.saveAll(hallId, wrapper.getWorkTimes());
        return "redirect:/halls";
    }

    @ModelAttribute("days")
    public DayOfWeek[] getDays() {
        return DayOfWeek.values();
    }

    @Data
    public static class WorkTimeFormWrapper {
        private List<WorkTime> workTimes;
    }
}
