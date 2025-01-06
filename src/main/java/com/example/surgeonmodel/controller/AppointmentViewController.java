package com.example.surgeonmodel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppointmentViewController {

    @GetMapping("/appointments") // URL для отображения HTML-страницы приемов
    public String getAppointmentsPage() {
        return "appointments"; // Имя вашего HTML-файла appointments.html
    }
}