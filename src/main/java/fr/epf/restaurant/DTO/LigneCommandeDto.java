package fr.epf.restaurant.DTO;

import fr.epf.restaurant.entity.Plat;

public record LigneCommandeDto(Long id, Plat plat, Double quantite) {
}
