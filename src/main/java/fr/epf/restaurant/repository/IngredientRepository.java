package fr.epf.restaurant.repository;

import fr.epf.restaurant.entity.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

    public Optional<Ingredient> ofId(Long id){
        String sql = "SELECT * FROM INGREDIENT WHERE id = ?";

        return Optional.ofNullable(jdbcTemplate.queryForObject(
                sql,
                new Object[]{id},
                (rs, rowNum) -> new Ingredient(
                        rs.getLong("id"),
                        rs.getString("nom"),
                        rs.getString("unite"),
                        rs.getDouble("stock_actuel"),
                        rs.getDouble("seuil_alerte")
                )
        ));
    }

    public Map<Ingredient, Double> ofPlatId(Long platId) {
        return jdbcTemplate.query(
                """
                SELECT i.*, pi.quantite_requise
                FROM INGREDIENT i
                JOIN PLAT_INGREDIENT pi ON i.id = pi.ingredient_id
                WHERE pi.plat_id = ?
                """,
                rs -> {
                    Map<Ingredient, Double> result = new HashMap<>();

                    while (rs.next()) {
                        Ingredient ingredient = new Ingredient(
                                rs.getLong("id"),
                                rs.getString("nom"),
                                rs.getString("unite"),
                                rs.getDouble("stock_actuel"),
                                rs.getDouble("seuil_alerte")
                        );

                        double quantite = rs.getDouble("quantite_requise");

                        result.put(ingredient, quantite);
                    }

                    return result;
                },
                platId
        );
    }

    public void updateStock(Long id, double quantity) {
        jdbcTemplate.update(
                "UPDATE INGREDIENT SET stock_actuel = ? WHERE id = ?",
                quantity,
                id
        );
    }
}
