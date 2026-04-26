package fr.epf.restaurant.plat;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import fr.epf.restaurant.controller.PlatController;
import fr.epf.restaurant.dto.IngredientWithQuantity;
import fr.epf.restaurant.dto.RecetteDto;
import fr.epf.restaurant.entity.Ingredient;
import fr.epf.restaurant.entity.Plat;
import fr.epf.restaurant.service.PlatsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class PlatControllerTest {

    @Mock
    private PlatsService platsService;

    @InjectMocks
    private PlatController platController;

    @Test
    void shouldReturnAllDishes() {
        Plat p1 = new Plat(1L, "Quiche Lorraine", "Savoureuse", 12.0);
        when(platsService.getPlats()).thenReturn(List.of(p1));

        List<Plat> result = platController.plats();

        assertEquals(1, result.size());
        assertEquals("Quiche Lorraine", result.getFirst().getNom());
        verify(platsService, times(1)).getPlats();
    }

    @Test
    void shouldReturnHttp200WithRecipeWhenFound() {
        Ingredient oeuf = new Ingredient(1L, "Oeuf", "unité", 10.0, 5.0);
        RecetteDto recette = new RecetteDto(1L, "Quiche Lorraine", "Savoureuse", 12.0,
                List.of(new IngredientWithQuantity(oeuf, 3.0)));
        when(platsService.getRecette(1L)).thenReturn(recette);

        ResponseEntity<RecetteDto> result = platController.recette(1L);

        assertEquals(200, result.getStatusCode().value());
        assertNotNull(result.getBody());
        assertEquals("Quiche Lorraine", result.getBody().nom());
    }

    @Test
    void shouldReturnHttp404WhenRecipeDoesNotExist() {
        when(platsService.getRecette(99L)).thenReturn(null);

        ResponseEntity<RecetteDto> result = platController.recette(99L);

        assertEquals(404, result.getStatusCode().value());
    }
}