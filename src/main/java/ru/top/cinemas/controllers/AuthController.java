package ru.top.cinemas.controllers;


import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.top.cinemas.dtos.AuthUserDTO;
import ru.top.cinemas.dtos.RegisterUserDTO;
import ru.top.cinemas.services.AuthService;

@Controller

@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @GetMapping("auth/login")
    public String login(HttpSession session, Model model) {
        model.addAttribute("user", new AuthUserDTO());
        return "auth/login";
    }


    @PostMapping("auth/login")
    public String login(HttpSession session, @RequestParam String username,
                        @RequestParam String password,
                        RedirectAttributes redirectAttributes) {
        return "redirect:user/index";
    }


    @GetMapping("auth/register")
    public String register(Model model) {
        model.addAttribute("user", new RegisterUserDTO());
        return "/auth/register";
    }

    @PostMapping("auth/register")
    public String register(@Valid @ModelAttribute("user") RegisterUserDTO user,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes,
                           Model model) {

        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        try {
            authService.registerUser(user);
            redirectAttributes.addFlashAttribute("success", "Регистрация прошла успешно!");
            return "redirect:/auth/login";
        } catch (Exception e){
            model.addAttribute("error", e.getMessage());
            return "auth/register";
        }

    }

}
