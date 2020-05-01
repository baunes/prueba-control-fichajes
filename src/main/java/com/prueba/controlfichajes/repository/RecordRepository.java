package com.prueba.controlfichajes.repository;

import com.prueba.controlfichajes.model.Record;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.ZonedDateTime;
import java.util.List;

public interface RecordRepository extends JpaRepository<Record, Long> {

    List<Record> findAllByEmployeeIdAndDateBetweenOrderByDate(String employeeId, ZonedDateTime fromDate, ZonedDateTime toDate);

}
