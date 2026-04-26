package fr.epf.restaurant.dto;

import java.util.List;

public record CommandeClientRequest(Long clientId,
                                    List<LigneCommandeClientUp> lignes) {
}
