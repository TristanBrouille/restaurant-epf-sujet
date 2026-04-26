package fr.epf.restaurant.repository;


import fr.epf.restaurant.entity.Plat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PlatRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Plat> getAll() {
        String sql = "SELECT * FROM PLAT";

        RowMapper<Plat> mapper = (rs, rowNum) -> {
            return new Plat(
                    rs.getLong("id"),
                    rs.getString("nom"),
                    rs.getString("description"),
                    rs.getDouble("prix")
            );
        };

        return jdbcTemplate.query(sql, mapper);
    }

    public Optional<Plat> ofId(Long id) {
        String sql = "SELECT * FROM PLAT WHERE id = ?";

        return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                        sql,
                        new Object[]{id},
                        (rs, rowNum) -> new Plat(
                                rs.getLong("id"),
                                rs.getString("nom"),
                                rs.getString("description"),
                                rs.getDouble("prix")
                        )
                ));
    }

}
