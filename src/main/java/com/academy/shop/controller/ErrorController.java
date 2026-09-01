package com.academy.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class ErrorController {

    @GetMapping("/403")
    public String forbidden(Model model) {
        model.addAttribute("status", 403);
        model.addAttribute("error", "Доступ запрещён");
        model.addAttribute(
                "message",
                "У вас недостаточно прав для просмотра этой страницы"
        );

        return "error";
    }
}
