package com.prueba.controlfichajes.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Entity
@Table(name = "record")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private Long id;

    @NotNull
    @Column(name = "business_id", nullable = false)
    private String businessId;

    @NotNull
    @Column(name = "date", nullable = false)
    private ZonedDateTime date;

    @NotNull
    @Column(name = "employee_id", nullable = false)
    private String employeeId;

    @NotNull
    @Column(name = "record_type", nullable = false)
    private RecordType recordType;

    @NotNull
    @Column(name = "service_id", nullable = false)
    private String serviceId;

    @NotNull
    @Column(name = "service_type", nullable = false)
    private ServiceType serviceType;
}
