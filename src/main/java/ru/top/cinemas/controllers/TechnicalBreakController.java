package ru.top.cinemas.controllers;

import jakarta.validation.Valid;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.top.cinemas.dtos.AgeRatingDto;
import ru.top.cinemas.dtos.TechnicalBreakDto;
import ru.top.cinemas.entities.AgeRating;
import ru.top.cinemas.entities.Hall;
import ru.top.cinemas.entities.SessionStatus;
import ru.top.cinemas.entities.TechnicalBreak;
import ru.top.cinemas.mappers.TechnicalBreakMapper;
import ru.top.cinemas.repositories.HallRepository;
import ru.top.cinemas.repositories.TechnicalBreakRepository;
import ru.top.cinemas.services.impl.HallServiceImpl;
import ru.top.cinemas.services.impl.TechnicalBreakServiceImpl;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class TechnicalBreakController {

    private final TechnicalBreakServiceImpl technicalBreakService;
    private final HallServiceImpl hallService;
    private final TechnicalBreakMapper technicalBreakMapper;

    @GetMapping("/breaks")
    public String showTechnicalBreaksList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "date") String sort,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) Long hallId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sort));
        Page<TechnicalBreak> technicalBreaksPage = technicalBreakService.findFilteredTechnicalBreaks( hallId, date, pageable);
        List<Hall> halls = hallService.getAllHalls();
        model.addAttribute("breaksPage", technicalBreaksPage);
        model.addAttribute("halls", halls);
        return "/admin/breaks/breaks-list";
    }

    @GetMapping("/breaks/create")
    public String showCreateForm(Model model) {
        List<Hall> halls = hallService.getAllHalls();
        model.addAttribute("title", "Создать технический перерыв");
        model.addAttribute("halls", halls);
        model.addAttribute("technicalBreak", new TechnicalBreakDto());
        return "/admin/breaks/break-create";
    }

    @PostMapping("/breaks/create")
    public String createTechnicalBreak(
            @Valid @ModelAttribute("technicalBreak") TechnicalBreakDto technicalBreak,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (bindingResult.hasErrors()) {
            List<Hall> halls = hallService.getAllHalls();
            model.addAttribute("title", "Создать технический перерыв");
            model.addAttribute("halls", halls);
            model.addAttribute("technicalBreak",technicalBreak);
            return "/admin/breaks/break-create";
        }
        try {
            TechnicalBreak technicalBreakDtoEntity=technicalBreakMapper.toEntity(technicalBreak);
            technicalBreakService.createTechnicalBreak(technicalBreakDtoEntity);
            redirectAttributes.addFlashAttribute("success", "Технический перерыв добавлен/обновлен");
            return "redirect:/admin/breaks";
        } catch (Exception e){
            List<Hall> halls = hallService.getAllHalls();
            model.addAttribute("error", e.getMessage());
            model.addAttribute("title", "Создать технический перерыв");
            model.addAttribute("halls", halls);
            model.addAttribute("technicalBreak",technicalBreak);
            return "/admin/breaks/break-create";
        }
    }


    @GetMapping("/breaks/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) throws NotFoundException {
        TechnicalBreak technicalBreak = technicalBreakService.findById(id);
        TechnicalBreakDto technicalBreakDto=technicalBreakMapper.toDto(technicalBreak);
        List<Hall> halls = hallService.getAllHalls();
        model.addAttribute("technicalBreak", technicalBreakDto);
        model.addAttribute("halls", halls);
        model.addAttribute("title", "Редактировать технический перерыв");
        model.addAttribute("actionUrl", "/admin/breaks/create");
        return "/admin/breaks/break-create";
    }


    @PostMapping("/breaks/delete/{id}")
    public String deleteBreak(@PathVariable Long id, Model model) {
        technicalBreakService.deleteTechnicalBrake(id);
        return "redirect:/admin/breaks";
    }

    @ResponseBody
    @PostMapping("/breaks/create-modal")
    public ResponseEntity<?> saveModalTechnicalBreak(
            @RequestBody TechnicalBreakDto technicalBreak,
            BindingResult bindingResult){
        try {
            if (bindingResult.hasErrors()) {
                // Получаем первую ошибку валидации
                String errorMessage = bindingResult.getFieldErrors().stream()
                        .findFirst()
                        .map(FieldError::getDefaultMessage)
                        .orElse("Неверные данные формы");
                return ResponseEntity.badRequest().body(errorMessage);
            }
            TechnicalBreak technicalBreakDtoEntity=technicalBreakMapper.toEntity(technicalBreak);
            technicalBreakService.createTechnicalBreak(technicalBreakDtoEntity);
            return ResponseEntity.ok("Технический перерыв успешно создан");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
