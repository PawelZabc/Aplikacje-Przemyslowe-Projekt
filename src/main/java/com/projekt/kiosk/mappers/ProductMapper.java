package com.projekt.kiosk.mappers;

import com.projekt.kiosk.entities.ProductEntity;
import com.projekt.kiosk.dto.ProductDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper implements Mapper<ProductEntity, ProductDto> {

    private final ModelMapper modelMapper;

    public ProductMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public ProductDto mapTo(ProductEntity product) {
        return modelMapper.map(product, ProductDto.class);
    }

    @Override
    public ProductEntity mapFrom(ProductDto productDto) {
        return modelMapper.map(productDto, ProductEntity.class);
    }
}

