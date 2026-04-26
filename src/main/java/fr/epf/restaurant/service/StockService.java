package fr.epf.restaurant.service;

import fr.epf.restaurant.entity.Ingredient;
import fr.epf.restaurant.repository.IngredientRepository;
import fr.epf.restaurant.repository.LigneCommandeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class StockService {

    private final IngredientRepository ingredientRepository;
    private final LigneCommandeRepository ligneCommandeRepository;

    public StockService(IngredientRepository ingredientRepository, LigneCommandeRepository ligneCommandeRepository) {
        this.ingredientRepository = ingredientRepository;
        this.ligneCommandeRepository = ligneCommandeRepository;
    }

    @Transactional
    public boolean lowerStockOfPlat(Long platId) {

        Map<Ingredient, Double> ingredientsAndQuantity = ingredientRepository.ofPlatId(platId);

        for (Map.Entry<Ingredient, Double> entry : ingredientsAndQuantity.entrySet()) {

            Ingredient ingredient = entry.getKey();
            Double quantite = entry.getValue();

            if (ingredient.getStockActuel() < quantite) {
                throw new RuntimeException("Stock insuffisant pour " + ingredient.getNom());
            }

            ingredientRepository.updateStock(
                    ingredient.getId(),
                    ingredient.getStockActuel() - quantite
            );
        }

        return true;
    }

    public void increaseStock(Long commandeId) {
        ligneCommandeRepository.ofCommandeFournisseurId(commandeId)
                .forEach(ligne -> {
                    Ingredient ingredient = ingredientRepository.ofId(ligne.getIngredientId()).orElseThrow();
                    ingredientRepository.updateStock(ligne.getIngredientId(), ligne.getQuantite()+ingredient.getStockActuel());
                });
    }
}
