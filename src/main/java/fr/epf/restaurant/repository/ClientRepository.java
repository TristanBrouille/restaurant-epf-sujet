package fr.epf.restaurant.repository;

import fr.epf.restaurant.entity.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ClientRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Client> getAll() {
        String sql = "SELECT * FROM CLIENT";

        RowMapper<Client> mapper = (rs, rowNum)-> {
            return new Client(
                    rs.getLong("id"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("email"),
                    rs.getString("telephone")
            );
        };

        return jdbcTemplate.query(sql, mapper);
    }

    public Optional<Client> ofId(Long id){
        String sql = "SELECT * FROM CLIENT WHERE id = ?";

        return Optional.ofNullable(jdbcTemplate.queryForObject(
                sql,
                new Object[]{id},
                (rs, rowNum) -> new Client(
                        rs.getLong("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("telephone")
                )
        ));
    }


}
