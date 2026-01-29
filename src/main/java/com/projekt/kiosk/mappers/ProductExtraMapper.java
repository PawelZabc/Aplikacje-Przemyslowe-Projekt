package com.projekt.kiosk.mappers;

import com.projekt.kiosk.entities.ExtraEntity;
import com.projekt.kiosk.entities.ProductExtraEntity;
import com.projekt.kiosk.dto.ProductExtraDto;
import org.springframework.stereotype.Component;

@Component
public class ProductExtraMapper
        implements Mapper<ProductExtraEntity, ProductExtraDto> {

    @Override
    public ProductExtraDto mapTo(ProductExtraEntity entity) {
        return ProductExtraDto.builder()
                .extraId(entity.getExtra().getId())
                .build();
    }

    @Override
    public ProductExtraEntity mapFrom(ProductExtraDto dto) {
        ProductExtraEntity entity = new ProductExtraEntity();
        ExtraEntity extra = new ExtraEntity();
        extra.setId(dto.getExtraId());
        entity.setExtra(extra);
        return entity;
    }
}

