package fr.epf.restaurant.repository;

import fr.epf.restaurant.entity.LigneCommandeClient;
import fr.epf.restaurant.entity.LigneCommandeFournisseur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public class LigneCommandeRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Collection<LigneCommandeClient> ofCommandeClientId(
            Long id) {
        String sql = "SELECT * FROM LIGNE_COMMANDE_CLIENT WHERE commande_client_id = ?";

        return jdbcTemplate.query(
                sql,
                new Object[]{id},
                (rs, rowNum) -> new LigneCommandeClient(
                        rs.getLong("id"),
                        rs.getLong("plat_id"),
                        rs.getDouble("quantite")
                )
        );
    }

    public Collection<LigneCommandeFournisseur> ofCommandeFournisseurId(
            Long id) {
        String sql = "SELECT * FROM LIGNE_COMMANDE_FOURNISSEUR WHERE commande_fournisseur_id = ?";

        return jdbcTemplate.query(
                sql,
                new Object[]{id},
                (rs, rowNum) -> new LigneCommandeFournisseur(
                        rs.getLong("id"),
                        rs.getLong("ingredient_id"),
                        rs.getDouble("quantite_commandee"),
                        rs.getDouble("prix_unitaire")
                )
        );
    }

}
