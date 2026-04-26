package fr.epf.restaurant.entity;

public class LigneCommandeClient {

    private final Long id;
    private final Long platId;
    private final Double quantite;

    public LigneCommandeClient(Long id, Long platId,
                               Double quantite) {
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
