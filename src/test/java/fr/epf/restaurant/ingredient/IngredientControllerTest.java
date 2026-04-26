package fr.epf.restaurant.ingredient;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import fr.epf.restaurant.controller.IngredientController;
import fr.epf.restaurant.dto.RecommandationCommande;
import fr.epf.restaurant.entity.Ingredient;
import fr.epf.restaurant.service.IngredientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class IngredientControllerTest {

    @Mock
    private IngredientService ingredientService;

    @InjectMocks
    private IngredientController ingredientController;

    @Test
    void shouldReturnAllIngredients() {
        when(ingredientService.ingredients()).thenReturn(
                List.of(new Ingredient(1L, "Oeuf", "unité", 10.0, 5.0)));

        Collection<Ingredient> result = ingredientController.ingredients();

        assertEquals(1, result.size());
        verify(ingredientService, times(1)).ingredients();
    }

    @Test
    void shouldReturnIngredientsBelowThreshold() {
        Ingredient enAlerte = new Ingredient(2L, "Lait", "L", 1.0, 5.0);
        when(ingredientService.alerte()).thenReturn(List.of(enAlerte));

        Collection<Ingredient> result = ingredientController.alerte();

        assertEquals(1, result.size());
        assertEquals("Lait", result.iterator().next().getNom());
    }

    @Test
    void shouldReturnEmptyListWhenNoIngredientsAreInAlert() {
        when(ingredientService.alerte()).thenReturn(Collections.emptyList());

        Collection<Ingredient> result = ingredientController.alerte();

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnAllSupplierPricesForAnIngredient() {
        RecommandationCommande rec = new RecommandationCommande(1L, "Metro", 1.5, 14.0);
        when(ingredientService.prixIngredients(1L)).thenReturn(List.of(rec));

        Collection<RecommandationCommande> result = ingredientController.prix(1L);

        assertEquals(1, result.size());
        verify(ingredientService, times(1)).prixIngredients(1L);
    }

    @Test
    void shouldReturnCheapestSupplierInHttp200() {
        RecommandationCommande rec = new RecommandationCommande(1L, "Metro", 1.5, 14.0);
        when(ingredientService.recommandation(1L)).thenReturn(rec);

        ResponseEntity<RecommandationCommande> result = ingredientController.recommandation(1L);

        assertEquals(200, result.getStatusCode().value());
        assertNotNull(result.getBody());
        assertEquals("Metro", result.getBody().fournisseurNom());
    }
}