package fr.epf.restaurant.dto;


import java.util.Collection;

public record RecetteDto(Long id, String nom,
                         String description, Double prix,
                         Collection<IngredientWithQuantity> ingredients) {
}
