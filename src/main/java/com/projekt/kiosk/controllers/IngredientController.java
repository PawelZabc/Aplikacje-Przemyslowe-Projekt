package com.projekt.kiosk.controllers;

import com.projekt.kiosk.domain.Ingredient;
import com.projekt.kiosk.dtos.IngredientDto;
import com.projekt.kiosk.mappers.IngredientMapper;
import com.projekt.kiosk.mappers.Mapper;
import com.projekt.kiosk.repositories.IngredientRepository;
import com.projekt.kiosk.services.IngredientService;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log
public class IngredientController {

    private IngredientService ingredientService;

    private Mapper<Ingredient,IngredientDto> ingredientMapper;

    public IngredientController(IngredientService ingredientService,Mapper<Ingredient,IngredientDto> ingredientMapper) {
        this.ingredientMapper = ingredientMapper;
        this.ingredientService = ingredientService;
    }

    @GetMapping(path = "/ingredients")
    public Iterable<Ingredient> getIngredient(){
        return ingredientService.readAll();
    }


    @PostMapping(path = "/ingredients")
    public IngredientDto createIngredient(@RequestBody final IngredientDto ingredient){
        Ingredient ingredientEntity = ingredientMapper.mapFrom(ingredient);
        Ingredient createdIngredient = ingredientService.createIngredient(ingredientEntity);
        log.info("Ingredient created " + ingredient.toString());
        return ingredientMapper.mapTo(createdIngredient);
    }
}
