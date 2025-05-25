package ru.top.cinemas.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.top.cinemas.dtos.Schedule.CreateSessionRequest;
import ru.top.cinemas.dtos.Schedule.TimeGridResponse;
import ru.top.cinemas.entities.Film;
import ru.top.cinemas.entities.FilmStatus;
import ru.top.cinemas.entities.Hall;
import ru.top.cinemas.entities.Session;
import ru.top.cinemas.services.impl.FilmServiceImpl;
import ru.top.cinemas.services.impl.HallServiceImpl;
import ru.top.cinemas.services.impl.ScheduleServiceImpl;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final HallServiceImpl hallService;
    private final FilmServiceImpl filmService;
    private final ScheduleServiceImpl scheduleService;

    @GetMapping
    public String showSchedulePage(Model model) {
        List<Hall> halls = hallService.getAllHallsByStatus(true);
        List<Film> films = filmService.getAllFilmsByStatuses(List.of(FilmStatus.NOW,FilmStatus.SOON));
        model.addAttribute("halls",halls);
        model.addAttribute("films", films);
        return "admin/sessions/sessions-schedule";
    }

    @ResponseBody
    @GetMapping("/grid")
    public ResponseEntity<TimeGridResponse> getTimeGrid(
            @RequestParam Long hallId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(defaultValue = "5") int stepMinutes) {

        return ResponseEntity.ok(scheduleService.generateTimeGrid(hallId, date, stepMinutes));
    }

    @ResponseBody
    @PostMapping("/add")
    public ResponseEntity<?> createSession(@RequestBody CreateSessionRequest request){
        try {
            Session session = scheduleService.createSession(
                    request.getFilmId(),
                    request.getHallId(),
                    request.getDate().atTime(request.getStartTime())
            );
            return ResponseEntity.ok(scheduleService.mapToSessionDtos(session));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{sessionId}")
    public ResponseEntity<?> deleteSession(@PathVariable Long sessionId) {
        try {
            scheduleService.deleteSession(sessionId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



}
