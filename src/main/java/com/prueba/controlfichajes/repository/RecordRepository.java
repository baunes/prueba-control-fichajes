package com.prueba.controlfichajes.repository;

import com.prueba.controlfichajes.model.Record;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordRepository extends JpaRepository<Record, Long> {
}
