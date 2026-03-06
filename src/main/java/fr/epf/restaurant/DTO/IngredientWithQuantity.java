package fr.epf.restaurant.DTO;

import fr.epf.restaurant.entity.Ingredient;

public record IngredientWithQuantity(Ingredient ingredient, Double quantity) {
}
