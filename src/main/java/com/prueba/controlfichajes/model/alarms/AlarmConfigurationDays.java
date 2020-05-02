package com.prueba.controlfichajes.model.alarms;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "alarm_configuration_days")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AlarmConfigurationDays {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private Long id;

    @NotNull
    @Column(name = "day_week")
    private DayType dayWeek;

    @Column(name = "value_s")
    private String valueS;

    @Column(name = "value_n", precision = 15, scale = 2)
    private BigDecimal valueN;

    @ManyToOne
    private AlarmConfiguration alarmConfiguration;
}
