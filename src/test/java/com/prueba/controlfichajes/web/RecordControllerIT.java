package com.prueba.controlfichajes.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba.controlfichajes.ControlFichajesApplication;
import com.prueba.controlfichajes.dto.RecordDTOMapper;
import com.prueba.controlfichajes.model.Record;
import com.prueba.controlfichajes.repository.RecordRepository;
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

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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
