package fr.epf.restaurant.dto;

import fr.epf.restaurant.entity.Client;
import fr.epf.restaurant.entity.StatutCommande;

import java.time.LocalDate;
import java.util.Collection;

public record CommandeClientDto(Long id, Client client,
                                LocalDate dateCommande,
                                StatutCommande statut,
                                Collection<LigneCommandeClientDto> lignes) {
}
