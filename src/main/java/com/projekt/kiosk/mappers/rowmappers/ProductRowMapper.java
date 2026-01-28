package com.projekt.kiosk.mappers.rowmappers;

import com.projekt.kiosk.domain.ProductEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductRowMapper implements RowMapper<ProductEntity> {
    @Override
    public ProductEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return ProductEntity.builder().id(rs.getInt("id"))
                .name(rs.getString("name")).priceCents(rs.getInt("price_cents")).build();
    }
}
