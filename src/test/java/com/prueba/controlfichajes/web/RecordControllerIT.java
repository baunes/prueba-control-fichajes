package com.prueba.controlfichajes.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba.controlfichajes.ControlFichajesApplication;
import com.prueba.controlfichajes.dto.RecordDTOMapper;
import com.prueba.controlfichajes.model.alarms.AlarmConfiguration;
import com.prueba.controlfichajes.model.alarms.AlarmConfigurationDays;
import com.prueba.controlfichajes.model.alarms.AlarmType;
import com.prueba.controlfichajes.model.alarms.DayType;
import com.prueba.controlfichajes.model.records.Record;
import com.prueba.controlfichajes.repository.AlarmRepository;
import com.prueba.controlfichajes.repository.RecordRepository;
import com.prueba.controlfichajes.tests.TemporalStringMatcher;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = ControlFichajesApplication.class)
@AutoConfigureMockMvc
public class RecordControllerIT {

    @Autowired
    private RecordRepository recordRepository;
    @Autowired
    private AlarmRepository alarmRepository;

    @Autowired
    private RecordDTOMapper recordDTOMapper;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc restRecordMockMvc;

    @Test
    @Transactional
    public void createRecord() throws Exception {
        int databaseSizeBeforeCreate = recordRepository.findAll().size();

        String dto = "{\"businessId\": \"1\"," +
                "\"date\": \"2018-01-01T08:00:00.000Z\"," +
                "\"employeeId\": \"02_000064\"," +
                "\"recordType\": \"IN\"," +
                "\"serviceId\": \"ATALAYAS\"," +
                "\"type\": \"WORK\"}";

        // Create the Record
        restRecordMockMvc.perform(post("/api/records")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dto))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                //.andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.businessId").value("1"))
                .andExpect(jsonPath("$.date").value(TemporalStringMatcher.match("2018-01-01T08:00:00.000Z")))
                .andExpect(jsonPath("$.employeeId").value("02_000064"))
                .andExpect(jsonPath("$.recordType").value("IN"))
                .andExpect(jsonPath("$.serviceId").value("ATALAYAS"))
                .andExpect(jsonPath("$.type").value("WORK"));

        // Validate the Record in the database
        List<Record> recordList = recordRepository.findAll();
        assertThat(recordList).hasSize(databaseSizeBeforeCreate + 1);
    }

    @Test
    @Transactional
    public void createAllRecords() throws Exception {
        int databaseSizeBeforeCreate = recordRepository.findAll().size();

        String dtos = "[" +
                "{\"businessId\": \"1\"," +
                " \"date\": \"2018-01-01T08:00:00.000Z\"," +
                " \"employeeId\": \"02_000064\"," +
                " \"recordType\": \"IN\"," +
                " \"serviceId\": \"ATALAYAS\"," +
                " \"type\": \"WORK\"}," +
                "{\"businessId\": \"1\"," +
                " \"date\": \"2018-01-01T13:30:00.000Z\"," +
                " \"employeeId\": \"02_000064\"," +
                " \"recordType\": \"OUT\",\n" +
                " \"serviceId\": \"ATALAYAS\",\n" +
                " \"type\": \"WORK\"}" +
                "]";

        // Create the Record
        restRecordMockMvc.perform(post("/api/records/all")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dtos))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.[0].businessId").value("1"))
                .andExpect(jsonPath("$.[0].date").value(TemporalStringMatcher.match("2018-01-01T08:00:00.000Z")))
                .andExpect(jsonPath("$.[0].employeeId").value("02_000064"))
                .andExpect(jsonPath("$.[0].recordType").value("IN"))
                .andExpect(jsonPath("$.[0].serviceId").value("ATALAYAS"))
                .andExpect(jsonPath("$.[0].type").value("WORK"))
                .andExpect(jsonPath("$.[1].businessId").value("1"))
                .andExpect(jsonPath("$.[1].date").value(TemporalStringMatcher.match("2018-01-01T13:30:00.000Z")))
                .andExpect(jsonPath("$.[1].employeeId").value("02_000064"))
                .andExpect(jsonPath("$.[1].recordType").value("OUT"))
                .andExpect(jsonPath("$.[1].serviceId").value("ATALAYAS"))
                .andExpect(jsonPath("$.[1].type").value("WORK"));

        // Validate the Record in the database
        List<Record> recordList = recordRepository.findAll();
        assertThat(recordList).hasSize(databaseSizeBeforeCreate + 2);
    }

    @Test
    @Transactional
    public void importRealJsonData1() throws Exception {
        importRealJsonData("/data/fichero_1.json", 395);
    }

    @Test
    @Transactional
    public void importRealJsonData2() throws Exception {
        importRealJsonData("/data/fichero_2.json", 491);
    }

    private void importRealJsonData(String file, int length) throws Exception {
        int databaseSizeBeforeCreate = recordRepository.findAll().size();

        String json = IOUtils.toString(this.getClass().getResourceAsStream(file), StandardCharsets.UTF_8);

        // Create the Record
        restRecordMockMvc.perform(post("/api/records/all")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.length()").value(length));

        // Validate the Record in the database
        List<Record> recordList = recordRepository.findAll();
        assertThat(recordList).hasSize(databaseSizeBeforeCreate + length);
    }

    @Test
    @Transactional
    public void getWeekByEmployeeAndDates() throws Exception {
        importRealJsonData("/data/file_week.json", 156);

        restRecordMockMvc.perform(get("/api/records/{employeeId}/{fromDate}/{toDate}", "111111111",
                "2018-01-01", "2018-01-07")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.days.length()").value(7))
                .andExpect(jsonPath("$.days[0].records.length()").value(6))
                .andExpect(jsonPath("$.days[0].dayType").value(DayType.MONDAY.toString()))
                .andExpect(jsonPath("$.days[0].date").value("2018-01-01"))
                .andExpect(jsonPath("$.days[0].workTime").value(8.25))
                .andExpect(jsonPath("$.days[0].restTime").value(0.25))
                .andExpect(jsonPath("$.days[1].records.length()").value(6))
                .andExpect(jsonPath("$.days[1].dayType").value(DayType.TUESDAY.toString()))
                .andExpect(jsonPath("$.days[1].date").value("2018-01-02"))
                .andExpect(jsonPath("$.days[1].workTime").value(8.25))
                .andExpect(jsonPath("$.days[1].restTime").value(0.25))
                .andExpect(jsonPath("$.days[2].records.length()").value(6))
                .andExpect(jsonPath("$.days[2].dayType").value(DayType.WEDNESDAY.toString()))
                .andExpect(jsonPath("$.days[2].date").value("2018-01-03"))
                .andExpect(jsonPath("$.days[2].workTime").value(8.25))
                .andExpect(jsonPath("$.days[2].restTime").value(0.25))
                .andExpect(jsonPath("$.days[3].records.length()").value(6))
                .andExpect(jsonPath("$.days[3].dayType").value(DayType.THURSDAY.toString()))
                .andExpect(jsonPath("$.days[3].date").value("2018-01-04"))
                .andExpect(jsonPath("$.days[3].workTime").value(8.25))
                .andExpect(jsonPath("$.days[3].restTime").value(0.25))
                .andExpect(jsonPath("$.days[4].records.length()").value(2))
                .andExpect(jsonPath("$.days[4].dayType").value(DayType.FRIDAY.toString()))
                .andExpect(jsonPath("$.days[4].date").value("2018-01-05"))
                .andExpect(jsonPath("$.days[4].workTime").value(7))
                .andExpect(jsonPath("$.days[4].restTime").value(0))
                .andExpect(jsonPath("$.days[5].records.length()").value(0))
                .andExpect(jsonPath("$.days[5].dayType").value(DayType.SATURDAY.toString()))
                .andExpect(jsonPath("$.days[5].date").value("2018-01-06"))
                .andExpect(jsonPath("$.days[5].workTime").value(0))
                .andExpect(jsonPath("$.days[5].restTime").value(0))
                .andExpect(jsonPath("$.days[6].records.length()").value(0))
                .andExpect(jsonPath("$.days[6].dayType").value(DayType.SUNDAY.toString()))
                .andExpect(jsonPath("$.days[6].date").value("2018-01-07"))
                .andExpect(jsonPath("$.days[6].workTime").value(0))
                .andExpect(jsonPath("$.days[6].restTime").value(0));
    }

    @Test
    @Transactional
    public void getWeekByEmployeeAndDatesWithAlarmIncomplete() throws Exception {
        importRealJsonData("/data/file_alarm_record_incomplete.json", 11);
        createAlarmConfigurationIncomplete();

        restRecordMockMvc.perform(get("/api/records/{employeeId}/{fromDate}/{toDate}", "111111111",
                "2018-01-01", "2018-01-02")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.days.length()").value(2))
                .andExpect(jsonPath("$.days[0].records.length()").value(6))
                .andExpect(jsonPath("$.days[0].dayType").value(DayType.MONDAY.toString()))
                .andExpect(jsonPath("$.days[0].date").value("2018-01-01"))
                .andExpect(jsonPath("$.days[0].workTime").value(8.25))
                .andExpect(jsonPath("$.days[0].restTime").value(0.25))
                .andExpect(jsonPath("$.days[0].alarms.length()").value(0))
                .andExpect(jsonPath("$.days[1].records.length()").value(5))
                .andExpect(jsonPath("$.days[1].dayType").value(DayType.TUESDAY.toString()))
                .andExpect(jsonPath("$.days[1].date").value("2018-01-02"))
                .andExpect(jsonPath("$.days[1].workTime").value(5.25))
                .andExpect(jsonPath("$.days[1].restTime").value(0.25))
                .andExpect(jsonPath("$.days[1].alarms.length()").value(1))
                .andExpect(jsonPath("$.days[1].alarms.[0].type").value(AlarmType.INCOMPLETE.toString()));
    }

    private void createAlarmConfigurationIncomplete() {
        AlarmConfiguration configuration = new AlarmConfiguration();
        configuration.setBusinessId("1");
        configuration.setDescription("Incomplete day");
        configuration.setActive(true);
        configuration.setType(AlarmType.INCOMPLETE);
        configuration.setFromDate(ZonedDateTime.parse("2018-01-01T00:00:00.000Z"));
        configuration.setToDate(ZonedDateTime.parse("2018-12-31T23:59:59.999Z"));
        configuration.setDays(new ArrayList<>(7));
        configuration.getDays().add(new AlarmConfigurationDays());
        configuration.getDays().get(0).setDayWeek(DayType.MONDAY);
        configuration.getDays().add(new AlarmConfigurationDays());
        configuration.getDays().get(1).setDayWeek(DayType.TUESDAY);
        configuration.getDays().add(new AlarmConfigurationDays());
        configuration.getDays().get(2).setDayWeek(DayType.WEDNESDAY);
        configuration.getDays().add(new AlarmConfigurationDays());
        configuration.getDays().get(3).setDayWeek(DayType.THURSDAY);
        configuration.getDays().add(new AlarmConfigurationDays());
        configuration.getDays().get(4).setDayWeek(DayType.FRIDAY);
        configuration.getDays().add(new AlarmConfigurationDays());
        configuration.getDays().get(5).setDayWeek(DayType.SATURDAY);
        configuration.getDays().add(new AlarmConfigurationDays());
        configuration.getDays().get(6).setDayWeek(DayType.SUNDAY);
        alarmRepository.save(configuration);
    }

    @Test
    @Transactional
    public void getWeekByEmployeeAndDatesWithAlarmMaxWorkingHours() throws Exception {
        importRealJsonData("/data/file_alarm_record_max_working_hours.json", 12);
        createAlarmConfigurationMaxWorkingHours();

        restRecordMockMvc.perform(get("/api/records/{employeeId}/{fromDate}/{toDate}", "111111111",
                "2018-01-01", "2018-01-02")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.days.length()").value(2))
                .andExpect(jsonPath("$.days[0].records.length()").value(6))
                .andExpect(jsonPath("$.days[0].dayType").value(DayType.MONDAY.toString()))
                .andExpect(jsonPath("$.days[0].date").value("2018-01-01"))
                .andExpect(jsonPath("$.days[0].workTime").value(8.25))
                .andExpect(jsonPath("$.days[0].restTime").value(0.25))
                .andExpect(jsonPath("$.days[0].alarms.length()").value(0))
                .andExpect(jsonPath("$.days[1].records.length()").value(6))
                .andExpect(jsonPath("$.days[1].dayType").value(DayType.TUESDAY.toString()))
                .andExpect(jsonPath("$.days[1].date").value("2018-01-02"))
                .andExpect(jsonPath("$.days[0].workTime").value(8.25))
                .andExpect(jsonPath("$.days[0].restTime").value(0.25))
                .andExpect(jsonPath("$.days[1].alarms.length()").value(1))
                .andExpect(jsonPath("$.days[1].alarms.[0].type").value(AlarmType.MAX_WORKING_HOURS.toString()));
    }

    private void createAlarmConfigurationMaxWorkingHours() {
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
        configuration.getDays().get(1).setValueN(BigDecimal.valueOf(8));
        configuration.getDays().add(new AlarmConfigurationDays());
        configuration.getDays().get(2).setDayWeek(DayType.WEDNESDAY);
        configuration.getDays().get(2).setValueN(BigDecimal.valueOf(8));
        configuration.getDays().add(new AlarmConfigurationDays());
        configuration.getDays().get(3).setDayWeek(DayType.THURSDAY);
        configuration.getDays().get(3).setValueN(BigDecimal.valueOf(8));
        configuration.getDays().add(new AlarmConfigurationDays());
        configuration.getDays().get(4).setDayWeek(DayType.FRIDAY);
        configuration.getDays().get(4).setValueN(BigDecimal.valueOf(8));
        configuration.getDays().add(new AlarmConfigurationDays());
        configuration.getDays().get(5).setDayWeek(DayType.SATURDAY);
        configuration.getDays().get(5).setValueN(BigDecimal.valueOf(0));
        configuration.getDays().add(new AlarmConfigurationDays());
        configuration.getDays().get(6).setDayWeek(DayType.SUNDAY);
        configuration.getDays().get(6).setValueN(BigDecimal.valueOf(1));
        alarmRepository.save(configuration);
    }

}
