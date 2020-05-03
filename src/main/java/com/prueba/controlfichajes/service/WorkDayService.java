package com.prueba.controlfichajes.service;

import com.prueba.controlfichajes.dto.RecordDTO;
import com.prueba.controlfichajes.dto.RecordsDayDTO;
import com.prueba.controlfichajes.model.records.RecordType;
import com.prueba.controlfichajes.model.records.ServiceType;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.With;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class WorkDayService {

    /**
     * Calculate work/rest time and check records integrity.
     *
     * @param recordsDayDTO List of records of a day.
     * @throws InvalidRecordsException When the records are not valid for a working day.
     */
    public void fillTimeAndCheckIntegrity(RecordsDayDTO recordsDayDTO) {
        WorkDay workDay = WorkDay.builder().build();
        for (RecordDTO record : recordsDayDTO.getRecords()) {
            if (workDay.getWorkStart() == null) {
                workDay = fromEmptyState(workDay, record);
            } else if (workDay.getRestStart() == null) {
                workDay = fromWorkingState(workDay, record);
            } else if (workDay.getRestStart() != null) {
                workDay = fromRestState(workDay, record);
            } else if (workDay.getWorkStart() != null) {
                throw new InvalidRecordsException(String.format("The record [%s] is not expected", record.getDate()));
            }
        }
        recordsDayDTO.setWorkTime(workDay.getWorkTime().subtract(workDay.getRestTime()));
        recordsDayDTO.setRestTime(workDay.getRestTime());
    }

    private WorkDay fromEmptyState(WorkDay workDay, RecordDTO record) {
        if (Objects.equals(ServiceType.WORK, record.getType()) && Objects.equals(RecordType.IN, record.getRecordType())) {
            return workDay.withWorkStart(record.getDate());
        } else {
            throw new InvalidRecordsException(String.format("First record is not of type %s %s", ServiceType.WORK, RecordType.IN));
        }
    }

    private WorkDay fromWorkingState(WorkDay workDay, RecordDTO record) {
        if (Objects.equals(ServiceType.WORK, record.getType()) && Objects.equals(RecordType.OUT, record.getRecordType())) {
            return workDay
                    .withWorkTime(workDay.workTime.add(calculateDifference(workDay.getWorkStart(), record.getDate())))
                    .withWorkStart(null);
        } else if (Objects.equals(ServiceType.REST, record.getType()) && Objects.equals(RecordType.IN, record.getRecordType())) {
            return workDay.withRestStart(record.getDate());
        } else {
            throw new InvalidRecordsException(String.format("The record [%s] is not expected", record.getDate()));
        }
    }

    private WorkDay fromRestState(WorkDay workDay, RecordDTO record) {
        if (Objects.equals(ServiceType.REST, record.getType()) && Objects.equals(RecordType.OUT, record.getRecordType())) {
            return workDay
                    .withRestTime(workDay.getRestTime().add(calculateDifference(workDay.getRestStart(), record.getDate())))
                    .withRestStart(null);
        } else {
            throw new InvalidRecordsException(String.format("Next record is not of type %s %s", ServiceType.REST, RecordType.OUT));
        }
    }

    private BigDecimal calculateDifference(ZonedDateTime start, ZonedDateTime end) {
        return BigDecimal.valueOf(ChronoUnit.MINUTES.between(start, end)).divide(BigDecimal.valueOf(60));
    }

    @Data
    @Builder
    private static final class WorkDay {
        @With
        private ZonedDateTime workStart;
        @With
        private ZonedDateTime restStart;
        @With
        @Builder.Default
        private BigDecimal restTime = BigDecimal.ZERO;
        @With
        @Builder.Default
        private BigDecimal workTime = BigDecimal.ZERO;
    }

    public static class InvalidRecordsException extends RuntimeException {
        public InvalidRecordsException(String message) {
            super(message);
        }

        public InvalidRecordsException(String message, Throwable cause) {
            super(message, cause);
        }

        public InvalidRecordsException(Throwable cause) {
            super(cause);
        }
    }

}
