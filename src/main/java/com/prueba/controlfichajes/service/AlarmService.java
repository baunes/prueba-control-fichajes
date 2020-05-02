package com.prueba.controlfichajes.service;

import com.prueba.controlfichajes.dto.DayAlarmDTO;
import com.prueba.controlfichajes.dto.RecordDTO;
import com.prueba.controlfichajes.dto.RecordsDayDTO;
import com.prueba.controlfichajes.model.ModelUtils;
import com.prueba.controlfichajes.model.alarms.AlarmConfiguration;
import com.prueba.controlfichajes.model.alarms.AlarmConfigurationDays;
import com.prueba.controlfichajes.model.alarms.AlarmType;
import com.prueba.controlfichajes.model.alarms.DayType;
import com.prueba.controlfichajes.repository.AlarmRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmRepository alarmRepository;

    public Optional<List<DayAlarmDTO>> checkRecordsWithAlarms(RecordsDayDTO recordsDayDTO) {
        // TODO Por simplicidad sólo se comprueban alarmas si hay registros, porque el businessId va asociado al businessId
        if (recordsDayDTO == null || recordsDayDTO.getRecords().isEmpty()) {
            return Optional.empty();
        }
        List<DayAlarmDTO> alarms = new ArrayList<>();
        List<RecordDTO> records = recordsDayDTO.getRecords();
        List<AlarmConfiguration> configurations =
                alarmRepository.findAllByBusinessIdAndActiveIsTrueAndFromDateLessThanEqualAndToDateGreaterThanEqual(
                        records.get(0).getBusinessId(), records.get(0).getDate(), records.get(0).getDate());
        for (AlarmConfiguration configuration : configurations) {
            Map<DayType, AlarmConfigurationDays> days = toDaysMap(configuration.getDays());
            if (AlarmType.INCOMPLETE.equals(configuration.getType())) {
                checkIncomplete(recordsDayDTO, days).ifPresent(alarms::add);
            } else if (AlarmType.MAX_WORKING_HOURS.equals(configuration.getType())) {
                checkMaxWorkingHours(recordsDayDTO, days).ifPresent(alarms::add);
            } else if (AlarmType.MIN_ENTRY_TIME.equals(configuration.getType())) {
                checkMinEntryTime(recordsDayDTO, days).ifPresent(alarms::add);
            }
        }
        return Optional.of(alarms);
    }

    private Map<DayType, AlarmConfigurationDays> toDaysMap(List<AlarmConfigurationDays> days) {
        return days.stream().collect(Collectors.toMap(AlarmConfigurationDays::getDayWeek, alarmConfigurationDays -> alarmConfigurationDays));
    }

    private Optional<DayAlarmDTO> checkIncomplete(RecordsDayDTO recordsDayDTO, Map<DayType, AlarmConfigurationDays> days) {
        if (days.containsKey(ModelUtils.fromDayOfWeek(recordsDayDTO.getRecords().get(0).getDate().getDayOfWeek())) && recordsDayDTO.getRecords().size() % 2 == 1) {
            return Optional.of(DayAlarmDTO.builder().type(AlarmType.INCOMPLETE).build());
        } else {
            return Optional.empty();
        }
    }

    private Optional<DayAlarmDTO> checkMaxWorkingHours(RecordsDayDTO recordsDayDTO, Map<DayType, AlarmConfigurationDays> days) {
        if (days.containsKey(recordsDayDTO.getDayType())) {
            AlarmConfigurationDays conf = days.get(recordsDayDTO.getDayType());
            if (recordsDayDTO.getWorkTime().compareTo(conf.getValueN()) > 0) {
                return Optional.of(DayAlarmDTO.builder().type(AlarmType.MAX_WORKING_HOURS).build());
            }
        }
        return Optional.empty();
    }

    private Optional<DayAlarmDTO> checkMinEntryTime(RecordsDayDTO recordsDayDTO, Map<DayType, AlarmConfigurationDays> days) {
        if (days.containsKey(recordsDayDTO.getDayType())) {
            AlarmConfigurationDays conf = days.get(recordsDayDTO.getDayType());
            LocalTime minEntryTime = LocalTime.parse(conf.getValueS());
            if (recordsDayDTO.getRecords().get(0).getDate().toLocalTime().isBefore(minEntryTime)) {
                return Optional.of(DayAlarmDTO.builder().type(AlarmType.MIN_ENTRY_TIME).build());
            }
        }
        return Optional.empty();
    }

}
