package com.prueba.controlfichajes.dto;

import com.prueba.controlfichajes.model.alarms.AlarmType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DayAlarmDTO {

    private AlarmType type;

}
