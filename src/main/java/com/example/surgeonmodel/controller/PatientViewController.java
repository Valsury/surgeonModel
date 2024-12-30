package com.example.surgeonmodel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PatientViewController {
    @GetMapping("/patients")
    public String getPatientsPage() {
        return "patients"; // Это имя вашего HTML-файла patients.html
    }
}