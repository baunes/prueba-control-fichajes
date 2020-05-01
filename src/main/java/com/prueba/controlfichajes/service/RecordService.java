package com.prueba.controlfichajes.service;

import com.prueba.controlfichajes.dto.RecordDTO;
import com.prueba.controlfichajes.dto.RecordDTOMapper;
import com.prueba.controlfichajes.model.Record;
import com.prueba.controlfichajes.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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


}
