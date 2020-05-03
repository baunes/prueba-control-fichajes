package com.prueba.controlfichajes.repository;

import com.prueba.controlfichajes.model.alarms.AlarmConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.ZonedDateTime;
import java.util.List;

public interface AlarmRepository extends JpaRepository<AlarmConfiguration, Long> {

    List<AlarmConfiguration> findAllByBusinessId(String businessId);

    List<AlarmConfiguration> findAllByBusinessIdAndActiveIsTrue(String businessId);

    List<AlarmConfiguration> findAllByBusinessIdAndActiveIsTrueAndFromDateLessThanEqual(
            String businessId, ZonedDateTime fromDate);

    List<AlarmConfiguration> findAllByBusinessIdAndActiveIsTrueAndFromDateLessThanEqualAndToDateGreaterThanEqual(
            String businessId, ZonedDateTime fromDate, ZonedDateTime toDate);

}
