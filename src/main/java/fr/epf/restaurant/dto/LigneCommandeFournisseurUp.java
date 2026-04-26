package fr.epf.restaurant.dto;

public record LigneCommandeFournisseurUp(Long ingredientId,
                                         Double quantite,
                                         Double prixUnitaire) {
}
