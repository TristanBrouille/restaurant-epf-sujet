package fr.epf.restaurant.repository;


import fr.epf.restaurant.entity.Fournisseur;
import fr.epf.restaurant.entity.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.stereotype.Repository;


import java.util.HashMap;
import java.util.Map;

@Repository
public class FournisseurIngredientRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Map<Double, Fournisseur> ofIngredientId(
            Long id) {
        String sql = """
                SELECT f.id, f.nom, f.contact, f.email, fi.prix_unitaire
                FROM FOURNISSEUR_INGREDIENT fi
                JOIN FOURNISSEUR f ON fi.fournisseur_id = f.id
                WHERE fi.ingredient_id = ?
                """;

        return jdbcTemplate.query(
                sql,
                new Object[]{id},
                rs -> {
                    Map<Double, Fournisseur> map = new HashMap<>();

                    while (rs.next()) {

                        double prix = rs.getDouble(
                                "prix_unitaire");

                        Fournisseur fournisseur = new Fournisseur(
                                rs.getLong("id"),
                                rs.getString("nom"),
                                rs.getString("contact"),
                                rs.getString("email")
                        );

                        map.put(prix, fournisseur);
                    }

                    return map;
                }
        );
    }

    public Map<Double, Ingredient> ofFournisseurId(
            Long id) {
        String sql = """
                SELECT i.id, i.nom, i.unite, fi.prix_unitaire
                FROM FOURNISSEUR_INGREDIENT fi
                JOIN INGREDIENT i ON fi.ingredient_id = i.id
                WHERE fi.fournisseur_id = ?
                """;

        return jdbcTemplate.query(
                sql,
                new Object[]{id},
                rs -> {
                    Map<Double, Ingredient> map = new HashMap<>();

                    while (rs.next()) {

                        double prix = rs.getDouble(
                                "prix_unitaire");

                        Ingredient ingredient = new Ingredient(
                                rs.getLong("id"),
                                rs.getString("nom"),
                                rs.getString("unite")
                        );

                        map.put(prix, ingredient);
                    }

                    return map;
                }
        );
    }
}
