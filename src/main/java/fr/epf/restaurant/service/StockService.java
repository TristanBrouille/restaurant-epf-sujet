package fr.epf.restaurant.service;

import fr.epf.restaurant.entity.Ingredient;
import fr.epf.restaurant.repository.IngredientRepository;
import fr.epf.restaurant.repository.PlatIngredientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Map;

@Service
public class StockService {

    private final IngredientRepository ingredientRepository;

    public StockService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @Transactional
    public boolean raiseStockOfPlat(Long platId) {

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
}
