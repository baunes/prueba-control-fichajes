package com.prueba.controlfichajes.dto;

import com.prueba.controlfichajes.model.Record;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RecordDTOMapper {
    @Mapping(source = "type", target = "serviceType")
    Record toEntity(RecordDTO dto);

    @Mapping(source = "serviceType", target = "type")
    RecordDTO toDto(Record entity);

    List<Record> toEntity(List<RecordDTO> dtoList);

    List<RecordDTO> toDto(List<Record> entityList);
}
