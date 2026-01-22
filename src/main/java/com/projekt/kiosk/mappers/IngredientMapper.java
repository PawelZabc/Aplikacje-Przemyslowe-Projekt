package com.projekt.kiosk.mappers;

import com.projekt.kiosk.domain.Ingredient;
import com.projekt.kiosk.dtos.IngredientDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


@Component
public class IngredientMapper implements Mapper<Ingredient, IngredientDto> {


    private ModelMapper modelMapper;

    public IngredientMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public IngredientDto mapTo(Ingredient ingredient) {
        return modelMapper.map(ingredient, IngredientDto.class);
    }

    @Override
    public Ingredient mapFrom(IngredientDto ingredientDto) {
        return modelMapper.map(ingredientDto, Ingredient.class);
    }
}
