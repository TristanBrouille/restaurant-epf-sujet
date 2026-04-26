package fr.epf.restaurant.fournisseur;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import fr.epf.restaurant.controller.FournisseurController;
import fr.epf.restaurant.dto.IngredientPrix;
import fr.epf.restaurant.entity.Fournisseur;
import fr.epf.restaurant.service.FournisseurService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class FournisseurControllerTest {

    @Mock
    private FournisseurService fournisseurService;

    @InjectMocks
    private FournisseurController fournisseurController;

    @Test
    void shouldReturnAllSuppliers() {
        Fournisseur f1 = new Fournisseur(1L, "Metro", "contact", "metro@test.fr");
        when(fournisseurService.fournisseurs()).thenReturn(List.of(f1));

        Collection<Fournisseur> result = fournisseurController.fournisseurs();

        assertEquals(1, result.size());
        assertEquals("Metro", result.iterator().next().getNom());
        verify(fournisseurService, times(1)).fournisseurs();
    }

    @Test
    void shouldReturnEmptyListWhenNoSuppliersExist() {
        when(fournisseurService.fournisseurs()).thenReturn(Collections.emptyList());

        Collection<Fournisseur> result = fournisseurController.fournisseurs();

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnSupplierCatalog() {
        IngredientPrix prix = new IngredientPrix(1L, "Oeuf", "unité", 0.30);
        when(fournisseurService.catalogueFournisseur(1L)).thenReturn(List.of(prix));

        Collection<IngredientPrix> result = fournisseurController.catalogue(1L);

        assertEquals(1, result.size());
        assertEquals("Oeuf", result.iterator().next().ingredientNom());
        verify(fournisseurService, times(1)).catalogueFournisseur(1L);
    }

    @Test
    void shouldReturnEmptyCatalogWhenNoIngredientsListed() {
        when(fournisseurService.catalogueFournisseur(1L)).thenReturn(Collections.emptyList());

        Collection<IngredientPrix> result = fournisseurController.catalogue(1L);

        assertTrue(result.isEmpty());
    }
}