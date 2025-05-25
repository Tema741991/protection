package ru.top.cinemas.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import ru.top.cinemas.entities.*;

import ru.top.cinemas.repositories.HallRepository;
import ru.top.cinemas.repositories.SeatSessionRepository;
import ru.top.cinemas.repositories.SessionRepository;
import ru.top.cinemas.services.HallService;
import ru.top.cinemas.services.impl.*;


import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/sessions")
public class SessionController {

    private final SessionServiceImpl sessionServiceImpl;
    private final ScheduleServiceImpl scheduleService;
    private final SessionServiceImpl sessionService;
    private final SessionSeatServiceImpl sessionSeatServiceImpl;
    private final HallService hallService;


    @GetMapping("")
    public String listSessions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "startTime") String sort,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String filmTitle,
            @RequestParam(required = false) Long hallId,
            @RequestParam(required = false) SessionStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sort));
        Page<Session> sessionPage = sessionServiceImpl.findFilteredSessions(filmTitle, hallId, date,status, pageable);
        List<Hall> halls = hallService.getAllHalls();
        model.addAttribute("sessionPage", sessionPage);
        model.addAttribute("halls", halls);
        model.addAttribute("allStatuses", SessionStatus.values());
        return "/admin/sessions/sessions-list";
    }


    @GetMapping("/view/{id}")
    public String viewSession(@PathVariable Long id, Model model) {
        Session session = sessionService.findById(id);
        List<SeatSession> seatSessions = sessionSeatServiceImpl.getSeatsBySessionId(id);

        // Уникальные ряды в отсортированном виде
        List<String> rowLabels = seatSessions.stream()
                .map(seat -> seat.getSeatTemplate().getRowNumber())
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        // Собираем статистику по местам
        Map<String, Long> seatStats = sessionSeatServiceImpl.getSeatStatsForSession(id);
        model.addAttribute("seatStats", seatStats);
        model.addAttribute("sessionItem", session);
        model.addAttribute("seatSessions", seatSessions);
        model.addAttribute("rowLabels", rowLabels);
        return "/admin/sessions/session-view";
    }


    @PostMapping("/delete/{id}")
    public String deleteSession(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            scheduleService.deleteSession(id);
            redirectAttributes.addFlashAttribute("success", "Сеанс успешно удален");
            return "redirect:/admin/sessions";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/sessions";
    }

    @PostMapping("/change-status/{id}/{status}")
    public String changeSessionStatus(@PathVariable Long id,
                                      @PathVariable SessionStatus status,
                                      RedirectAttributes redirectAttributes) {
        try {
            sessionServiceImpl.changeSessionStatus(id, status);
            redirectAttributes.addFlashAttribute("success", "Статус успешно изменен на " + status.getDisplayName());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/sessions";
    }


}


