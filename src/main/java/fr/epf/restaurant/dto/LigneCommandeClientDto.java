package fr.epf.restaurant.dto;

import fr.epf.restaurant.entity.Plat;

public record LigneCommandeClientDto(Long id, Plat plat,
                                     Double quantite) {
}
