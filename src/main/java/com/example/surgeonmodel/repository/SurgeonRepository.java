package com.example.surgeonmodel.repository;

import com.example.surgeonmodel.model.Surgeon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurgeonRepository extends JpaRepository<Surgeon, Long> {
}