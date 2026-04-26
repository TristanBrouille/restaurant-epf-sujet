package fr.epf.restaurant.fournisseur;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import fr.epf.restaurant.dto.IngredientPrix;
import fr.epf.restaurant.entity.Fournisseur;
import fr.epf.restaurant.entity.Ingredient;
import fr.epf.restaurant.repository.FournisseurIngredientRepository;
import fr.epf.restaurant.repository.FournisseurRepository;
import fr.epf.restaurant.service.FournisseurService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class FournisseurServiceTest {

    @Mock
    private FournisseurRepository fournisseurRepository;

    @Mock
    private FournisseurIngredientRepository fournisseurIngredientRepository;

    @InjectMocks
    private FournisseurService fournisseurService;

    @Test
    void shouldReturnAllSuppliers() {
        Fournisseur f1 = new Fournisseur(1L, "Metro", "contact1", "metro@test.fr");
        Fournisseur f2 = new Fournisseur(2L, "Transgourmet", "contact2", "trans@test.fr");
        when(fournisseurRepository.getAll()).thenReturn(List.of(f1, f2));

        Collection<Fournisseur> result = fournisseurService.fournisseurs();

        assertEquals(2, result.size());
        verify(fournisseurRepository, times(1)).getAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoSuppliersExist() {
        when(fournisseurRepository.getAll()).thenReturn(Collections.emptyList());

        Collection<Fournisseur> result = fournisseurService.fournisseurs();

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnAllSupplierIngredientsInCatalog() {
        Ingredient oeuf = new Ingredient(1L, "Oeuf", "unité");
        Ingredient lait = new Ingredient(2L, "Lait", "L");
        when(fournisseurIngredientRepository.ofFournisseurId(1L))
                .thenReturn(Map.of(0.25, oeuf, 1.20, lait));

        Collection<IngredientPrix> result = fournisseurService.catalogueFournisseur(1L);

        assertEquals(2, result.size());
    }

    @Test
    void shouldReturnCorrectPricePerIngredient() {
        Ingredient oeuf = new Ingredient(3L, "Oeuf", "unité");
        when(fournisseurIngredientRepository.ofFournisseurId(1L))
                .thenReturn(Map.of(0.30, oeuf));

        Collection<IngredientPrix> result = fournisseurService.catalogueFournisseur(1L);

        IngredientPrix prix = result.iterator().next();
        assertEquals(3L, prix.ingredientId());
        assertEquals("Oeuf", prix.ingredientNom());
        assertEquals("unité", prix.ingredientUnite());
        assertEquals(0.30, prix.prixUnitaire());
    }

    @Test
    void shouldReturnEmptyCatalogWhenNoIngredientsFound() {
        when(fournisseurIngredientRepository.ofFournisseurId(1L)).thenReturn(Map.of());

        Collection<IngredientPrix> result = fournisseurService.catalogueFournisseur(1L);

        assertTrue(result.isEmpty());
    }
}