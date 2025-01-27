package com.example.surgeonmodel.controller;

import com.example.surgeonmodel.model.Appointment;
import com.example.surgeonmodel.model.AppointmentDTO;
import com.example.surgeonmodel.service.AppointmentService;
import com.example.surgeonmodel.service.PatientService;
import com.example.surgeonmodel.service.SurgeonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private SurgeonService surgeonService;


    @PostMapping
    public ResponseEntity<String> createAppointment(@RequestBody Appointment appointment) {

        if (appointment.getPatient() == null || !patientService.existsById(appointment.getPatient().getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Пациент не существует.");
        }


        if (appointment.getSurgeon() == null || !surgeonService.existsById(appointment.getSurgeon().getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Хирург не существует.");
        }


        Appointment savedAppointment = appointmentService.save(appointment);
        return ResponseEntity.status(HttpStatus.CREATED).body("Прием успешно создан с ID: " + savedAppointment.getId());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable Long id) {
        return appointmentService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping
    public List<AppointmentDTO> getAllAppointments() {
        List<Appointment> appointments = appointmentService.findAll();
        return appointments.stream().map(appointment -> {
            AppointmentDTO dto = new AppointmentDTO();
            dto.setId(appointment.getId());
            dto.setAppointmentDateTime(appointment.getAppointmentDateTime());
            dto.setAppointmentType(appointment.getAppointmentType());
            dto.setOperationType(appointment.getOperationType());
            dto.setSurgeonName(appointment.getSurgeon().getFirstName() + " " + appointment.getSurgeon().getLastName());
            dto.setPatientName(appointment.getPatient().getFirstName() + " " + appointment.getPatient().getLastName());
            return dto;
        }).collect(Collectors.toList());
    }
    @PutMapping("/{id}")
    public ResponseEntity<Appointment> updateAppointment(@PathVariable Long id, @RequestBody Appointment appointment) {
        if (!appointmentService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        appointment.setId(id);
        return ResponseEntity.ok(appointmentService.save(appointment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        appointmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}