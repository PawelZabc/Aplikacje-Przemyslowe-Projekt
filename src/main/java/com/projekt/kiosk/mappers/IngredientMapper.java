package com.projekt.kiosk.mappers;

import com.projekt.kiosk.entities.IngredientEntity;
import com.projekt.kiosk.dto.IngredientDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


@Component
public class IngredientMapper implements Mapper<IngredientEntity, IngredientDto> {


    private ModelMapper modelMapper;

    public IngredientMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public IngredientDto mapTo(IngredientEntity ingredient) {
        return modelMapper.map(ingredient, IngredientDto.class);
    }

    @Override
    public IngredientEntity mapFrom(IngredientDto ingredientDto) {
        return modelMapper.map(ingredientDto, IngredientEntity.class);
    }
}
