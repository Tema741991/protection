package ru.top.cinemas.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.top.cinemas.entities.Booking;
import ru.top.cinemas.entities.BookingStatus;
import ru.top.cinemas.entities.User;
import ru.top.cinemas.services.CustomUserDetailsService;
import ru.top.cinemas.services.impl.BookingServiceImpl;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {


    private final BookingServiceImpl bookingService;
    private final CustomUserDetailsService userDetailsService;

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal User user,
                          @RequestParam(required = false) String tab,
                          @RequestParam(required = false) String status,
                          Model model) {

        String activeTab = (tab != null && tab.equals("tickets")) ? "tickets" : "bookings";
        model.addAttribute("activeTab", activeTab);
        model.addAttribute("user", user);

        List<Booking> items;
        List<BookingStatus> availableStatuses;

        if ("tickets".equals(activeTab)) {
            availableStatuses = List.of(BookingStatus.SOLD, BookingStatus.RETURNED);
            items = status != null
                    ? bookingService.findAllByUserIdAndAndStatus(user.getId(), BookingStatus.valueOf(status))
                    : bookingService.findAllByUserIdAndAndStatusIn(user.getId(), availableStatuses);
        } else {
            availableStatuses = List.of(BookingStatus.RESERVED, BookingStatus.CANCELLED);
            items = status != null
                    ? bookingService.findAllByUserIdAndAndStatus(user.getId(), BookingStatus.valueOf(status))
                    : bookingService.findAllByUserIdAndAndStatusIn(user.getId(),availableStatuses);
        }

        model.addAttribute("availableStatuses", availableStatuses);
        model.addAttribute("items", items);
        model.addAttribute("status", status);

        return "user/profile";
    }

    @PostMapping("/bookings/{id}/cancel")
    public String cancelBooking(@PathVariable Long id,
                                @RequestParam(required = true) Long seassionId,
                                @RequestParam(required = true) Long seatId,
                                @AuthenticationPrincipal User user,
                                RedirectAttributes redirectAttributes) {
        try {
            bookingService.cancelReservation(id, seassionId,seatId);
            redirectAttributes.addFlashAttribute("successMessage", "Бронирование успешно отменено");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Не удалось отменить бронирование");
        }
        return "redirect:/user/profile";
    }

    @PostMapping("/bookings/{id}/buy")
    public String Sold(@PathVariable Long id,
                       @RequestParam(required = true) Long seassionId,
                       @RequestParam(required = true) Long seatId,
                       @AuthenticationPrincipal User user,
                       RedirectAttributes redirectAttributes) {
        try {
            bookingService.buySeatLater(id,seassionId, seatId);
            redirectAttributes.addFlashAttribute("successMessage", "Билет успешно продан");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Не удалось продать билет");
        }
        return "redirect:/user/profile?tab=tickets";
    }

    @PostMapping("/bookings/{id}/refund")
    public String cancelSold(@PathVariable Long id,
                             @RequestParam(required = true) Long seassionId,
                             @RequestParam(required = true) Long seatId,
                             @AuthenticationPrincipal User user,
                             RedirectAttributes redirectAttributes) {
        try {
            bookingService.cancelSold(id, seassionId,seatId);
            redirectAttributes.addFlashAttribute("successMessage", "Билет успешно возвращен");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Не удалось вернуть билет");
        }
        return "redirect:/user/profile?tab=tickets";
    }


    @GetMapping("/settings")
    public String settingsForm(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "user/settings";
    }
    @PostMapping("/settings")
    public String updateSettings(
            @AuthenticationPrincipal User user,
            @RequestParam String name,
            @RequestParam String email,
            RedirectAttributes redirectAttributes) throws IOException {

        userDetailsService.updateProfile(user, name, email);
        redirectAttributes.addFlashAttribute("success", "Профиль успешно обновлён");
        return "redirect:/user/settings";
    }

    @PostMapping("/change-password")
    public String changePassword(
            @AuthenticationPrincipal User user,
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            RedirectAttributes redirectAttributes) {

        try {
            userDetailsService.changePassword(user, currentPassword, newPassword ,confirmPassword);
            redirectAttributes.addFlashAttribute("success", "Пароль успешно изменён");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/user/settings";
    }
}
