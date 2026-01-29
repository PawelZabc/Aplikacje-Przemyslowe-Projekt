package com.projekt.kiosk.mappers.rowmappers;

import com.projekt.kiosk.entities.ExtraEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ExtraRowMapper implements RowMapper<ExtraEntity> {
    @Override
    public ExtraEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return ExtraEntity.builder().id(rs.getInt("id")).name(rs.getString("name"))
                .priceCents(rs.getInt("price_cents")).build();
    }
}