package ru.top.cinemas.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.top.cinemas.dtos.HallDto;
import ru.top.cinemas.dtos.HallWorkTimeDto;
import ru.top.cinemas.dtos.WorkTimeDto;
import ru.top.cinemas.entities.AgeRating;
import ru.top.cinemas.entities.Hall;
import ru.top.cinemas.entities.Seat;
import ru.top.cinemas.entities.WorkTime;
import ru.top.cinemas.mappers.HallMapper;
import ru.top.cinemas.mappers.WorkTimeMapper;
import ru.top.cinemas.services.HallService;
import ru.top.cinemas.services.impl.WorkTimeServiceImpl;
import ru.top.cinemas.utils.DayOfWeekConverter;

import java.time.DayOfWeek;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/halls")
public class HallController {

    private final HallService hallService;
    private final WorkTimeServiceImpl workTimeService;
    private final WorkTimeMapper workTimeMapper;
    private final DayOfWeekConverter dayOfWeekConverter;
    private final HallMapper hallMapper;

    @GetMapping
    public String listHalls(Model model) {
        model.addAttribute("title", "Создать зал");
        model.addAttribute("halls", hallService.getAllHalls());
        return "/admin/halls/halls-list";
    }


    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("title", "Создать новый зал");
        model.addAttribute("hall", new HallDto());
        return "/admin/halls/create";
    }

    @PostMapping("/create")
    public String createHall(
            @Valid @ModelAttribute("hall") HallDto hall,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model
            ) {
        if (bindingResult.hasErrors()) {
            return "/admin/halls/create";
        }
        try {
            Hall hallEntity = hallMapper.toEntity(hall);
            hallService.createHallWithSeats(hallEntity);
            redirectAttributes.addFlashAttribute("success", "Зал успешно добавлен");
            return "redirect:/admin/halls";
        } catch (Exception e){
            model.addAttribute("error", e.getMessage());
            return "/admin/halls/create";
        }
    }


    @PostMapping("/edit/{id}")
    public String renameHall(@PathVariable Long id, @RequestParam String newName,
                           Model model) {
        Hall hall = hallService.getHallById(id);
        if (hall != null && newName != null && !newName.trim().isEmpty()) {
            hall.setName(newName.trim());
            hallService.updateHall(hall);
        }

        return "redirect:/admin/halls";
    }

    @PostMapping("/edit/{id}/toggle-active")
    public String toggleHallActiveStatus(@PathVariable Long id,
                                         RedirectAttributes redirectAttributes,
                                         Model model) {
        try {
            hallService.toggleHallActiveStatus(id);
            return "redirect:/admin/halls";
        } catch (Exception e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/halls";
        }
    }

    @GetMapping("/seats/edit/{id}")
    public String editSeats(@PathVariable Long id, Model model) {
        Hall hall = hallService.getHallById(id);
        Seat[][] seatMatrix = hallService.getSeatMatrix(hall);
        model.addAttribute("hall", hall);
        model.addAttribute("seatMatrix", seatMatrix);
        return "/admin/halls/seats-edit";
    }

    @PostMapping("/seats/action")
    public String seatAction(@RequestParam Long hallId,
                                   @RequestParam int row,
                                   @RequestParam int col,
                                   @RequestParam String action) {
        hallService.editSeat(hallId, row, col, action);
        return "redirect:/admin/halls/seats/edit/" + hallId;
    }

    @PostMapping("/delete/{id}")
    public String deleteHall(@PathVariable Long id,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        try {
            hallService.deleteHall(id);
            redirectAttributes.addFlashAttribute("success", "Зал успешно удален");
            return "redirect:/admin/halls";
        } catch (Exception e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/halls";
        }
    }

    // Показать форму расписания
    @GetMapping("/{id}/work-time")
    public String showScheduleForm(@PathVariable Long id, Model model) {
        Hall hall = hallService.getHallById(id);
        List<WorkTime> workTimes = workTimeService.getByHall(hall.getId());
        Map<DayOfWeek, String> russianDayNames = new LinkedHashMap<>();
        for (DayOfWeek day : DayOfWeek.values()) {
            russianDayNames.put(day, dayOfWeekConverter.toRussian(day));
        }

        List<WorkTimeDto> dtos = workTimes.stream()
                .map(workTime->workTimeMapper.toDto(workTime))
                .collect(Collectors.toList());


        model.addAttribute("russianDayNames", russianDayNames);
        model.addAttribute("hall", hall);
        model.addAttribute("form", new HallWorkTimeDto(dtos));
        return "/admin/halls/hall-work-time";
    }

    @PostMapping("/{id}/work-time")
    public String saveSchedule(
            @PathVariable Long id,
            @ModelAttribute("hall") Hall hall,
            @ModelAttribute("form") HallWorkTimeDto form,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("hall", hall);
                   return "/admin/halls/" + id + "/work-time";
        }

        if (!workTimeService.validateWorkTimes(form.getWorkTimes())) {
            model.addAttribute("error", "Некорректное время работы");
            model.addAttribute("hall", hall);
            return "/admin/halls/" + id + "/work-time";
        }

        List<WorkTime> entity= form.getWorkTimes().stream()
                        .map(workTime->workTimeMapper.toEntity(workTime))
                                .collect(Collectors.toList());
        workTimeService.saveAll(hall.getId(), entity);
        return "redirect:/admin/halls/" + id + "/work-time?success";
    }

}
