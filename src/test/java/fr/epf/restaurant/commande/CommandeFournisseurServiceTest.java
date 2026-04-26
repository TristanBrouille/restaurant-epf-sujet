package fr.epf.restaurant.commande;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import fr.epf.restaurant.dto.CommandeFournisseurDto;
import fr.epf.restaurant.dto.CommandeFournisseurRequest;
import fr.epf.restaurant.dto.LigneCommandeFournisseurUp;
import fr.epf.restaurant.entity.Commande;
import fr.epf.restaurant.entity.Fournisseur;
import fr.epf.restaurant.entity.StatutCommande;
import fr.epf.restaurant.repository.CommandeFournisseurRepository;
import fr.epf.restaurant.repository.FournisseurRepository;
import fr.epf.restaurant.service.CommandeFournisseurService;
import fr.epf.restaurant.service.LigneCommandeFournisseurService;
import fr.epf.restaurant.service.StockService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CommandeFournisseurServiceTest {

    @Mock
    private CommandeFournisseurRepository commandeFournisseurRepository;

    @Mock
    private LigneCommandeFournisseurService ligneCommandeFournisseurService;

    @Mock
    private StockService stockService;

    @Mock
    private FournisseurRepository fournisseurRepository;

    @InjectMocks
    private CommandeFournisseurService commandeFournisseurService;

    private final Fournisseur fournisseur = new Fournisseur(1L, "Metro", "contact", "metro@test.fr");
    private final Commande commandeEnAttente = new Commande(5L, 1L, LocalDate.now(), StatutCommande.EN_ATTENTE);

    @Test
    void shouldIncreaseStockWhenStatusChangesToReceived() {
        Commande commandeEnvoyee = new Commande(5L, 1L, LocalDate.now(), StatutCommande.ENVOYEE);
        when(commandeFournisseurRepository.ofId(5L)).thenReturn(Optional.of(commandeEnvoyee));
        when(fournisseurRepository.ofId(1L)).thenReturn(Optional.of(fournisseur));
        when(ligneCommandeFournisseurService.getDto(5L)).thenReturn(Collections.emptyList());

        commandeFournisseurService.changeStatut(5L, StatutCommande.RECUE);

        verify(stockService).increaseStock(5L);
        verify(commandeFournisseurRepository).updateStatut(5L, StatutCommande.RECUE);
    }

    @Test
    void shouldNotIncreaseStockWhenStatusChangesToSent() {
        when(commandeFournisseurRepository.ofId(5L)).thenReturn(Optional.of(commandeEnAttente));
        when(fournisseurRepository.ofId(1L)).thenReturn(Optional.of(fournisseur));
        when(ligneCommandeFournisseurService.getDto(5L)).thenReturn(Collections.emptyList());

        commandeFournisseurService.changeStatut(5L, StatutCommande.ENVOYEE);

        verify(stockService, never()).increaseStock(any());
        verify(commandeFournisseurRepository).updateStatut(5L, StatutCommande.ENVOYEE);
    }

    @Test
    void shouldCreateAndReturnDtoWhenAddingOrder() {
        LigneCommandeFournisseurUp ligne = new LigneCommandeFournisseurUp(1L, 10.0, 1.5);
        CommandeFournisseurRequest request = new CommandeFournisseurRequest(1L, List.of(ligne));
        when(ligneCommandeFournisseurService.checkIfIngredientExiste(ligne)).thenReturn(true);
        when(fournisseurRepository.ofId(1L)).thenReturn(Optional.of(fournisseur));
        when(commandeFournisseurRepository.getLastCommandeByFournisseur(1L))
                .thenReturn(Optional.of(commandeEnAttente));
        when(ligneCommandeFournisseurService.getDto(5L)).thenReturn(Collections.emptyList());

        CommandeFournisseurDto result = commandeFournisseurService.addCommande(request);

        assertNotNull(result);
        verify(commandeFournisseurRepository).add(request);
    }

    @Test
    void shouldThrowExceptionWhenIngredientDoesNotExistDuringAdd() {
        LigneCommandeFournisseurUp ligneBadIngredient = new LigneCommandeFournisseurUp(99L, 5.0, 2.0);
        CommandeFournisseurRequest request = new CommandeFournisseurRequest(1L, List.of(ligneBadIngredient));
        when(ligneCommandeFournisseurService.checkIfIngredientExiste(ligneBadIngredient)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> commandeFournisseurService.addCommande(request));
        verify(commandeFournisseurRepository, never()).add(any());
    }

    @Test
    void shouldThrowExceptionWhenSupplierDoesNotExistDuringAdd() {
        LigneCommandeFournisseurUp ligne = new LigneCommandeFournisseurUp(1L, 5.0, 2.0);
        CommandeFournisseurRequest request = new CommandeFournisseurRequest(99L, List.of(ligne));
        when(ligneCommandeFournisseurService.checkIfIngredientExiste(ligne)).thenReturn(true);
        when(fournisseurRepository.ofId(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> commandeFournisseurService.addCommande(request));
        verify(commandeFournisseurRepository, never()).add(any());
    }

    @Test
    void shouldDeleteOrderSuccessfully() {
        when(commandeFournisseurRepository.ofId(5L)).thenReturn(Optional.of(commandeEnAttente));

        commandeFournisseurService.deleteCommande(5L);

        verify(commandeFournisseurRepository).delete(5L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentOrder() {
        when(commandeFournisseurRepository.ofId(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> commandeFournisseurService.deleteCommande(99L));
        verify(commandeFournisseurRepository, never()).delete(any());
    }

    @Test
    void shouldReturnOnlyOrdersWithRequestedStatus() {
        when(commandeFournisseurRepository.ofStatut(StatutCommande.ENVOYEE))
                .thenReturn(List.of(commandeEnAttente));
        when(fournisseurRepository.ofId(1L)).thenReturn(Optional.of(fournisseur));
        when(ligneCommandeFournisseurService.getDto(5L)).thenReturn(Collections.emptyList());

        Collection<CommandeFournisseurDto> result = commandeFournisseurService.commandeByStatut(StatutCommande.ENVOYEE);

        assertEquals(1, result.size());
    }
}