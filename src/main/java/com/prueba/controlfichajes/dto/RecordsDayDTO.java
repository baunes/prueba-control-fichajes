package com.prueba.controlfichajes.dto;

import com.prueba.controlfichajes.model.alarms.DayType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordsDayDTO {

    private DayType dayType;

    private LocalDate date;

    private List<RecordDTO> records;

    @Builder.Default
    private BigDecimal workTime = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal restTime = BigDecimal.ZERO;

    @Builder.Default
    private List<DayAlarmDTO> alarms = new ArrayList<>();
}
