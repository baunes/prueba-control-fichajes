package com.prueba.controlfichajes.service;

import com.prueba.controlfichajes.dto.*;
import com.prueba.controlfichajes.model.ModelUtils;
import com.prueba.controlfichajes.model.alarms.AlarmType;
import com.prueba.controlfichajes.model.records.Record;
import com.prueba.controlfichajes.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class RecordService {

    private final RecordRepository recordRepository;
    private final RecordDTOMapper recordDTOMapper;
    private final AlarmService alarmService;
    private final WorkDayService workDayService;

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
        try {
            workDayService.fillTimeAndCheckIntegrity(day);
            alarmService.checkRecordsWithAlarms(day).ifPresent(day.getAlarms()::addAll);
        } catch (WorkDayService.InvalidRecordsException e) {
            day.getAlarms().add(DayAlarmDTO.builder().type(AlarmType.INTEGRITY).build());
        }
    }
}
