package com.prueba.controlfichajes.service;

import com.prueba.controlfichajes.dto.DayAlarmDTO;
import com.prueba.controlfichajes.dto.RecordDTO;
import com.prueba.controlfichajes.model.alarms.AlarmConfiguration;
import com.prueba.controlfichajes.model.alarms.AlarmType;
import com.prueba.controlfichajes.repository.AlarmRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmRepository alarmRepository;

    public Optional<List<DayAlarmDTO>> checkRecordsWithAlarms(List<RecordDTO> records) {
        // TODO Por simplicidad sólo se comprueban alarmas si hay registros, porque el businessId va asociado al businessId
        if (records.isEmpty()) {
            return Optional.empty();
        }
        List<DayAlarmDTO> alarms = new ArrayList<>();
        List<AlarmConfiguration> configurations =
                alarmRepository.findAllByBusinessIdAndActiveIsTrueAndFromDateLessThanEqualAndToDateGreaterThanEqual(
                        records.get(0).getBusinessId(), records.get(0).getDate(), records.get(0).getDate());
        for (AlarmConfiguration configuration : configurations) {
            if (AlarmType.INCOMPLETE.equals(configuration.getType())) {
                checkIncomplete(records).ifPresent(alarms::add);
            }
        }
        return Optional.of(alarms);
    }

    private Optional<DayAlarmDTO> checkIncomplete(List<RecordDTO> records) {
        if (records.size() % 2 == 1) {
            return Optional.of(DayAlarmDTO.builder().type(AlarmType.INCOMPLETE).build());
        } else {
            return Optional.empty();
        }
    }

}
