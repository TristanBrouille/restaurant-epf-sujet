package fr.epf.restaurant.dto;

public record RecommandationCommande(Long fournisseurId,
                                     String fournisseurNom,
                                     Double prixUnitaire,
                                     Double quantiteRecommandee) {
}
