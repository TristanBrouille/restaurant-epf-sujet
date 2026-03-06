package fr.epf.restaurant.repository;

import fr.epf.restaurant.entity.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public class IngredientRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Collection<Ingredient> ofAll(){
        String sql = "SELECT * FROM INGREDIENT";

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new Ingredient(
                        rs.getLong("id"),
                        rs.getString("nom"),
                        rs.getString("unite"),
                        rs.getDouble("stock_actuel"),
                        rs.getDouble("seuil_alerte")
                )
        );
    }

    public Ingredient ofId(Long id){
        String sql = "SELECT * FROM INGREDIENT WHERE id = ?";

        return jdbcTemplate.queryForObject(
                sql,
                new Object[]{id},
                (rs, rowNum) -> new Ingredient(
                        rs.getLong("id"),
                        rs.getString("nom"),
                        rs.getString("unite"),
                        rs.getDouble("stock_actuel"),
                        rs.getDouble("seuil_alerte")
                )
        );
    }
}
