package fr.epf.restaurant.DTO;

import java.util.List;

public record CommandeClientRequest(Long clientId, List<LigneCommandeClientUp> lignes) {
}
