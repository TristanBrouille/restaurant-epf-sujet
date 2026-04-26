package fr.epf.restaurant.repository;

import fr.epf.restaurant.entity.Fournisseur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public class FournisseurRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Collection<Fournisseur> getAll() {
        String sql = "SELECT * FROM FOURNISSEUR";

        RowMapper<Fournisseur> mapper = (rs, rowNum)-> {
            return new Fournisseur(
                    rs.getLong("id"),
                    rs.getString("nom"),
                    rs.getString("contact"),
                    rs.getString("email")
            );
        };

        return jdbcTemplate.query(sql, mapper);
    }

    public Optional<Fournisseur> ofId(Long id){
        String sql = "SELECT * FROM FOURNISSEUR WHERE id = ?";

        return Optional.ofNullable(jdbcTemplate.queryForObject(
                sql,
                new Object[]{id},
                (rs, rowNum) -> new Fournisseur(
                        rs.getLong("id"),
                        rs.getString("nom"),
                        rs.getString("contact"),
                        rs.getString("email")
                )
        ));
    }

}
