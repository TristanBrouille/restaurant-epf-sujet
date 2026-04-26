package fr.epf.restaurant.dto;

import fr.epf.restaurant.entity.Fournisseur;
import fr.epf.restaurant.entity.StatutCommande;

import java.time.LocalDate;
import java.util.Collection;

public record CommandeFournisseurDto(Long id,
                                     Fournisseur fournisseur,
                                     LocalDate dateCommande,
                                     StatutCommande statut,
                                     Collection<LigneCommandeFournisseurDto> lignes) {
}
