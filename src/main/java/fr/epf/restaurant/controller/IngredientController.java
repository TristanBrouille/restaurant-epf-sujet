package fr.epf.restaurant.controller;

import fr.epf.restaurant.DTO.RecommandationCommande;
import fr.epf.restaurant.entity.Ingredient;
import fr.epf.restaurant.service.IngredientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("api/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping
    public Collection<Ingredient> ingredients(){
        return ingredientService.ingredients();
    }

    @GetMapping("/alertes")
    public Collection<Ingredient> alerte(){
        return ingredientService.alerte();
    }

    @GetMapping("/{id}/prix")
    public Collection<RecommandationCommande> prix(@PathVariable Long id){
        return ingredientService.prixIngredients(id);
    }

    @GetMapping("/{id}/recommandation")
    public ResponseEntity<RecommandationCommande> recommandation(@PathVariable Long id){
        return ResponseEntity.ok(ingredientService.Recommandation(id));
    }
}
