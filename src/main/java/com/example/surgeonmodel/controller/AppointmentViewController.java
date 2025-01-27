package com.example.surgeonmodel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppointmentViewController {

    @GetMapping("/appointments")
    public String getAppointmentsPage() {
        return "appointments";
    }
}