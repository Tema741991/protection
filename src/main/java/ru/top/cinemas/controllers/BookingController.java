package ru.top.cinemas.controllers;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.top.cinemas.entities.*;
import ru.top.cinemas.repositories.BookingRepository;
import ru.top.cinemas.repositories.SessionRepository;
import ru.top.cinemas.services.impl.BookingServiceImpl;
import ru.top.cinemas.services.impl.SessionSeatServiceImpl;
import ru.top.cinemas.services.impl.SessionServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingServiceImpl bookingService;
    private final SessionSeatServiceImpl sessionSeatService;
    private final SessionServiceImpl sessionService;

    @GetMapping("/select/{sessionId}")
    public String selectSeats(@PathVariable Long sessionId,
                              @AuthenticationPrincipal User user,
                              Model model  ){
        Session session = sessionService.findById(sessionId);
        List<SeatSession> seatSessions = sessionSeatService.getSeatsBySessionId(sessionId);

        // Группировка мест по рядам
        Set<String> rowLabels = seatSessions.stream()
                .map(ss -> ss.getSeatTemplate().getRowNumber())
                .collect(Collectors.toSet());
        Map<String, Long> seatStats = sessionSeatService.getSeatStatsForSession(session.getId());
        model.addAttribute("sessionItem", session);
        model.addAttribute("seatSessions", seatSessions);
        model.addAttribute("rowLabels", rowLabels);
        model.addAttribute("seatStats", seatStats);
        model.addAttribute("user", user);
        return "/booking/select-seats";
    }

    @PostMapping("/confirm")
    public String confirmBooking(
            @RequestParam Long sessionId,
            @RequestParam Long seatId,
            @AuthenticationPrincipal User user,
            RedirectAttributes redirectAttributes) {

        Booking booking = bookingService.reserveSeat(user, sessionId, seatId);
        redirectAttributes.addAttribute("bookingId", booking.getId());
        return "redirect:/";
    }

    @GetMapping("/ticket/{bookingId}")
    public String viewTicket(@PathVariable Long bookingId, Model model) {
        Booking booking = bookingService.viewTicket(bookingId);
        model.addAttribute("booking", booking);
        return "/booking/ticket";
    }


}
