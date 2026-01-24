package com.projekt.kiosk.mappers;

import com.projekt.kiosk.domain.ExtraEntity;
import com.projekt.kiosk.dtos.ExtraDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


@Component
public class ExtraMapper implements Mapper<ExtraEntity, ExtraDto> {

    private final ModelMapper modelMapper;

    public ExtraMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public ExtraDto mapTo(ExtraEntity extra) {
        return modelMapper.map(extra, ExtraDto.class);
    }

    @Override
    public ExtraEntity mapFrom(ExtraDto extraDto) {
        return modelMapper.map(extraDto, ExtraEntity.class);
    }
}
