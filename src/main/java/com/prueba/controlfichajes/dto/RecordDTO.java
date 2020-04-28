package com.prueba.controlfichajes.dto;

import com.prueba.controlfichajes.model.RecordType;
import com.prueba.controlfichajes.model.ServiceType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RecordDTO {

    @EqualsAndHashCode.Include
    private Long id;

    @NotNull
    private String businessId;

    @NotNull
    private ZonedDateTime date;

    @NotNull
    private String employeeId;

    @NotNull
    private RecordType recordType;

    @NotNull
    private String serviceId;

    @NotNull
    private ServiceType type;

}
