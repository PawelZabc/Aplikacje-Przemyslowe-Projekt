package com.projekt.kiosk.controllers;

import com.projekt.kiosk.domain.ExtraEntity;
import com.projekt.kiosk.dtos.ExtraDto;
import com.projekt.kiosk.mappers.Mapper;
import com.projekt.kiosk.services.ExtraService;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Log
public class ExtraController {

    private final ExtraService extraService;
    private final Mapper<ExtraEntity, ExtraDto> extraMapper;

    public ExtraController(ExtraService extraService,
                           Mapper<ExtraEntity, ExtraDto> extraMapper) {
        this.extraService = extraService;
        this.extraMapper = extraMapper;
    }

    @GetMapping(path = "/extras")
    public List<ExtraEntity> getExtras() {
        return extraService.readAll();
    }

    @PostMapping(path = "/extras")
    public ResponseEntity<ExtraDto> createExtra(@RequestBody ExtraDto extraDto) {
        ExtraEntity extraEntity = extraMapper.mapFrom(extraDto);
        ExtraEntity createdExtra = extraService.save(extraEntity);
        log.info("Extra created " + extraDto);
        return new ResponseEntity<>(extraMapper.mapTo(createdExtra), HttpStatus.CREATED);
    }

    @GetMapping(path = "/extras/{id}")
    public ResponseEntity<ExtraDto> getExtraById(@PathVariable Integer id) {
        return extraService.readOne(id)
                .map(extra -> new ResponseEntity<>(
                        extraMapper.mapTo(extra), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(path = "/extras/{id}")
    public ResponseEntity<ExtraDto> updateExtra(
            @PathVariable Integer id,
            @RequestBody ExtraDto extraDto) {

        if (!extraService.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        extraDto.setId(id);
        ExtraEntity updatedExtra = extraMapper.mapFrom(extraDto);
        ExtraEntity savedExtra = extraService.save(updatedExtra);

        return new ResponseEntity<>(extraMapper.mapTo(savedExtra), HttpStatus.OK);
    }

    @PatchMapping(path = "/extras/{id}")
    public ResponseEntity<ExtraDto> patchExtra(
            @PathVariable Integer id,
            @RequestBody ExtraDto extraDto) {

        if (!extraService.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ExtraEntity extraEntity = extraMapper.mapFrom(extraDto);
        ExtraEntity updatedExtra = extraService.partialUpdate(id, extraEntity);

        return new ResponseEntity<>(extraMapper.mapTo(updatedExtra), HttpStatus.OK);
    }

    @DeleteMapping(path = "/extras/{id}")
    public ResponseEntity<Void> deleteExtra(@PathVariable Integer id) {
        extraService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

