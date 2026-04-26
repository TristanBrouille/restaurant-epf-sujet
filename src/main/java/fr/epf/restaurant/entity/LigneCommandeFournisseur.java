package fr.epf.restaurant.entity;

public class LigneCommandeFournisseur {

    private final Long id;
    private final Long ingredientId;
    private final Double quantite;
    private final Double prixUnitaire;

    public LigneCommandeFournisseur(Long id,
                                    Long ingredientId,
                                    Double quantite,
                                    Double prixUnitaire) {
        this.id = id;
        this.ingredientId = ingredientId;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
    }

    public Long getId() {
        return id;
    }

    public Long getIngredientId() {
        return ingredientId;
    }

    public Double getQuantite() {
        return quantite;
    }

    public Double getPrixUnitaire() {
        return prixUnitaire;
    }
}
