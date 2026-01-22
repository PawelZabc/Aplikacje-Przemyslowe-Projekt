package com.projekt.kiosk.dao.impl;

import com.projekt.kiosk.dao.IngredientDao;
import com.projekt.kiosk.domain.Ingredient;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
public class IngredientDaoImpl implements IngredientDao {
    private final JdbcTemplate jdbcTemplate;
    public IngredientDaoImpl(final JdbcTemplate jdbcTemplate) {this.jdbcTemplate = jdbcTemplate;}
    @Override
        public void create(Ingredient ingredient) {
        jdbcTemplate.update("insert into ingredients (name) values (?)",
                ingredient.getName());
        };
    public Optional<Ingredient> findById(int id) {
        List<Ingredient> result  = jdbcTemplate.query("select id, name from ingredients where id = ? limit 1", new IngredientMapper(),id);
        return result.stream().findFirst();
    }

    public List<Ingredient> find() {
        return jdbcTemplate.query("select id, name from ingredients", new IngredientMapper());
    }

    @Override
    public void update(Ingredient ingredient) {
        jdbcTemplate.update("update ingredients set name = ? where id = ?", ingredient.getName(), ingredient.getId());

    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update("delete from ingredients where id = ?", id);
    }

    public static class IngredientMapper implements RowMapper<Ingredient> {

        @Override
        public Ingredient mapRow(ResultSet rs, int rowNum) throws SQLException{
            return Ingredient.builder().
                    id(rs.getInt("id")).
                    name(rs.getString("name")).
                    build();
        }
    }

}
