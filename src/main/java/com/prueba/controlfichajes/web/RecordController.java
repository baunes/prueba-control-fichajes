package com.prueba.controlfichajes.web;

import com.prueba.controlfichajes.dto.RecordDTO;
import com.prueba.controlfichajes.service.RecordService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
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

}
