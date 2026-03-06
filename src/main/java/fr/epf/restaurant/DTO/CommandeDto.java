package fr.epf.restaurant.DTO;

import fr.epf.restaurant.entity.Client;
import fr.epf.restaurant.entity.StatutCommande;

import java.time.LocalDate;
import java.util.Collection;

public record CommandeDto(Long id, Client client, LocalDate dateCommande, StatutCommande statut, Collection<LigneCommandeDto> lignes) {
}
