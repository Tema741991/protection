package ru.top.cinemas.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.top.cinemas.services.BatchSessionsService;
import ru.top.cinemas.services.impl.BatchSessionsServiceImpl;

import java.util.List;

@Controller
@RequestMapping("/admin/sessions")
@RequiredArgsConstructor
public class BatchSessionController {

    private final BatchSessionsService batchSessionsService;

    @PostMapping("/batch")
    public String handleBatchAction(
            @RequestParam("action") String action,
            @RequestParam("sessionIds") List<Long> sessionIds,
            RedirectAttributes redirectAttributes) {
        if (sessionIds == null || sessionIds.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Не выбрано ни одного сеанса");
            return "redirect:/admin/sessions";
        }
        try {
            switch (action) {
                case "publish":
                    batchSessionsService.publishSessions(sessionIds);
                    redirectAttributes.addFlashAttribute("success", "Сеансы успешно опубликованы");
                    break;
                case "delete":
                    batchSessionsService.deleteSessions(sessionIds);
                    redirectAttributes.addFlashAttribute("success", "Сеансы успешно удалены");
                    break;
                case "complete":
                    batchSessionsService.completeSessions(sessionIds);
                    redirectAttributes.addFlashAttribute("success", "Сеансы успешно завершены");
                    break;
                case "cancel":
                    batchSessionsService.cancelSessions(sessionIds);
                    redirectAttributes.addFlashAttribute("success", "Сеансы успешно отменены");
                    break;
                default:
                    redirectAttributes.addFlashAttribute("error", "Неизвестное действие");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/admin/sessions";
    }
}
