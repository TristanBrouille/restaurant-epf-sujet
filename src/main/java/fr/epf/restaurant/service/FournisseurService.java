package fr.epf.restaurant.service;

import fr.epf.restaurant.dto.IngredientPrix;
import fr.epf.restaurant.entity.Fournisseur;
import fr.epf.restaurant.entity.Ingredient;
import fr.epf.restaurant.repository.FournisseurIngredientRepository;
import fr.epf.restaurant.repository.FournisseurRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

@Service
public class FournisseurService {

    private final FournisseurRepository fournisseurRepository;
    private final FournisseurIngredientRepository fournisseurIngredientRepository;

    public FournisseurService(
            FournisseurRepository fournisseurRepository,
            FournisseurIngredientRepository fournisseurIngredientRepository) {
        this.fournisseurRepository = fournisseurRepository;
        this.fournisseurIngredientRepository = fournisseurIngredientRepository;
    }

    public Collection<Fournisseur> fournisseurs() {
        return fournisseurRepository.getAll();
    }

    public Collection<IngredientPrix> catalogueFournisseur(
            Long id) {
        Map<Double, Ingredient> ingredientCatalogue = fournisseurIngredientRepository.ofFournisseurId(
                id);
        return ingredientCatalogue.entrySet()
                .stream()
                .map(entry -> new IngredientPrix(
                        entry.getValue().getId(),
                        entry.getValue().getNom(),
                        entry.getValue().getUnite(),
                        entry.getKey()
                ))
                .toList();
    }

}
