package fr.epf.restaurant.DTO;

import fr.epf.restaurant.entity.Ingredient;

public record LigneCommandeFournisseurDto(Long id, Ingredient ingredient, Double quantiteCommandee, Double prixUnitaire) {
}
