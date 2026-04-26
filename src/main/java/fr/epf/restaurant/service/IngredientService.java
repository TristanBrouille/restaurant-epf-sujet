package fr.epf.restaurant.service;

import fr.epf.restaurant.dto.RecommandationCommande;
import fr.epf.restaurant.entity.Fournisseur;
import fr.epf.restaurant.entity.Ingredient;
import fr.epf.restaurant.repository.FournisseurIngredientRepository;
import fr.epf.restaurant.repository.IngredientRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;

@Service
public class IngredientService {

    public final IngredientRepository ingredientRepository;
    public final FournisseurIngredientRepository fournisseurIngredientRepository;

    public IngredientService(
            IngredientRepository ingredientRepository,
            FournisseurIngredientRepository fournisseurIngredientRepository) {
        this.ingredientRepository = ingredientRepository;
        this.fournisseurIngredientRepository = fournisseurIngredientRepository;
    }

    public Collection<Ingredient> ingredients() {
        return ingredientRepository.ofAll();
    }

    public Collection<Ingredient> alerte() {
        return ingredients().stream()
                .filter(ingredient -> ingredient.getStockActuel() < ingredient.getSeuilAlerte())
                .toList();
    }

    public Collection<RecommandationCommande> prixIngredients(
            Long id) {

        Ingredient ingredient = ingredientRepository.ofId(
                        id)
                .orElseThrow(() -> new RuntimeException(
                        "Aucun ingredient trouvé pour cet id"));
        Map<Double, Fournisseur> fournisseurIngredients = fournisseurIngredientRepository.ofIngredientId(
                id);

        double quantiteRecommandee = (ingredient.getSeuilAlerte() > ingredient.getStockActuel())
                ? 2 * (ingredient.getSeuilAlerte() - ingredient.getStockActuel())
                : ingredient.getSeuilAlerte();

        return fournisseurIngredients.entrySet()
                .stream()
                .map(entry -> new RecommandationCommande(
                        entry.getValue().getId(),
                        entry.getValue().getNom(),
                        entry.getKey(),
                        quantiteRecommandee
                ))
                .toList();
    }

    public RecommandationCommande recommandation(Long id) {
        return prixIngredients(id)
                .stream()
                .min(Comparator.comparing(
                        RecommandationCommande::prixUnitaire))
                .orElse(null);
    }
}
