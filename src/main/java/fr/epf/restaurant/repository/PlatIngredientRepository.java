package fr.epf.restaurant.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class PlatIngredientRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Map<Long, Double> ofPlatId(Long id) {
        String sql = "SELECT ingredient_id, quantite_requise FROM PLAT_INGREDIENT WHERE plat_id = ?";

        List<Map.Entry<Long, Double>> list = jdbcTemplate.query(
                sql,
                new Object[]{id},
                (rs, rowNum) -> Map.entry(
                        rs.getLong("ingredient_id"),
                        rs.getDouble("quantite_requise")
                )
        );

        return list.stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
