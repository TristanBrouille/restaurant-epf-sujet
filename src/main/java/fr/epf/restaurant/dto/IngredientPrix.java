package fr.epf.restaurant.dto;

public record IngredientPrix(Long ingredientId,
                             String ingredientNom,
                             String ingredientUnite,
                             Double prixUnitaire) {
}
