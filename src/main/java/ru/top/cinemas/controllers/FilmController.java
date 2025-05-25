package ru.top.cinemas.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.top.cinemas.dtos.FilmDTO;
import ru.top.cinemas.entities.AgeRating;
import ru.top.cinemas.entities.Film;
import ru.top.cinemas.entities.FilmStatus;
import ru.top.cinemas.entities.Genre;
import ru.top.cinemas.mappers.FilmMapper;
import ru.top.cinemas.services.AgeRatingService;
import ru.top.cinemas.services.FilmService;
import ru.top.cinemas.services.GenreService;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/films")
public class FilmController {

    private final FilmService filmService;
    private final AgeRatingService ageRatingService;
    private final GenreService genreService;
    private final FilmMapper filmMapper;


    @GetMapping
    public String listFilms(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Set<Long> genreIds,
            @RequestParam(required = false) Long ageRatingId,
            @RequestParam(required = false) FilmStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model) {
        List<Genre> allGenres = genreService.getAll();
        List<AgeRating> allAgeRating = ageRatingService.getAll();

        // Создаем объект Pageable для пагинации
        Pageable pageable = PageRequest.of(page, size);


        Page<FilmDTO> filmPage = filmService.search(title, year, genreIds, ageRatingId,status,pageable)
                .map(film -> {
                    FilmDTO dto = filmMapper.toDto(film);
                    List<Genre> filmsGenres = allGenres.stream()
                            .filter(g -> dto.getGenreIds().contains(g.getId()))
                            .collect(Collectors.toList());
                    dto.setGenres(filmsGenres);
                    return dto;
                });



        model.addAttribute("films", filmPage);
        model.addAttribute("genres", allGenres);
        model.addAttribute("ageRatings", allAgeRating);
        model.addAttribute("allStatuses", FilmStatus.values());

        // чтобы поля поиска оставались заполненными
        model.addAttribute("searchTitle", title);
        model.addAttribute("searchYear", year);
        model.addAttribute("searchGenreIds", genreIds);
        model.addAttribute("searchAgeRatingId", ageRatingId);
        model.addAttribute("searchStatus", status);


        return "/admin/films/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("filmDTO", new FilmDTO());
        model.addAttribute("genres", genreService.getAll());
        model.addAttribute("ageRatings", ageRatingService.getAll());
        model.addAttribute("allStatuses", FilmStatus.values());
        return "/admin/films/create-edit";
    }

    @PostMapping("/create")
    public String saveFilm(
            @Valid @ModelAttribute("filmDTO") FilmDTO dto,
            BindingResult bindingResult,
            @RequestParam("posterFile") MultipartFile posterFile,
            RedirectAttributes redirectAttributes,
            Model model) throws IOException {

        if (bindingResult.hasErrors()) {
            model.addAttribute("genres", genreService.getAll());
            model.addAttribute("ageRatings", ageRatingService.getAll());
            model.addAttribute("allStatuses", FilmStatus.values());
            return "/admin/films/create-edit";
        }
        try {
            Film film = filmMapper.toEntity(dto);
            filmService.save(film, posterFile);
            redirectAttributes.addFlashAttribute("success", "Фильм добавлен/обновлен");
            return "redirect:/admin/films";
        } catch (Exception e){
            model.addAttribute("genres", genreService.getAll());
            model.addAttribute("ageRatings", ageRatingService.getAll());
            model.addAttribute("allStatuses", FilmStatus.values());
            model.addAttribute("error", e.getMessage());
            return "/admin/films/create-edit";
        }

    }

    @GetMapping("/edit/{id}")
    public String editFilm(@PathVariable Long id, Model model) {
        Film film = filmService.getById(id);
        FilmDTO filmDTO = filmMapper.toDto(film);
        model.addAttribute("filmDTO", filmDTO);
        model.addAttribute("genres", genreService.getAll());
        model.addAttribute("ageRatings", ageRatingService.getAll());
        model.addAttribute("allStatuses", FilmStatus.values());
        return "/admin/films/create-edit";
    }



    @PostMapping("/delete/{id}")
    public String deleteFilm(@PathVariable Long id,RedirectAttributes redirectAttributes) {
       try {
           filmService.delete(id);
           redirectAttributes.addFlashAttribute("success", "Фильм успешно удален");
           return "redirect:/admin/films";
       }catch (Exception e){
           redirectAttributes.addFlashAttribute("error", e.getMessage());
       }
        return "redirect:/admin/films";
    }

}
