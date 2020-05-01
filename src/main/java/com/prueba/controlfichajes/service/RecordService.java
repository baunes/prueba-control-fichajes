package com.prueba.controlfichajes.service;

import com.prueba.controlfichajes.dto.RecordDTO;
import com.prueba.controlfichajes.dto.RecordDTOMapper;
import com.prueba.controlfichajes.dto.WeekRecordsDTO;
import com.prueba.controlfichajes.model.Record;
import com.prueba.controlfichajes.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class RecordService {

    private final RecordRepository recordRepository;
    private final RecordDTOMapper recordDTOMapper;

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

    public WeekRecordsDTO getWeekRecords(String employeeId, LocalDate fromDate, LocalDate toDate) {
        ZonedDateTime fromDateZoned = fromDate.atTime(0, 0, 0, 0).atZone(ZoneId.of("Z"));
        ZonedDateTime toDateZoned = toDate.atTime(23, 59, 59, 999).atZone(ZoneId.of("Z"));
        List<Record> records = this.recordRepository.findAllByEmployeeIdAndDateBetweenOrderByDate(employeeId, fromDateZoned, toDateZoned);
        return parseRecords(records);
    }

    private WeekRecordsDTO parseRecords(List<Record> records) {
        WeekRecordsDTO.WeekRecordsDTOBuilder builder = WeekRecordsDTO.builder();
        for (Record record : records) {
            switch (record.getDate().getDayOfWeek()) {
                case MONDAY:
                    builder.monday(this.recordDTOMapper.toDto(record));
                    break;
                case TUESDAY:
                    builder.tuesday(this.recordDTOMapper.toDto(record));
                    break;
                case WEDNESDAY:
                    builder.wednesday(this.recordDTOMapper.toDto(record));
                    break;
                case THURSDAY:
                    builder.thursday(this.recordDTOMapper.toDto(record));
                    break;
                case FRIDAY:
                    builder.friday(this.recordDTOMapper.toDto(record));
                    break;
                case SATURDAY:
                    builder.saturday(this.recordDTOMapper.toDto(record));
                    break;
                case SUNDAY:
                    builder.sunday(this.recordDTOMapper.toDto(record));
                    break;
            }
        }
        return builder.build();
    }


}
