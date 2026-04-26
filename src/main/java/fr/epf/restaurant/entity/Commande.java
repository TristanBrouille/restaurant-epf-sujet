package fr.epf.restaurant.entity;

import java.time.LocalDate;

public class Commande {

    private final Long id;
    private final Long clientId;
    private final LocalDate dateCommande;
    private StatutCommande statut;

    public Commande(Long id, Long clientId,
                    LocalDate dateCommande,
                    StatutCommande statut) {
        this.id = id;
        this.clientId = clientId;
        this.dateCommande = dateCommande;
        this.statut = statut;
    }

    public Long getId() {
        return id;
    }

    public Long getClientId() {
        return clientId;
    }

    public LocalDate getDateCommande() {
        return dateCommande;
    }

    public StatutCommande getStatut() {
        return statut;
    }

    public void setStatut(StatutCommande statut) {
        this.statut = statut;
    }
}
