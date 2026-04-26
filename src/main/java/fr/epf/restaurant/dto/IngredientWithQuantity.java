package fr.epf.restaurant.dto;

import fr.epf.restaurant.entity.Ingredient;

public record IngredientWithQuantity(Ingredient ingredient,
                                     Double quantity) {
}
