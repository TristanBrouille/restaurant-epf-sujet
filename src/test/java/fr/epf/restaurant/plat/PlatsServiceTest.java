package fr.epf.restaurant.plat;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import fr.epf.restaurant.dto.RecetteDto;
import fr.epf.restaurant.entity.Ingredient;
import fr.epf.restaurant.entity.Plat;
import fr.epf.restaurant.repository.IngredientRepository;
import fr.epf.restaurant.repository.PlatIngredientRepository;
import fr.epf.restaurant.repository.PlatRepository;
import fr.epf.restaurant.service.PlatsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PlatsServiceTest {

    @Mock
    private PlatRepository platRepository;

    @Mock
    private PlatIngredientRepository platIngredientRepository;

    @Mock
    private IngredientRepository ingredientRepository;

    @InjectMocks
    private PlatsService platsService;

    @Test
    void shouldReturnAllDishes() {
        Plat p1 = new Plat(1L, "Quiche Lorraine", "Avec lardons", 12.0);
        Plat p2 = new Plat(2L, "Omelette", "Aux champignons", 9.0);
        when(platRepository.getAll()).thenReturn(List.of(p1, p2));

        List<Plat> result = platsService.getPlats();

        assertEquals(2, result.size());
        verify(platRepository, times(1)).getAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoDishesExist() {
        when(platRepository.getAll()).thenReturn(Collections.emptyList());

        List<Plat> result = platsService.getPlats();

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnRecipeWithItsIngredients() {
        Plat plat = new Plat(1L, "Quiche Lorraine", "Savoureuse", 12.0);
        Ingredient oeuf = new Ingredient(2L, "Oeuf", "unité", 10.0, 5.0);
        when(platRepository.ofId(1L)).thenReturn(Optional.of(plat));
        when(platIngredientRepository.ofPlatId(1L)).thenReturn(Map.of(2L, 3.0));
        when(ingredientRepository.ofId(2L)).thenReturn(Optional.of(oeuf));

        RecetteDto result = platsService.getRecette(1L);

        assertNotNull(result);
        assertEquals("Quiche Lorraine", result.nom());
        assertEquals(12.0, result.prix());
        assertEquals(1, result.ingredients().size());
    }

    @Test
    void shouldReturnCorrectIngredientQuantityInRecipe() {
        Plat plat = new Plat(1L, "Omelette", "Simple", 8.0);
        Ingredient oeuf = new Ingredient(1L, "Oeuf", "unité", 10.0, 5.0);
        when(platRepository.ofId(1L)).thenReturn(Optional.of(plat));
        when(platIngredientRepository.ofPlatId(1L)).thenReturn(Map.of(1L, 3.0));
        when(ingredientRepository.ofId(1L)).thenReturn(Optional.of(oeuf));

        RecetteDto result = platsService.getRecette(1L);

        double quantite = result.ingredients().iterator().next().quantity();
        assertEquals(3.0, quantite);
    }

    @Test
    void shouldThrowExceptionWhenDishDoesNotExist() {
        when(platRepository.ofId(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> platsService.getRecette(99L));
    }

    @Test
    void shouldThrowExceptionWhenRecipeIngredientDoesNotExist() {
        Plat plat = new Plat(1L, "Mystère", "?", 5.0);
        when(platRepository.ofId(1L)).thenReturn(Optional.of(plat));
        when(platIngredientRepository.ofPlatId(1L)).thenReturn(Map.of(999L, 1.0));
        when(ingredientRepository.ofId(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> platsService.getRecette(1L));
    }
}