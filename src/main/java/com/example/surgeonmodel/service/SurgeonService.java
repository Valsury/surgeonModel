package com.example.surgeonmodel.service;

import com.example.surgeonmodel.model.Surgeon;
import com.example.surgeonmodel.repository.SurgeonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SurgeonService {
    @Autowired
    private SurgeonRepository surgeonRepository;

    public List<Surgeon> findAll() {
        return surgeonRepository.findAll();
    }

    public Optional<Surgeon> findById(Long id) {
        return surgeonRepository.findById(id);
    }

    public Surgeon save(Surgeon surgeon) {
        return surgeonRepository.save(surgeon);
    }

    public void delete(Long id) {
        surgeonRepository.deleteById(id);
    }
}