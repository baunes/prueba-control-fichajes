package com.prueba.controlfichajes.service;

import com.prueba.controlfichajes.dto.RecordDTO;
import com.prueba.controlfichajes.dto.RecordDTOMapperImpl;
import com.prueba.controlfichajes.model.Record;
import com.prueba.controlfichajes.model.RecordType;
import com.prueba.controlfichajes.model.ServiceType;
import com.prueba.controlfichajes.repository.RecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
public class RecordServiceTest {

    private final AtomicLong counter = new AtomicLong();

    private RecordService service;

    @BeforeEach
    public void setUpTest(@Mock RecordRepository recordRepository) {
        this.service = new RecordService(recordRepository, new RecordDTOMapperImpl());
        Mockito.when(recordRepository.save(any())).thenAnswer(invocation -> {
            Record record = invocation.getArgument(0);
            record.setId(counter.incrementAndGet());
            return record;
        });
    }

    @Test
    public void foo() {
        RecordDTO dto = new RecordDTO();
        dto.setBusinessId("1");
        dto.setDate(ZonedDateTime.parse("2018-01-01T08:00:00.000Z"));
        dto.setEmployeeId("02_000064");
        dto.setRecordType(RecordType.IN);
        dto.setServiceId("ATALAYAS");
        dto.setType(ServiceType.WORK);
        RecordDTO created = service.create(dto);

        assertThat(created).isNotNull();
        assertThat(created.getId()).isNotNull();
        assertThat(created.getBusinessId()).isEqualTo(dto.getBusinessId());
        assertThat(created.getDate()).isEqualTo(dto.getDate());
        assertThat(created.getEmployeeId()).isEqualTo(dto.getEmployeeId());
        assertThat(created.getRecordType()).isEqualTo(dto.getRecordType());
        assertThat(created.getServiceId()).isEqualTo(dto.getServiceId());
        assertThat(created.getType()).isEqualTo(dto.getType());
    }
}
