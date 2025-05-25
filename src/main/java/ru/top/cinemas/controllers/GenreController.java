package ru.top.cinemas.controllers;

import jakarta.validation.Valid;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.top.cinemas.dtos.AgeRatingDto;
import ru.top.cinemas.dtos.GenreDto;
import ru.top.cinemas.entities.AgeRating;
import ru.top.cinemas.entities.Genre;
import ru.top.cinemas.mappers.GenreMapper;
import ru.top.cinemas.services.GenreService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/genres")
public class GenreController {

    private final GenreService genreService;
    private final GenreMapper genreMapper;

    @GetMapping
    public String listGenre(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model) {
        // Создаем объект Pageable для пагинации
        Pageable pageable = PageRequest.of(page, size);
        Page<GenreDto> genrePage = genreService.search(name, description,pageable)
                .map(genreMapper::toDto);

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", genrePage.getTotalPages());
        model.addAttribute("totalItems", genrePage.getTotalElements());
        model.addAttribute("pageSize", size);

        model.addAttribute("genres", genrePage);

        // чтобы поля поиска оставались заполненными
        model.addAttribute("searchName", name);
        model.addAttribute("searchDescription", description);
        return "/admin/genres/genre-list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("genreDTO", new GenreDto());
        return "/admin/genres/genre-create-edit";
    }

    @PostMapping("/create")
    public String saveGenre(
            @Valid @ModelAttribute("genreDTO") GenreDto dto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) throws Exception {

        if (bindingResult.hasErrors()) {
            return "/admin/genres/genre-create-edit";
        }
        try {
            Genre genre = genreMapper.toEntity(dto);
            genreService.save(genre);
            redirectAttributes.addFlashAttribute("success", "Жанр добавлен / обновлен");
            return "redirect:/admin/genres";
        } catch (Exception e){
            model.addAttribute("error", e.getMessage());
            return "/admin/genres/genre-create-edit";
        }
    }

    @GetMapping("/edit/{id}")
    public String editGenre(@PathVariable Long id,Model model) throws NotFoundException {
        Genre genre = genreService.getById(id);
        GenreDto genreDto = genreMapper.toDto(genre);
              model.addAttribute("genreDTO", genreDto);

        return "/admin/genres/genre-create-edit";
    }


    @PostMapping("/delete/{id}")
    public String deleteGenre(@PathVariable Long id,RedirectAttributes redirectAttributes) {

        try {
            genreService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Жанр удален");
        } catch (Exception e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/genres";
    }

    @ResponseBody
    @PostMapping("/create-modal")
    public ResponseEntity<?> saveModalGenre(@Valid @ModelAttribute("genreDTO") GenreDto dto,
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
            Genre genre = genreMapper.toEntity(dto);
            genreService.save(genre);
            return ResponseEntity.ok("Жанр успешно создан");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
