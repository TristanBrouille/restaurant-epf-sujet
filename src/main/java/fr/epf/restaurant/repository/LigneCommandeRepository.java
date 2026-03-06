package fr.epf.restaurant.repository;

import fr.epf.restaurant.DTO.LigneCommandeDto;
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

    public Collection<LigneCommandeDto> ofCommandeId(Long id) {
        String sql = "SELECT * FROM LIGNE_COMMANDE_CLIENT WHERE commande_client_id = ?";

        return jdbcTemplate.query(
                sql,
                new Object[]{id},
                (rs, rowNum) -> new LigneCommandeDto(
                        rs.getLong("id"),
                        rs.getLong("plat_id"),
                        rs.getDouble("quantite")
                )
        );
    }

}
