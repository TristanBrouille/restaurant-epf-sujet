package fr.epf.restaurant.ingredient;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import fr.epf.restaurant.dto.RecommandationCommande;
import fr.epf.restaurant.entity.Fournisseur;
import fr.epf.restaurant.entity.Ingredient;
import fr.epf.restaurant.repository.FournisseurIngredientRepository;
import fr.epf.restaurant.repository.IngredientRepository;
import fr.epf.restaurant.service.IngredientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class IngredientServiceTest {

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private FournisseurIngredientRepository fournisseurIngredientRepository;

    @InjectMocks
    private IngredientService ingredientService;

    @Test
    void shouldReturnIngredientWhenStockIsStrictlyBelowThreshold() {
        Ingredient enAlerte = new Ingredient(1L, "Farine", "kg", 4.0, 5.0);
        Ingredient stockOk = new Ingredient(2L, "Sel", "g", 5.0, 5.0);
        when(ingredientRepository.ofAll()).thenReturn(List.of(enAlerte, stockOk));

        Collection<Ingredient> result = ingredientService.alerte();

        assertEquals(1, result.size());
        assertEquals("Farine", result.iterator().next().getNom());
    }

    @Test
    void shouldNotIncludeIngredientWhenStockEqualsThreshold() {
        Ingredient stockEgalSeuil = new Ingredient(1L, "Sel", "g", 5.0, 5.0);
        when(ingredientRepository.ofAll()).thenReturn(List.of(stockEgalSeuil));

        Collection<Ingredient> result = ingredientService.alerte();

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldNotIncludeIngredientWhenStockIsAboveThreshold() {
        Ingredient stockAbondant = new Ingredient(1L, "Beurre", "kg", 10.0, 5.0);
        when(ingredientRepository.ofAll()).thenReturn(List.of(stockAbondant));

        Collection<Ingredient> result = ingredientService.alerte();

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnAllIngredientsWithInsufficientStock() {
        Ingredient i1 = new Ingredient(1L, "Lait", "L", 1.0, 5.0);
        Ingredient i2 = new Ingredient(2L, "Sucre", "kg", 0.0, 2.0);
        Ingredient i3 = new Ingredient(3L, "Farine", "kg", 10.0, 3.0);
        when(ingredientRepository.ofAll()).thenReturn(List.of(i1, i2, i3));

        Collection<Ingredient> result = ingredientService.alerte();

        assertEquals(2, result.size());
    }

    @Test
    void shouldReturnAllAvailableSuppliersForAnIngredient() {
        Ingredient ingredient = new Ingredient(1L, "Oeuf", "unité", 3.0, 10.0);
        Fournisseur f1 = new Fournisseur(1L, "Metro", "contact1", "metro@test.fr");
        Fournisseur f2 = new Fournisseur(2L, "Transgourmet", "contact2", "trans@test.fr");
        when(ingredientRepository.ofId(1L)).thenReturn(Optional.of(ingredient));
        when(fournisseurIngredientRepository.ofIngredientId(1L))
                .thenReturn(Map.of(1.5, f1, 2.0, f2));

        Collection<RecommandationCommande> result = ingredientService.prixIngredients(1L);

        assertEquals(2, result.size());
    }

    @Test
    void shouldCalculateQuantityTwiceTheGapWhenStockIsBelowThreshold() {
        Ingredient ingredient = new Ingredient(1L, "Oeuf", "unité", 3.0, 10.0);
        Fournisseur f1 = new Fournisseur(1L, "Metro", "contact", "metro@test.fr");
        when(ingredientRepository.ofId(1L)).thenReturn(Optional.of(ingredient));
        when(fournisseurIngredientRepository.ofIngredientId(1L)).thenReturn(Map.of(1.5, f1));

        Collection<RecommandationCommande> result = ingredientService.prixIngredients(1L);

        assertEquals(14.0, result.iterator().next().quantiteRecommandee());
    }

    @Test
    void shouldCalculateQuantityEqualToThresholdWhenStockIsSufficient() {
        Ingredient ingredient = new Ingredient(1L, "Sel", "g", 5.0, 5.0);
        Fournisseur f1 = new Fournisseur(1L, "Metro", "contact", "metro@test.fr");
        when(ingredientRepository.ofId(1L)).thenReturn(Optional.of(ingredient));
        when(fournisseurIngredientRepository.ofIngredientId(1L)).thenReturn(Map.of(0.5, f1));

        Collection<RecommandationCommande> result = ingredientService.prixIngredients(1L);

        assertEquals(5.0, result.iterator().next().quantiteRecommandee());
    }

    @Test
    void shouldThrowExceptionWhenIngredientDoesNotExist() {
        when(ingredientRepository.ofId(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> ingredientService.prixIngredients(99L));
    }

    @Test
    void shouldReturnTheCheapestSupplier() {
        Ingredient ingredient = new Ingredient(1L, "Oeuf", "unité", 3.0, 10.0);
        Fournisseur f1 = new Fournisseur(1L, "Metro", "contact1", "metro@test.fr");
        Fournisseur f2 = new Fournisseur(2L, "Cher", "contact2", "cher@test.fr");
        when(ingredientRepository.ofId(1L)).thenReturn(Optional.of(ingredient));
        when(fournisseurIngredientRepository.ofIngredientId(1L))
                .thenReturn(Map.of(1.5, f1, 3.0, f2));

        RecommandationCommande result = ingredientService.recommandation(1L);

        assertNotNull(result);
        assertEquals(1.5, result.prixUnitaire());
        assertEquals("Metro", result.fournisseurNom());
    }

    @Test
    void shouldReturnNullWhenNoSuppliersFound() {
        Ingredient ingredient = new Ingredient(1L, "Oeuf", "unité", 3.0, 10.0);
        when(ingredientRepository.ofId(1L)).thenReturn(Optional.of(ingredient));
        when(fournisseurIngredientRepository.ofIngredientId(1L)).thenReturn(Map.of());

        RecommandationCommande result = ingredientService.recommandation(1L);

        assertNull(result);
    }
}