package fr.epf.restaurant.dto;

import java.util.Collection;

public record CommandeFournisseurRequest(Long fournisseurId,
                                         Collection<LigneCommandeFournisseurUp> lignes) {
}
