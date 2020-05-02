package com.prueba.controlfichajes.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba.controlfichajes.ControlFichajesApplication;
import com.prueba.controlfichajes.dto.RecordDTOMapper;
import com.prueba.controlfichajes.model.Record;
import com.prueba.controlfichajes.model.DayType;
import com.prueba.controlfichajes.repository.RecordRepository;
import org.apache.commons.io.IOUtils;
import org.exparity.hamcrest.date.ZonedDateTimeMatchers;
import org.exparity.hamcrest.date.core.TemporalMatcher;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
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
    public void getWeekByEmployeeAndDates2() throws Exception {
        importRealJsonData("/data/fichero_week.json", 102);

        restRecordMockMvc.perform(get("/api/records/{employeeId}/{fromDate}/{toDate}", "111111111",
                "2018-01-01", "2018-01-07")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.days.length()").value(7))
                .andExpect(jsonPath("$.days[0].records.length()").value(4))
                .andExpect(jsonPath("$.days[0].dayType").value(DayType.MONDAY.toString()))
                .andExpect(jsonPath("$.days[0].date").value("2018-01-01"))
                .andExpect(jsonPath("$.days[1].records.length()").value(4))
                .andExpect(jsonPath("$.days[1].dayType").value(DayType.TUESDAY.toString()))
                .andExpect(jsonPath("$.days[1].date").value("2018-01-02"))
                .andExpect(jsonPath("$.days[2].records.length()").value(4))
                .andExpect(jsonPath("$.days[2].dayType").value(DayType.WEDNESDAY.toString()))
                .andExpect(jsonPath("$.days[2].date").value("2018-01-03"))
                .andExpect(jsonPath("$.days[3].records.length()").value(4))
                .andExpect(jsonPath("$.days[3].dayType").value(DayType.THURSDAY.toString()))
                .andExpect(jsonPath("$.days[3].date").value("2018-01-04"))
                .andExpect(jsonPath("$.days[4].records.length()").value(2))
                .andExpect(jsonPath("$.days[4].dayType").value(DayType.FRIDAY.toString()))
                .andExpect(jsonPath("$.days[4].date").value("2018-01-05"))
                .andExpect(jsonPath("$.days[5].records.length()").value(0))
                .andExpect(jsonPath("$.days[5].dayType").value(DayType.SATURDAY.toString()))
                .andExpect(jsonPath("$.days[5].date").value("2018-01-06"))
                .andExpect(jsonPath("$.days[6].records.length()").value(0))
                .andExpect(jsonPath("$.days[6].dayType").value(DayType.SUNDAY.toString()))
                .andExpect(jsonPath("$.days[6].date").value("2018-01-07"));
    }

    private static class TemporalStringMatcher extends BaseMatcher<String> {

        private final TemporalMatcher<ZonedDateTime> matcher;

        private TemporalStringMatcher(String dateTime) {
            this.matcher = ZonedDateTimeMatchers.within(0, ChronoUnit.SECONDS, ZonedDateTime.parse(dateTime));
        }

        @Override
        public boolean matches(Object actual) {
            return this.matcher.matches(ZonedDateTime.parse((String) actual));
        }

        @Override
        public void describeTo(Description description) {
            this.matcher.describeTo(description);
        }

        private static TemporalStringMatcher match(String dateTime) {
            return new TemporalStringMatcher(dateTime);
        }
    }

}
