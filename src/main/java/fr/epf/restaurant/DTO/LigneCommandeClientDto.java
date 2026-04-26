package fr.epf.restaurant.DTO;

import fr.epf.restaurant.entity.Plat;

public record LigneCommandeClientDto(Long id, Plat plat, Double quantite) {
}
