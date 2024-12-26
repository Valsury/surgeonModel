package com.example.surgeonmodel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/surgeons") // URL для отображения HTML-страницы
public class PageController {
    @GetMapping
    public String getSurgeonsPage() {
        return "surgeons"; // имя HTML-файла без расширения
    }
}
