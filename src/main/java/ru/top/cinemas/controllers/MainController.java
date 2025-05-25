package ru.top.cinemas.controllers;

import lombok.RequiredArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


import org.thymeleaf.TemplateEngine;


import ru.top.cinemas.dtos.main.DayInfoDto;
import ru.top.cinemas.dtos.main.FilmCardCaruselDto;
import ru.top.cinemas.dtos.main.FilmDetailsDto;
import ru.top.cinemas.dtos.main.FilmSessionDto;
import ru.top.cinemas.services.MainService;
import ru.top.cinemas.services.impl.FilmServiceImpl;


import java.time.LocalDate;
import java.util.List;


@RequiredArgsConstructor
@Controller
@RequestMapping("/")
public class MainController {

    private final FilmServiceImpl filmService;
    private final MainService mainService;


    @GetMapping(value = {"/", "/schedule/{date}"})
    public String home(
            @PathVariable(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model) {


        LocalDate selectedDate = (date != null) ? date : LocalDate.now();

        List<FilmCardCaruselDto> nowShowingFilms = filmService.getNowShowingFilms();
        List<DayInfoDto> daysView = mainService.getDayInfos(LocalDate.now(), 7);

        List<FilmSessionDto> dailySchedule = mainService.getDailySchedule(selectedDate);

        model.addAttribute("featuredFilms", nowShowingFilms);
        model.addAttribute("daysView", daysView);
        model.addAttribute("films", dailySchedule);
        model.addAttribute("selectedDate", selectedDate);

        return "index";
    }


    @GetMapping("/films")
    public String filmsPage(Model model) {
        List<FilmCardCaruselDto> nowShowingFilms = filmService.getNowShowingFilms();
        List<FilmCardCaruselDto> comingSoonFilms = filmService.getComingSoonFilms();

        model.addAttribute("nowShowing", nowShowingFilms);
        model.addAttribute("comingSoon",comingSoonFilms );
        return "/films/films-list";
    }

    @GetMapping("/films/{id}")
    public String filmDetails(@PathVariable Long id, Model model) {
        FilmDetailsDto film = mainService.getFilmById(id);
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(7); // Расписание на неделю

        model.addAttribute("film", film);
        model.addAttribute("schedule", mainService.getFilmSchedule(id, startDate, endDate));
        return "/films/film-details";
    }

    @GetMapping("/contacts")
    public String contactsPage(Model model) {
        return "/contacts";
    }

    @GetMapping("/halls")
    public String HallsPage(Model model) {
        return "/halls";
    }
}
