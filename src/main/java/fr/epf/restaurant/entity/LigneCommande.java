package fr.epf.restaurant.entity;

public class LigneCommande {

    private Long id;
    private Long platId;
    private Double quantite;

    public LigneCommande(Long id, Long platId, Double quantite) {
        this.id = id;
        this.platId = platId;
        this.quantite = quantite;
    }

    public Long getId() {
        return id;
    }

    public Long getPlatId() {
        return platId;
    }

    public Double getQuantite() {
        return quantite;
    }
}
