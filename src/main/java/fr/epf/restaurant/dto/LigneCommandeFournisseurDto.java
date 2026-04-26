package fr.epf.restaurant.dto;

import fr.epf.restaurant.entity.Ingredient;

public record LigneCommandeFournisseurDto(Long id,
                                          Ingredient ingredient,
                                          Double quantiteCommandee,
                                          Double prixUnitaire) {
}
