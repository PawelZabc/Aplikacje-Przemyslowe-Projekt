package com.projekt.kiosk.controllers.api;

import com.projekt.kiosk.domain.IngredientEntity;
import com.projekt.kiosk.dtos.IngredientDto;
import com.projekt.kiosk.mappers.Mapper;
import com.projekt.kiosk.services.IngredientService;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Log
public class IngredientController {

    private IngredientService ingredientService;

    private Mapper<IngredientEntity,IngredientDto> ingredientMapper;

    public IngredientController(IngredientService ingredientService,Mapper<IngredientEntity,IngredientDto> ingredientMapper) {
        this.ingredientMapper = ingredientMapper;
        this.ingredientService = ingredientService;
    }

    @GetMapping(path = "/ingredients")
    public List<IngredientEntity> getIngredient(){
        return ingredientService.readAll();
    }


    @PostMapping(path = "/ingredients")
    public ResponseEntity<IngredientDto> createIngredient(@RequestBody final IngredientDto ingredient){
        IngredientEntity ingredientEntity = ingredientMapper.mapFrom(ingredient);
        IngredientEntity createdIngredient = ingredientService.save(ingredientEntity);
        log.info("Ingredient created " + ingredient.toString());
        return new ResponseEntity<>(ingredientMapper.mapTo(createdIngredient), HttpStatus.CREATED);
    }
    @GetMapping(path="/ingredients/{id}")
    public ResponseEntity<IngredientDto> getIngredientById(@PathVariable("id") Integer id){
        Optional<IngredientEntity> ingredient = this.ingredientService.readOne(id);
        return ingredient.map(ing -> {
            IngredientDto ingredientDto = ingredientMapper.mapTo(ing);
            return new ResponseEntity<>(ingredientDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(path="/ingredients/{id}")
    public ResponseEntity<IngredientDto> updateIngredient(
            @PathVariable("id") Integer id, @RequestBody IngredientDto ingredient){
        if(!ingredientService.exists(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ingredient.setId(id);
        IngredientEntity updatedIngredient = ingredientMapper.mapFrom(ingredient);
        IngredientEntity updatedIng = ingredientService.save(updatedIngredient);
        return new ResponseEntity<>(ingredientMapper.mapTo(updatedIng), HttpStatus.OK);
    }
    @PatchMapping(path="/ingredients/{id}")
    public ResponseEntity<IngredientDto> patchIngredient(
            @PathVariable("id") Integer id, @RequestBody IngredientDto ingredient){
        if(!ingredientService.exists(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        IngredientEntity updatedIngredient = ingredientMapper.mapFrom(ingredient);
        IngredientEntity updatedIng = ingredientService.partialUpdate(id,updatedIngredient);
        return new ResponseEntity<>(ingredientMapper.mapTo(updatedIng), HttpStatus.OK);
    }

    @DeleteMapping(path="/ingredients/{id}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable("id") Integer id){
        ingredientService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
