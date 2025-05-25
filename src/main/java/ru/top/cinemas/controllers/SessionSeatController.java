package ru.top.cinemas.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.top.cinemas.services.SessionSeatPriceService;
import ru.top.cinemas.services.impl.SessionSeatServiceImpl;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/admin/sessions")
@RequiredArgsConstructor
public class SessionSeatController {

    private final SessionSeatPriceService sessionSeatPriceService;
    private final SessionSeatServiceImpl seatSessionService;


    @PostMapping("/{sessionId}/set-base-price")
    public String setBasePrice(
            @PathVariable Long sessionId,
            @RequestParam BigDecimal price,
            RedirectAttributes redirectAttributes) {

        sessionSeatPriceService.setBasePriceForSession(sessionId, price);
        redirectAttributes.addFlashAttribute("success", "Базовая цена установлена для всех мест");
        return "redirect:/admin/sessions/view/" + sessionId;
    }

    @PostMapping("/{sessionId}/set-price/{seatId}")
    public String setCustomPrice(
            @PathVariable Long sessionId,
            @PathVariable Long seatId,
            @RequestParam BigDecimal price,
            RedirectAttributes redirectAttributes) {

        sessionSeatPriceService.setCustomPrice(seatId, price);
        redirectAttributes.addFlashAttribute("success", "Цена для места обновлена");
        return "redirect:/admin/sessions/view/" + sessionId;
    }

    // Установка цены для конкретного места
    @PostMapping("/{sessionId}/seat/{seatId}/set-price")
    public String setPriceForSeat(
            @PathVariable Long sessionId,
            @PathVariable Long seatId,
            @RequestParam BigDecimal price,
            RedirectAttributes redirectAttributes) {

        sessionSeatPriceService.setPriceForSeat(seatId, price);
        redirectAttributes.addFlashAttribute("success", "Цена места обновлена");
        return "redirect:/admin/sessions/view/" + sessionId;
    }

    // Массовая установка цены для нескольких мест
    @PostMapping("/{sessionId}/seats/set-price")
    public String setPriceForMultipleSeats(
            @PathVariable Long sessionId,
            @RequestParam("seatIds") List<Long> seatIds,
            @RequestParam BigDecimal price,
            RedirectAttributes redirectAttributes) {
        sessionSeatPriceService.setPriceForMultipleSeats(seatIds, price);
        redirectAttributes.addFlashAttribute("success", "Цена установлена для " + seatIds.size() + " мест");
        return "redirect:/admin/sessions/view/" + sessionId;
    }

    @PostMapping("/{sessionId}/seat/{seatId}/reserve")
    public String reserveSeat(@PathVariable Long sessionId,@PathVariable Long seatId) {
        seatSessionService.reserveSeat(seatId);
        return "redirect:/admin/sessions/view/" + sessionId;

    }


    @PostMapping("/{sessionId}/seat/{seatId}/buy")
    public String buySeat(@PathVariable Long sessionId,@PathVariable Long seatId) {
        seatSessionService.buySeat(seatId);
        return "redirect:/admin/sessions/view/" + sessionId;

    }

    @PostMapping("/{sessionId}/seat/{seatId}/toggle-status")
    public String toggleSeatActive(@PathVariable Long sessionId,@PathVariable Long seatId) {
        seatSessionService.toggleActive(seatId);
        return "redirect:/admin/sessions/view/" + sessionId;

    }

}
