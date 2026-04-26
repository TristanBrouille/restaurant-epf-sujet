package fr.epf.restaurant.service;

import fr.epf.restaurant.DTO.LigneCommandeFournisseurDto;
import fr.epf.restaurant.DTO.LigneCommandeFournisseurUp;
import fr.epf.restaurant.entity.LigneCommandeFournisseur;
import fr.epf.restaurant.repository.IngredientRepository;
import fr.epf.restaurant.repository.LigneCommandeRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class LigneCommandeFournisseurService {

    private final LigneCommandeRepository ligneCommandeRepository;
    private final IngredientRepository ingredientRepository;

    public LigneCommandeFournisseurService(LigneCommandeRepository ligneCommandeRepository, IngredientRepository ingredientRepository) {
        this.ligneCommandeRepository = ligneCommandeRepository;
        this.ingredientRepository = ingredientRepository;
    }

    public Collection<LigneCommandeFournisseurDto> getDto(Long id) {
        Collection<LigneCommandeFournisseur> lignes = ligneCommandeRepository.ofCommandeFournisseurId(id);
        return lignes.stream()
                .map(ligne ->
                        new LigneCommandeFournisseurDto(
                                ligne.getId(),
                                ingredientRepository.ofId(ligne.getIngredientId())
                                        .orElseThrow(() -> new RuntimeException("Aucun ingredient trouvé pour cet id")),
                                ligne.getQuantite(),
                                ligne.getPrixUnitaire())
                        )
                .toList();
    }

    public boolean checkIfIngredientExiste(LigneCommandeFournisseurUp ligne) {
        return ingredientRepository.ofId(ligne.ingredientId()).isPresent();
    }

}
