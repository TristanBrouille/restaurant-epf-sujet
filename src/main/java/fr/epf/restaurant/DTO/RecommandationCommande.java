package fr.epf.restaurant.DTO;

public record RecommandationCommande(Long fournisseurId, String fournisseurNom, Double prixUnitaire, Double quantiteRecommandee) {
}
