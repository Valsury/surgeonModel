package com.example.surgeonmodel.controller;

import com.example.surgeonmodel.model.Surgeon;
import com.example.surgeonmodel.service.SurgeonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/surgeons")
public class SurgeonController {
    @Autowired
    private SurgeonService surgeonService;

    @GetMapping
    public List<Surgeon> getAllSurgeons() {
        return surgeonService.findAll();
    }

    @PostMapping
    public Surgeon createSurgeon(@RequestBody Surgeon surgeon) {
        return surgeonService.save(surgeon);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Surgeon> getSurgeonById(@PathVariable Long id) {
        return surgeonService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Surgeon> updateSurgeon(@PathVariable Long id, @RequestBody Surgeon surgeon) {
        if (!surgeonService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        surgeon.setId(id);
        return ResponseEntity.ok(surgeonService.save(surgeon));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSurgeon(@PathVariable Long id) {
        surgeonService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

