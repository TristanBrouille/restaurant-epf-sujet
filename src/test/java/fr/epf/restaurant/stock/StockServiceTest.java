package fr.epf.restaurant.stock;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import fr.epf.restaurant.entity.Ingredient;
import fr.epf.restaurant.entity.LigneCommandeFournisseur;
import fr.epf.restaurant.repository.IngredientRepository;
import fr.epf.restaurant.repository.LigneCommandeRepository;
import fr.epf.restaurant.service.StockService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private LigneCommandeRepository ligneCommandeRepository;

    @InjectMocks
    private StockService stockService;

    @Test
    void shouldDecrementStockWhenSufficientIngredientsExistForPlat() {
        Ingredient ingredient = new Ingredient(1L, "Oeufs", "unité", 10.0, 5.0);
        when(ingredientRepository.ofPlatId(1L)).thenReturn(Map.of(ingredient, 3.0));

        boolean result = stockService.lowerStockOfPlat(1L);

        assertTrue(result);
        verify(ingredientRepository).updateStock(1L, 7.0);
    }

    @Test
    void shouldThrowExceptionWhenStockIsInsufficientForPlat() {
        Ingredient ingredient = new Ingredient(1L, "Lardons", "g", 2.0, 5.0);
        when(ingredientRepository.ofPlatId(1L)).thenReturn(Map.of(ingredient, 5.0));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> stockService.lowerStockOfPlat(1L));
        assertTrue(ex.getMessage().contains("Lardons"));
    }

    @Test
    void shouldReturnTrueWhenAllIngredientsAreSuccessfullyDeducted() {
        Ingredient i1 = new Ingredient(1L, "Pâte", "g", 200.0, 50.0);
        Ingredient i2 = new Ingredient(2L, "Crème", "mL", 100.0, 20.0);
        when(ingredientRepository.ofPlatId(1L)).thenReturn(Map.of(i1, 150.0, i2, 50.0));

        boolean result = stockService.lowerStockOfPlat(1L);

        assertTrue(result);
        verify(ingredientRepository).updateStock(1L, 50.0);
        verify(ingredientRepository).updateStock(2L, 50.0);
    }

    @Test
    void shouldAddDeliveredQuantityToCurrentStock() {
        LigneCommandeFournisseur ligne = new LigneCommandeFournisseur(1L, 2L, 10.0, 1.5);
        Ingredient ingredient = new Ingredient(2L, "Farine", "kg", 3.0, 5.0);
        when(ligneCommandeRepository.ofCommandeFournisseurId(10L)).thenReturn(List.of(ligne));
        when(ingredientRepository.ofId(2L)).thenReturn(Optional.of(ingredient));

        stockService.increaseStock(10L);

        verify(ingredientRepository).updateStock(2L, 13.0);
    }

    @Test
    void shouldUpdateMultipleIngredientsFromSameSupplierOrder() {
        LigneCommandeFournisseur ligne1 = new LigneCommandeFournisseur(1L, 1L, 5.0, 1.0);
        LigneCommandeFournisseur ligne2 = new LigneCommandeFournisseur(2L, 2L, 8.0, 2.0);
        Ingredient i1 = new Ingredient(1L, "Sel", "g", 2.0, 3.0);
        Ingredient i2 = new Ingredient(2L, "Sucre", "g", 1.0, 5.0);
        when(ligneCommandeRepository.ofCommandeFournisseurId(5L)).thenReturn(List.of(ligne1, ligne2));
        when(ingredientRepository.ofId(1L)).thenReturn(Optional.of(i1));
        when(ingredientRepository.ofId(2L)).thenReturn(Optional.of(i2));

        stockService.increaseStock(5L);

        verify(ingredientRepository).updateStock(1L, 7.0);
        verify(ingredientRepository).updateStock(2L, 9.0);
    }
}