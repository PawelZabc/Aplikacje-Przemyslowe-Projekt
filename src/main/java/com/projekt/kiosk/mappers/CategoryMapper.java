package com.projekt.kiosk.mappers;

import com.projekt.kiosk.dto.CategoryDto;
import com.projekt.kiosk.entities.CategoryEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper implements Mapper<CategoryEntity, CategoryDto> {

    private final ModelMapper modelMapper;

    public CategoryMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public CategoryDto mapTo(CategoryEntity category) {
        return modelMapper.map(category, CategoryDto.class);
    }

    @Override
    public CategoryEntity mapFrom(CategoryDto categoryDto) {
        return modelMapper.map(categoryDto, CategoryEntity.class);
    }
}
