package fr.epf.restaurant.DTO;

import java.util.Collection;

public record CommandeFournisseurRequest(Long fournisseurId, Collection<LigneCommandeFournisseurUp> lignes) {
}
