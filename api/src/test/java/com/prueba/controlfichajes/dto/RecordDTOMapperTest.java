package com.prueba.controlfichajes.dto;

import com.prueba.controlfichajes.model.records.Record;
import com.prueba.controlfichajes.model.records.RecordType;
import com.prueba.controlfichajes.model.records.ServiceType;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RecordDTOMapperTest {

    @Test
    public void testToEntity() {
        RecordDTO dto = new RecordDTO();
        dto.setId(1L);
        dto.setBusinessId("1");
        dto.setDate(ZonedDateTime.parse("2018-01-01T08:00:00.000Z"));
        dto.setEmployeeId("02_000064");
        dto.setRecordType(RecordType.IN);
        dto.setServiceId("ATALAYAS");
        dto.setType(ServiceType.WORK);

        RecordDTOMapper mapper = new RecordDTOMapperImpl();
        Record record = mapper.toEntity(dto);

        assertThat(record.getId()).isEqualTo(dto.getId());
        assertThat(record.getBusinessId()).isEqualTo(dto.getBusinessId());
        assertThat(record.getDate()).isEqualTo(dto.getDate());
        assertThat(record.getEmployeeId()).isEqualTo(dto.getEmployeeId());
        assertThat(record.getRecordType()).isEqualTo(dto.getRecordType());
        assertThat(record.getServiceId()).isEqualTo(dto.getServiceId());
        assertThat(record.getServiceType()).isEqualTo(dto.getType());
    }

    @Test
    public void testToDto() {
        Record record = new Record();
        record.setId(1L);
        record.setBusinessId("1");
        record.setDate(ZonedDateTime.parse("2018-01-01T08:00:00.000Z"));
        record.setEmployeeId("02_000064");
        record.setRecordType(RecordType.IN);
        record.setServiceId("ATALAYAS");
        record.setServiceType(ServiceType.WORK);

        RecordDTOMapper mapper = new RecordDTOMapperImpl();
        RecordDTO dto = mapper.toDto(record);

        assertThat(dto.getId()).isEqualTo(record.getId());
        assertThat(dto.getBusinessId()).isEqualTo(record.getBusinessId());
        assertThat(dto.getDate()).isEqualTo(record.getDate());
        assertThat(dto.getEmployeeId()).isEqualTo(record.getEmployeeId());
        assertThat(dto.getRecordType()).isEqualTo(record.getRecordType());
        assertThat(dto.getServiceId()).isEqualTo(record.getServiceId());
        assertThat(dto.getType()).isEqualTo(record.getServiceType());
    }

    @Test
    public void testToEntityList() {
        RecordDTO dto = new RecordDTO();
        dto.setId(1L);
        dto.setBusinessId("1");
        dto.setDate(ZonedDateTime.parse("2018-01-01T08:00:00.000Z"));
        dto.setEmployeeId("02_000064");
        dto.setRecordType(RecordType.IN);
        dto.setServiceId("ATALAYAS");
        dto.setType(ServiceType.WORK);

        RecordDTOMapper mapper = new RecordDTOMapperImpl();
        List<Record> records = mapper.toEntityList(Arrays.asList(dto));

        assertThat(records).hasSize(1);
        assertThat(records.get(0).getId()).isEqualTo(dto.getId());
        assertThat(records.get(0).getBusinessId()).isEqualTo(dto.getBusinessId());
        assertThat(records.get(0).getDate()).isEqualTo(dto.getDate());
        assertThat(records.get(0).getEmployeeId()).isEqualTo(dto.getEmployeeId());
        assertThat(records.get(0).getRecordType()).isEqualTo(dto.getRecordType());
        assertThat(records.get(0).getServiceId()).isEqualTo(dto.getServiceId());
        assertThat(records.get(0).getServiceType()).isEqualTo(dto.getType());
    }

    @Test
    public void testToDtoList() {
        Record record = new Record();
        record.setId(1L);
        record.setBusinessId("1");
        record.setDate(ZonedDateTime.parse("2018-01-01T08:00:00.000Z"));
        record.setEmployeeId("02_000064");
        record.setRecordType(RecordType.IN);
        record.setServiceId("ATALAYAS");
        record.setServiceType(ServiceType.WORK);

        RecordDTOMapper mapper = new RecordDTOMapperImpl();
        List<RecordDTO> dtos = mapper.toDtoList(Arrays.asList(record));

        assertThat(dtos).hasSize(1);
        assertThat(dtos.get(0).getId()).isEqualTo(record.getId());
        assertThat(dtos.get(0).getBusinessId()).isEqualTo(record.getBusinessId());
        assertThat(dtos.get(0).getDate()).isEqualTo(record.getDate());
        assertThat(dtos.get(0).getEmployeeId()).isEqualTo(record.getEmployeeId());
        assertThat(dtos.get(0).getRecordType()).isEqualTo(record.getRecordType());
        assertThat(dtos.get(0).getServiceId()).isEqualTo(record.getServiceId());
        assertThat(dtos.get(0).getType()).isEqualTo(record.getServiceType());
    }

}
