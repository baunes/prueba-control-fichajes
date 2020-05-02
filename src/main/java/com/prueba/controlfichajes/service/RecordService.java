package com.prueba.controlfichajes.service;

import com.prueba.controlfichajes.dto.RecordDTO;
import com.prueba.controlfichajes.dto.RecordDTOMapper;
import com.prueba.controlfichajes.dto.RecordsDayDTO;
import com.prueba.controlfichajes.dto.RecordsRangeDTO;
import com.prueba.controlfichajes.model.ModelUtils;
import com.prueba.controlfichajes.model.records.Record;
import com.prueba.controlfichajes.model.records.RecordType;
import com.prueba.controlfichajes.model.records.ServiceType;
import com.prueba.controlfichajes.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class RecordService {

    private final RecordRepository recordRepository;
    private final RecordDTOMapper recordDTOMapper;
    private final AlarmService alarmService;

    public RecordDTO create(RecordDTO dto) {
        Record record = this.recordDTOMapper.toEntity(dto);
        record = this.recordRepository.save(record);
        return this.recordDTOMapper.toDto(record);
    }

    public List<RecordDTO> createAll(List<RecordDTO> dtos) {
        List<Record> records = this.recordDTOMapper.toEntityList(dtos);
        records = this.recordRepository.saveAll(records);
        return this.recordDTOMapper.toDtoList(records);
    }

    /**
     * Obtiene los registros (y alarmas si las hibiera) para el usuario y las fechas indicadas.
     *
     * @param employeeId Id del empleado.
     * @param fromDate   Fecha inicial.
     * @param toDate     Fecha final.
     * @return DTO con los valores
     */
    public RecordsRangeDTO getRangeRecordsWithAlarms(String employeeId, LocalDate fromDate, LocalDate toDate) {
        ZonedDateTime fromDateZoned = fromDate.atTime(0, 0, 0, 0).atZone(ZoneId.of("Z"));
        ZonedDateTime toDateZoned = toDate.atTime(23, 59, 59, 999).atZone(ZoneId.of("Z"));
        List<Record> records = this.recordRepository.findAllByEmployeeIdAndDateBetweenOrderByDate(employeeId, fromDateZoned, toDateZoned);
        return parseRangeRecords(fromDate, toDate, records);
    }

    private RecordsRangeDTO parseRangeRecords(LocalDate fromDate, LocalDate toDate, List<Record> records) {
        LinkedHashMap<LocalDate, RecordsDayDTO> map = initializeRecordsDayDTO(fromDate, toDate);
        records.stream()
                .map(this.recordDTOMapper::toDto)
                .forEach(record -> map.get(record.getDate().toLocalDate()).getRecords().add(record));
        map.forEach((date, day) -> fillTimeAndAlarms(day));
        return RecordsRangeDTO.builder().days(new ArrayList<>(map.values())).build();
    }

    private LinkedHashMap<LocalDate, RecordsDayDTO> initializeRecordsDayDTO(LocalDate fromDate, LocalDate toDate) {
        LinkedHashMap<LocalDate, RecordsDayDTO> map = new LinkedHashMap<>();
        for (LocalDate currentDate = fromDate; currentDate.compareTo(toDate) <= 0; currentDate = currentDate.plusDays(1)) {
            map.put(currentDate, initializeDayRecordsDTO(currentDate));
        }
        return map;
    }

    private RecordsDayDTO initializeDayRecordsDTO(LocalDate date) {
        return RecordsDayDTO.builder()
                .date(date)
                .dayType(ModelUtils.fromDayOfWeek(date.getDayOfWeek()))
                .records(new ArrayList<>())
                .build();
    }

    private void fillTimeAndAlarms(RecordsDayDTO day) {
        fillTime(day);
        alarmService.checkRecordsWithAlarms(day).ifPresent(day.getAlarms()::addAll);
    }

    private void fillTime(RecordsDayDTO recordsDayDTO) {
        ZonedDateTime workStart = null;
        ZonedDateTime restStart = null;
        BigDecimal workTime = BigDecimal.ZERO;
        BigDecimal restTime = BigDecimal.ZERO;
        for (RecordDTO record : recordsDayDTO.getRecords()) {
            if (workStart == null) {
                // TODO Buscamos entrada normal
                if (Objects.equals(ServiceType.WORK, record.getType()) && Objects.equals(RecordType.IN, record.getRecordType())) {
                    workStart = record.getDate();
                } else {
                    throw new RuntimeException("There is no previous entrance"); // TODO Refactorizar
                }
            } else if (restStart == null) {
                // TODO Buscamos salida normal o descanso
                if (Objects.equals(ServiceType.WORK, record.getType()) && Objects.equals(RecordType.OUT, record.getRecordType())) {
                    workTime = workTime.add(calculateDifference(workStart, record.getDate()));
                    workStart = null;
                } else if (Objects.equals(ServiceType.REST, record.getType()) && Objects.equals(RecordType.IN, record.getRecordType())) {
                    restStart = record.getDate();
                } else {
                    throw new RuntimeException("There is logical step after normal entrance"); // TODO Refactorizar
                }
            } else if (restStart != null) {
                // Buscamos salida descanso
                if (Objects.equals(ServiceType.REST, record.getType()) && Objects.equals(RecordType.OUT, record.getRecordType())) {
                    restTime = restTime.add(calculateDifference(restStart, record.getDate()));
                    restStart = null;
                } else {
                    throw new RuntimeException("There is no rest exit"); // TODO Refactorizar
                }
            } else if (workStart != null) {
                throw new RuntimeException("Error"); // TODO Refactorizar
            }
        }
        recordsDayDTO.setWorkTime(workTime.subtract(restTime));
        recordsDayDTO.setRestTime(restTime);
    }

    private BigDecimal calculateDifference(ZonedDateTime start, ZonedDateTime end) {
        return BigDecimal.valueOf(ChronoUnit.MINUTES.between(start, end)).divide(BigDecimal.valueOf(60));
    }


}
