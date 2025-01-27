package com.example.surgeonmodel.controller;

import com.example.surgeonmodel.model.Appointment;
import com.example.surgeonmodel.service.AppointmentService;
import com.example.surgeonmodel.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pdf")
public class PdfController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private PdfService pdfService;


    @GetMapping("/appointments/{id}")
    public ResponseEntity<byte[]> generatePdf(@PathVariable Long id) {
        Appointment appointment = appointmentService.findById(id)
                .orElseThrow(() -> new RuntimeException("Прием не найден"));
        try {

            byte[] pdfBytes = pdfService.generatePdf(appointment);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "appointment_" + id + ".pdf");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка при генерации PDF: " + e.getMessage(), e);
        }
    }
}