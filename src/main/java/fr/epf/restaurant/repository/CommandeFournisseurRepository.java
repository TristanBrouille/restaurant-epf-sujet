package fr.epf.restaurant.repository;

import fr.epf.restaurant.dto.CommandeFournisseurRequest;
import fr.epf.restaurant.dto.LigneCommandeFournisseurUp;
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
import java.util.Objects;
import java.util.Optional;

@Repository
public class CommandeFournisseurRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void add(CommandeFournisseurRequest request) {

        String sqlCommande = """
                    INSERT INTO COMMANDE_FOURNISSEUR (fournisseur_id, date_commande, statut)
                    VALUES (?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    sqlCommande, new String[]{"ID"});
            ps.setLong(1, request.fournisseurId());
            ps.setTimestamp(2,
                    Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(3,
                    StatutCommande.EN_ATTENTE.name());
            return ps;
        }, keyHolder);

        Long commandeId = Objects.requireNonNull(
                        keyHolder.getKey())
                .longValue();

        String sqlLigne = """
                    INSERT INTO LIGNE_COMMANDE_FOURNISSEUR
                    (commande_fournisseur_id, ingredient_id, quantite_commandee, prix_unitaire)
                    VALUES (?, ?, ?, ?)
                """;

        for (LigneCommandeFournisseurUp ligne : request.lignes()) {
            jdbcTemplate.update(
                    sqlLigne,
                    commandeId,
                    ligne.ingredientId(),
                    ligne.quantite(),
                    ligne.prixUnitaire()
            );
        }

    }

    public Optional<Commande> getLastCommandeByFournisseur(
            Long fournisseurId) {

        String sql = """
                    SELECT *
                    FROM COMMANDE_FOURNISSEUR
                    WHERE fournisseur_id = ?
                    ORDER BY date_commande DESC
                    LIMIT 1
                """;

        List<Commande> commandes = jdbcTemplate.query(
                sql,
                new Object[]{fournisseurId},
                (rs, rowNum) -> {
                    Timestamp ts = rs.getTimestamp(
                            "date_commande");
                    LocalDate date = ts.toLocalDateTime()
                            .toLocalDate();

                    return new Commande(
                            rs.getLong("id"),
                            rs.getLong("fournisseur_id"),
                            date,
                            StatutCommande.valueOf(
                                    rs.getString("statut"))
                    );
                }
        );

        if (commandes.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(commandes.getFirst());
        }
    }

    public Collection<Commande> ofAll() {
        String sql = "SELECT * FROM COMMANDE_FOURNISSEUR";

        RowMapper<Commande> mapper = (rs, rowNum) -> {
            Timestamp ts = rs.getTimestamp("date_commande");
            LocalDate dateCommande = ts != null ? ts.toLocalDateTime()
                                                  .toLocalDate() : null;

            StatutCommande statut = StatutCommande.valueOf(
                    rs.getString("statut"));

            return new Commande(
                    rs.getLong("id"),
                    rs.getLong("fournisseur_id"),
                    dateCommande,
                    statut
            );
        };

        return jdbcTemplate.query(sql, mapper);
    }

    public Collection<Commande> ofStatut(
            StatutCommande statut) {
        String sql = "SELECT * FROM COMMANDE_FOURNISSEUR WHERE statut = ?";

        RowMapper<Commande> mapper = (rs, rowNum) -> {
            Timestamp ts = rs.getTimestamp("date_commande");
            LocalDate date = ts != null ? ts.toLocalDateTime()
                                          .toLocalDate() : null;
            return new Commande(
                    rs.getLong("id"),
                    rs.getLong("fournisseur_id"),
                    date,
                    StatutCommande.valueOf(
                            rs.getString("statut"))
            );
        };

        return jdbcTemplate.query(sql, mapper,
                statut.name());
    }


    public Optional<Commande> ofId(Long id) {
        String sql = "SELECT * FROM COMMANDE_FOURNISSEUR WHERE id=?";

        List<Commande> commandes = jdbcTemplate.query(
                sql,
                new Object[]{id},
                (rs, rowNum) -> {
                    Timestamp ts = rs.getTimestamp(
                            "date_commande");
                    LocalDate date = ts.toLocalDateTime()
                            .toLocalDate();

                    return new Commande(
                            rs.getLong("id"),
                            rs.getLong("fournisseur_id"),
                            date,
                            StatutCommande.valueOf(
                                    rs.getString("statut"))
                    );
                }
        );

        if (commandes.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(commandes.getFirst());
        }

    }

    public void updateStatut(Long id,
                             StatutCommande statut) {
        jdbcTemplate.update(
                "UPDATE COMMANDE_FOURNISSEUR SET statut = ? WHERE id = ?",
                statut.name(),
                id
        );
    }

    public void delete(Long id) {
        jdbcTemplate.update(
                "DELETE FROM LIGNE_COMMANDE_FOURNISSEUR WHERE id = ?",
                id);
        jdbcTemplate.update(
                "DELETE FROM COMMANDE_FOURNISSEUR WHERE id = ?",
                id);
    }
}
