package com.prueba.controlfichajes.web;

import com.prueba.controlfichajes.dto.RecordDTO;
import com.prueba.controlfichajes.dto.RecordsRangeDTO;
import com.prueba.controlfichajes.service.RecordService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping
@AllArgsConstructor
public class RecordController {

    // Importar de application.yml
    public static final String URI_BASE = "/api" + "/records";

    private final RecordService recordService;

    @PostMapping(RecordController.URI_BASE)
    public ResponseEntity<RecordDTO> create(@Valid @RequestBody RecordDTO dto) throws URISyntaxException {
        RecordDTO created = recordService.create(dto);
        return ResponseEntity
                .created(new URI(String.format("%s/%s", URI_BASE, created.getId())))
                .body(created);
    }

    @PostMapping(RecordController.URI_BASE + "/all")
    public ResponseEntity<List<RecordDTO>> createAll(@Valid @RequestBody List<RecordDTO> dtos) throws URISyntaxException {
        List<RecordDTO> created = recordService.createAll(dtos);
        return ResponseEntity
                .created(new URI(URI_BASE))
                .body(created);
    }

    @GetMapping(RecordController.URI_BASE + "/{employeeId}/{fromDate}/{toDate}")
    public ResponseEntity<RecordsRangeDTO> getRange(
            @PathVariable String employeeId,
            @PathVariable String fromDate,
            @PathVariable String toDate) {
        return ResponseEntity
                .ok(recordService.getRangeRecordsWithAlarms(employeeId, LocalDate.parse(fromDate), LocalDate.parse(toDate)));
    }

    @PostMapping(RecordController.URI_BASE + "/import")
    public ResponseEntity<List<RecordDTO>> handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException, URISyntaxException {
        List<RecordDTO> created = recordService.importAll(file.getInputStream());
        return ResponseEntity
                .created(new URI(URI_BASE))
                .body(created);
    }

}
