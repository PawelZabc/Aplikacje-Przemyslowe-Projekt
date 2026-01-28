package com.projekt.kiosk.controllers.api;

import com.projekt.kiosk.domain.ExtraEntity;
import com.projekt.kiosk.dtos.ExtraDto;
import com.projekt.kiosk.exceptions.ResourceNotFoundException;
import com.projekt.kiosk.mappers.Mapper;
import com.projekt.kiosk.services.ExtraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@Slf4j
@RequestMapping("/api/v1/extras")
public class ExtraController {

    private final ExtraService extraService;
    private final Mapper<ExtraEntity, ExtraDto> extraMapper;

    public ExtraController(ExtraService extraService,
                           Mapper<ExtraEntity, ExtraDto> extraMapper) {
        this.extraService = extraService;
        this.extraMapper = extraMapper;
    }

    @Operation(summary = "Odczytaj wszystkie dodatki", description = "Odczytuje listę wszystkich dodatków")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista dodatków zwrócona poprawnie")
    })
    @GetMapping
    public List<ExtraEntity> getExtras() {
        List<ExtraEntity> extras = extraService.readAll();
        log.info("GET /api/v1/extra - sukces, liczba dodatków: {}", extras.size());
        return extras;
    }

    @Operation(summary = "Utwórz nowy dodatek", description = "Tworzy nowy dodatek na podstawie przekazanego DTO")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Dodatek utworzony"),
            @ApiResponse(responseCode = "400", description = "Niepoprawne dane wejściowe")
    })
    @PostMapping
    public ResponseEntity<ExtraDto> createExtra(@RequestBody ExtraDto extraDto) {
        if (extraDto.getName() == null || extraDto.getName().isBlank()) {
            log.warn("POST /api/v1/extra - niepoprawne dane: brak nazwy");
            throw new IllegalArgumentException("Name cannot be null or blank");
        }

        ExtraEntity extraEntity = extraMapper.mapFrom(extraDto);
        ExtraEntity createdExtra = extraService.save(extraEntity);
        log.info("POST /api/v1/extra - sukces, utworzono dodatek id={}", createdExtra.getId());
        return new ResponseEntity<>(extraMapper.mapTo(createdExtra), HttpStatus.CREATED);
    }

    @Operation(summary = "Pobierz dodatek po ID", description = "Zwraca pojedynczy dodatek na podstawie ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dodatek znaleziony"),
            @ApiResponse(responseCode = "404", description = "Dodatek nie istnieje"),
            @ApiResponse(responseCode = "400", description = "Niepoprawne ID")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ExtraDto> getExtraById(@PathVariable Integer id) {
        if (id <= 0) {
            log.warn("GET /api/v1/extra/{} - niepoprawne ID", id);
            throw new IllegalArgumentException("ID must be positive");
        }

        ExtraEntity extra = extraService.readOne(id)
                .orElseThrow(() -> new ResourceNotFoundException("Extra id=" + id + " not found"));
        log.info("GET /api/v1/extra/{} - sukces", id);
        return ResponseEntity.ok(extraMapper.mapTo(extra));
    }

    @Operation(summary = "Aktualizuj dodatek", description = "Aktualizuje cały dodatek (PUT)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dodatek zaktualizowany"),
            @ApiResponse(responseCode = "404", description = "Dodatek nie istnieje"),
            @ApiResponse(responseCode = "400", description = "Niepoprawne dane")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ExtraDto> updateExtra(
            @PathVariable Integer id,
            @RequestBody ExtraDto extraDto) {

        if (id <= 0) {
            log.warn("PUT /api/v1/extra/{} - niepoprawne ID", id);
            throw new IllegalArgumentException("ID must be positive");
        }

        if (!extraService.exists(id)) {
            throw new ResourceNotFoundException("Extra id=" + id + " not found");
        }

        if (extraDto.getName() == null || extraDto.getName().isBlank()) {
            log.warn("PUT /api/v1/extra/{} - niepoprawne dane: brak nazwy", id);
            throw new IllegalArgumentException("Name cannot be null or blank");
        }

        extraDto.setId(id);
        ExtraEntity savedExtra = extraService.save(extraMapper.mapFrom(extraDto));
        log.info("PUT /api/v1/extra/{} - sukces", id);
        return ResponseEntity.ok(extraMapper.mapTo(savedExtra));
    }

    @Operation(summary = "Częściowa aktualizacja dodatku", description = "Aktualizuje wybrane pola dodatku (PATCH)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dodatek zaktualizowany"),
            @ApiResponse(responseCode = "404", description = "Dodatek nie istnieje"),
            @ApiResponse(responseCode = "400", description = "Niepoprawne dane")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<ExtraDto> patchExtra(
            @PathVariable Integer id,
            @RequestBody ExtraDto extraDto) {

        if (id <= 0) {
            log.warn("PATCH /api/v1/extra/{} - niepoprawne ID", id);
            throw new IllegalArgumentException("ID must be positive");
        }

        if (!extraService.exists(id)) {
            throw new ResourceNotFoundException("Extra id=" + id + " not found");
        }

        ExtraEntity updatedExtra = extraService.partialUpdate(id, extraMapper.mapFrom(extraDto));
        log.info("PATCH /api/v1/extra/{} - sukces", id);
        return ResponseEntity.ok(extraMapper.mapTo(updatedExtra));
    }

    @Operation(summary = "Usuń dodatek", description = "Usuwa dodatek na podstawie ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Dodatek usunięty"),
            @ApiResponse(responseCode = "404", description = "Dodatek nie istnieje"),
            @ApiResponse(responseCode = "400", description = "Niepoprawne ID")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExtra(@PathVariable Integer id) {

        if (id <= 0) {
            log.warn("DELETE /api/v1/extra/{} - niepoprawne ID", id);
            throw new IllegalArgumentException("ID must be positive");
        }

        if (!extraService.exists(id)) {
            throw new ResourceNotFoundException("Extra id=" + id + " not found");
        }

        extraService.delete(id);
        log.info("DELETE /api/v1/extra/{} - sukces", id);
        return ResponseEntity.noContent().build();
    }
}
