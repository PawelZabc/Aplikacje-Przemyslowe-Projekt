package com.projekt.kiosk.mappers.rowmappers;

import com.projekt.kiosk.domain.ExtraEntity;
import com.projekt.kiosk.domain.IngredientEntity;
import com.projekt.kiosk.domain.ProductEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class IngredientRowMapper implements RowMapper<IngredientEntity> {
    @Override
    public IngredientEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return IngredientEntity.builder().id(rs.getInt("id")).name(rs.getString("name")).build();
    }
}


