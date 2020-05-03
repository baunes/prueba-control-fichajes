package com.prueba.controlfichajes;

import com.prueba.controlfichajes.model.alarms.AlarmConfiguration;
import com.prueba.controlfichajes.model.alarms.AlarmConfigurationDays;
import com.prueba.controlfichajes.model.alarms.AlarmType;
import com.prueba.controlfichajes.model.alarms.DayType;
import com.prueba.controlfichajes.repository.AlarmRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;

@SpringBootApplication
public class ControlFichajesApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(ControlFichajesApplication.class, args);
		AlarmRepository alarmRepository = context.getBean(AlarmRepository.class);
	}

	private void createAlarmConfigurationMaxWorkingHours(AlarmRepository alarmRepository) {
		AlarmConfiguration configuration = new AlarmConfiguration();
		configuration.setBusinessId("1");
		configuration.setDescription("Max working hours");
		configuration.setActive(true);
		configuration.setType(AlarmType.MAX_WORKING_HOURS);
		configuration.setFromDate(ZonedDateTime.parse("2018-01-01T00:00:00.000Z"));
		configuration.setToDate(ZonedDateTime.parse("2018-12-31T23:59:59.999Z"));
		configuration.setDays(new ArrayList<>(7));
		configuration.getDays().add(new AlarmConfigurationDays());
		configuration.getDays().get(0).setDayWeek(DayType.MONDAY);
		configuration.getDays().get(0).setValueN(BigDecimal.valueOf(10));
		configuration.getDays().add(new AlarmConfigurationDays());
		configuration.getDays().get(1).setDayWeek(DayType.TUESDAY);
		configuration.getDays().get(1).setValueN(BigDecimal.valueOf(10));
		configuration.getDays().add(new AlarmConfigurationDays());
		configuration.getDays().get(2).setDayWeek(DayType.WEDNESDAY);
		configuration.getDays().get(2).setValueN(BigDecimal.valueOf(10));
		configuration.getDays().add(new AlarmConfigurationDays());
		configuration.getDays().get(3).setDayWeek(DayType.THURSDAY);
		configuration.getDays().get(3).setValueN(BigDecimal.valueOf(10));
		configuration.getDays().add(new AlarmConfigurationDays());
		configuration.getDays().get(4).setDayWeek(DayType.FRIDAY);
		configuration.getDays().get(4).setValueN(BigDecimal.valueOf(10));
		configuration.getDays().add(new AlarmConfigurationDays());
		configuration.getDays().get(5).setDayWeek(DayType.SATURDAY);
		configuration.getDays().get(5).setValueN(BigDecimal.valueOf(0));
		configuration.getDays().add(new AlarmConfigurationDays());
		configuration.getDays().get(6).setDayWeek(DayType.SUNDAY);
		configuration.getDays().get(6).setValueN(BigDecimal.valueOf(0));
		alarmRepository.save(configuration);
	}

	private void createAlarmConfigurationMinEntryTime(AlarmRepository alarmRepository) {
		AlarmConfiguration configuration = new AlarmConfiguration();
		configuration.setBusinessId("1");
		configuration.setDescription("Min entry time");
		configuration.setActive(true);
		configuration.setType(AlarmType.MIN_ENTRY_TIME);
		configuration.setFromDate(ZonedDateTime.parse("2018-01-01T00:00:00.000Z"));
		configuration.setToDate(ZonedDateTime.parse("2018-12-31T23:59:59.999Z"));
		configuration.setDays(new ArrayList<>(5));
		configuration.getDays().add(new AlarmConfigurationDays());
		configuration.getDays().get(0).setDayWeek(DayType.MONDAY);
		configuration.getDays().get(0).setValueS("08:00:00");
		configuration.getDays().add(new AlarmConfigurationDays());
		configuration.getDays().get(1).setDayWeek(DayType.TUESDAY);
		configuration.getDays().get(1).setValueS("08:00:00");
		configuration.getDays().add(new AlarmConfigurationDays());
		configuration.getDays().get(2).setDayWeek(DayType.WEDNESDAY);
		configuration.getDays().get(2).setValueS("08:00:00");
		configuration.getDays().add(new AlarmConfigurationDays());
		configuration.getDays().get(3).setDayWeek(DayType.THURSDAY);
		configuration.getDays().get(3).setValueS("08:00:00");
		configuration.getDays().add(new AlarmConfigurationDays());
		configuration.getDays().get(4).setDayWeek(DayType.FRIDAY);
		configuration.getDays().get(4).setValueS("07:00:00");
		alarmRepository.save(configuration);
	}

}
