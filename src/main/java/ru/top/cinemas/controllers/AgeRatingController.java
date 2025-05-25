package ru.top.cinemas.controllers;

import jakarta.validation.Valid;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.top.cinemas.dtos.AgeRatingDto;
import ru.top.cinemas.entities.AgeRating;
import ru.top.cinemas.mappers.AgeRatingMapper;
import ru.top.cinemas.services.AgeRatingService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/age-ratings")
public class AgeRatingController {

    private final AgeRatingService ageRatingService;
    private final AgeRatingMapper ageRatingMapper;

    @GetMapping
    public String listAgeRatings(Model model) {

        List<AgeRatingDto> allAgeRating = ageRatingService.getAll().stream()
                .map(ageRatingMapper::toDto)
                .collect(Collectors.toList());

        model.addAttribute("ageRatings", allAgeRating);
        return "/admin/age-ratings/age-rating-list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("ageRatingDTO", new AgeRatingDto());
        return "/admin/age-ratings/age-rating-create-edit";
    }

    @PostMapping("/create")
    public String saveAgeRating(
            @Valid @ModelAttribute("ageRatingDTO") AgeRatingDto dto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {
        if (bindingResult.hasErrors()) {
            return "/admin/age-ratings/age-rating-create-edit";
        }
        try {
            AgeRating ageRating = ageRatingMapper.toEntity(dto);
            ageRatingService.save(ageRating);
            redirectAttributes.addFlashAttribute("success", "Возрастной рейтинг добавлен/обновлен");
            return "redirect:/admin/age-ratings";
        } catch (Exception e){
            model.addAttribute("error", e.getMessage());
            return "/admin/age-ratings/age-rating-create-edit";
        }

    }

    @GetMapping("/edit/{id}")
    public String editAgeRating(@PathVariable Long id,Model model) throws NotFoundException {
        AgeRating ageRating = ageRatingService.getById(id);
        AgeRatingDto ageRatingDto = ageRatingMapper.toDto(ageRating);
        model.addAttribute("ageRatingDTO", ageRatingDto);
        return "/admin/age-ratings/age-rating-create-edit";
    }


    @PostMapping("/delete/{id}")
    public String deleteAgeRating(@PathVariable Long id,RedirectAttributes redirectAttributes) {
        try {
            ageRatingService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Возрастной рейтинг удален");
        } catch (Exception e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/age-ratings";
    }

    @ResponseBody
    @PostMapping("/create-modal")
    public ResponseEntity<?> saveModalAgeRating(
            @Valid @ModelAttribute("ageRatingDTO") AgeRatingDto dto,
            BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                // Получаем первую ошибку валидации
                String errorMessage = bindingResult.getFieldErrors().stream()
                        .findFirst()
                        .map(FieldError::getDefaultMessage)
                        .orElse("Неверные данные формы");
                return ResponseEntity.badRequest().body(errorMessage);
            }
            AgeRating ageRating = ageRatingMapper.toEntity(dto);
            ageRatingService.save(ageRating);
            return ResponseEntity.ok("Возрастной рейтинг успешно создан");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
