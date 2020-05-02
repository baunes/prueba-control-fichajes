package com.prueba.controlfichajes.model.alarms;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "alarm_configurations")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AlarmConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private Long id;

    @NotNull
    @Column(name = "business_id", nullable = false)
    private String businessId;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @NotNull
    @Column(name = "from_date", nullable = false)
    private ZonedDateTime fromDate;

    @NotNull
    @Column(name = "to_date", nullable = false)
    private ZonedDateTime toDate;

    @NotNull
    @Column(name = "type", nullable = false)
    private AlarmType type;

    @NotNull
    @OneToMany(mappedBy = "alarmConfiguration", fetch = FetchType.EAGER)
    private List<AlarmConfigurationDays> days;
}
