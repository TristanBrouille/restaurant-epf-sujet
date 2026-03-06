package fr.epf.restaurant.repository;

import fr.epf.restaurant.DTO.CommandeRequest;
import fr.epf.restaurant.DTO.LigneCommandeDto;
import fr.epf.restaurant.entity.Commande;
import fr.epf.restaurant.entity.StatutCommande;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class CommandeRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void add(CommandeRequest request) {

        String sqlCommande = """
        INSERT INTO COMMANDE_CLIENT (client_id, date_commande, statut)
        VALUES (?, ?, ?)
    """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlCommande, new String[]{"ID"}); // <-- ici
            ps.setLong(1, request.clientId());
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(3, StatutCommande.EN_ATTENTE.name());
            return ps;
        }, keyHolder);

        Long commandeId = keyHolder.getKey().longValue();

        // 2️⃣ Insérer les lignes
        String sqlLigne = """
        INSERT INTO LIGNE_COMMANDE_CLIENT
        (commande_client_id, plat_id, quantite)
        VALUES (?, ?, ?)
    """;

        for (LigneCommandeDto ligne : request.lignes()) {
            jdbcTemplate.update(
                    sqlLigne,
                    commandeId,
                    ligne.platId(),
                    ligne.quantite()
            );
        }

    }

    public Collection<Commande> ofAll(){
        String sql = "SELECT * FROM COMMANDE_CLIENT";

        RowMapper<Commande> mapper = (rs, rowNum) -> {
            Timestamp ts = rs.getTimestamp("date_commande");
            LocalDate dateCommande = ts != null ? ts.toLocalDateTime().toLocalDate() : null;

            StatutCommande statut = StatutCommande.valueOf(rs.getString("statut"));

            return new Commande(
                    rs.getLong("id"),
                    rs.getLong("client_id"),
                    dateCommande,
                    statut
            );
        };

        return jdbcTemplate.query(sql, mapper);
    }

    public Optional<Commande> getLastCommandeByClient(Long clientId) {

        String sql = """
        SELECT *
        FROM COMMANDE_CLIENT
        WHERE client_id = ?
        ORDER BY date_commande DESC
        LIMIT 1
    """;

        List<Commande> commandes = jdbcTemplate.query(
                sql,
                new Object[]{clientId},
                (rs, rowNum) -> {
                    Timestamp ts = rs.getTimestamp("date_commande");
                    LocalDate date = ts.toLocalDateTime().toLocalDate();

                    return new Commande(
                            rs.getLong("id"),
                            rs.getLong("client_id"),
                            date,
                            StatutCommande.valueOf(rs.getString("statut"))
                    );
                }
        );

        // retourne Optional vide si aucune commande
        if (commandes.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(commandes.get(0));
        }
    }
}
