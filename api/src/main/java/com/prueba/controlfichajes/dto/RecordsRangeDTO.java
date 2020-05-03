package com.prueba.controlfichajes.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordsRangeDTO {

    List<RecordsDayDTO> days;

}
