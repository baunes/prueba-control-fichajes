package com.prueba.controlfichajes.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeekRecordsDTO {

    @NotNull
    @Singular("monday")
    private List<RecordDTO> monday;

    @NotNull
    @Singular("tuesday")
    private List<RecordDTO> tuesday;

    @NotNull
    @Singular("wednesday")
    private List<RecordDTO> wednesday;

    @NotNull
    @Singular("thursday")
    private List<RecordDTO> thursday;

    @NotNull
    @Singular("friday")
    private List<RecordDTO> friday;

    @NotNull
    @Singular("saturday")
    private List<RecordDTO> saturday;

    @NotNull
    @Singular("sunday")
    private List<RecordDTO> sunday;

}
