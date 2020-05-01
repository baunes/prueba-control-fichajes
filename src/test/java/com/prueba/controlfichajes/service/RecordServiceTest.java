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
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
public class RecordServiceTest {

    private final AtomicLong counter = new AtomicLong();

    private RecordService service;
    private RecordRepository recordRepository;

    @BeforeEach
    public void setUpTest(@Mock RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
        this.service = new RecordService(recordRepository, new RecordDTOMapperImpl());
    }

    @Test
    public void testCreate() {
        Mockito.when(recordRepository.save(any())).thenAnswer(invocation -> {
            Record record = invocation.getArgument(0);
            record.setId(counter.incrementAndGet());
            return record;
        });

        RecordDTO dto = new RecordDTO(null, "1", ZonedDateTime.parse("2018-01-01T08:00:00.000Z"),
                "02_000064", RecordType.IN, "ATALAYAS", ServiceType.WORK);
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

    @Test
    public void testCreateAll() {
        Mockito.when(recordRepository.saveAll(any())).thenAnswer(invocation -> {
            List<Record> records = invocation.getArgument(0);
            records.forEach(record -> record.setId(counter.incrementAndGet()));
            return records;
        });

        RecordDTO dto1 = new RecordDTO(null, "1", ZonedDateTime.parse("2018-01-01T08:00:00.000Z"),
                "02_000064", RecordType.IN, "ATALAYAS", ServiceType.WORK);
        RecordDTO dto2 = new RecordDTO(null, "1", ZonedDateTime.parse("2018-01-01T13:30:00.000Z"),
                "02_000064", RecordType.OUT, "ATALAYAS", ServiceType.WORK);
        List<RecordDTO> created = service.createAll(Arrays.asList(dto1, dto2));

        assertThat(created).hasSize(2);
        assertThat(created).isNotNull();

        assertThat(created.get(0).getId()).isNotNull();
        assertThat(created.get(0).getBusinessId()).isEqualTo(dto1.getBusinessId());
        assertThat(created.get(0).getDate()).isEqualTo(dto1.getDate());
        assertThat(created.get(0).getEmployeeId()).isEqualTo(dto1.getEmployeeId());
        assertThat(created.get(0).getRecordType()).isEqualTo(dto1.getRecordType());
        assertThat(created.get(0).getServiceId()).isEqualTo(dto1.getServiceId());
        assertThat(created.get(0).getType()).isEqualTo(dto1.getType());

        assertThat(created.get(1).getId()).isNotNull();
        assertThat(created.get(1).getBusinessId()).isEqualTo(dto2.getBusinessId());
        assertThat(created.get(1).getDate()).isEqualTo(dto2.getDate());
        assertThat(created.get(1).getEmployeeId()).isEqualTo(dto2.getEmployeeId());
        assertThat(created.get(1).getRecordType()).isEqualTo(dto2.getRecordType());
        assertThat(created.get(1).getServiceId()).isEqualTo(dto2.getServiceId());
        assertThat(created.get(1).getType()).isEqualTo(dto2.getType());

    }

}
