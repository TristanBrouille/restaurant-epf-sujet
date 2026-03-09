package fr.epf.restaurant.DTO;

import java.util.List;

public record CommandeRequest(Long clientId, List<LigneCommandeUp> lignes) {
}
