package fr.epf.restaurant.commande;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import fr.epf.restaurant.dto.CommandeClientDto;
import fr.epf.restaurant.dto.CommandeClientRequest;
import fr.epf.restaurant.dto.LigneCommandeClientUp;
import fr.epf.restaurant.entity.Client;
import fr.epf.restaurant.entity.Commande;
import fr.epf.restaurant.entity.StatutCommande;
import fr.epf.restaurant.repository.ClientRepository;
import fr.epf.restaurant.repository.CommandeClientRepository;
import fr.epf.restaurant.service.CommandeClientService;
import fr.epf.restaurant.service.LigneCommandeClientService;
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
class CommandeClientServiceTest {

    @Mock
    private CommandeClientRepository commandeClientRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private LigneCommandeClientService ligneCommandeClientService;

    @InjectMocks
    private CommandeClientService commandeClientService;

    private final Client client = new Client(1L, "Dupont", "Alice", "alice@test.fr", "0600");
    private final Commande commandeEnAttente = new Commande(10L, 1L, LocalDate.now(), StatutCommande.EN_ATTENTE);

    @Test
    void shouldCreateAndReturnDtoWhenAddingOrder() {
        LigneCommandeClientUp ligne = new LigneCommandeClientUp(1L, 2.0);
        CommandeClientRequest request = new CommandeClientRequest(1L, List.of(ligne));

        when(ligneCommandeClientService.checkIfPlatExiste(ligne)).thenReturn(true);
        when(clientRepository.ofId(1L)).thenReturn(Optional.of(client));
        when(commandeClientRepository.getLastCommandeByClient(1L)).thenReturn(Optional.of(commandeEnAttente));
        when(ligneCommandeClientService.getDto(10L)).thenReturn(Collections.emptyList());
        CommandeClientDto result = commandeClientService.addCommande(request);

        assertNotNull(result);
        assertEquals(StatutCommande.EN_ATTENTE, result.statut());
        verify(commandeClientRepository).add(request);
    }

    @Test
    void shouldThrowExceptionWhenDishNotFoundDuringAdd() {
        LigneCommandeClientUp ligneBadPlat = new LigneCommandeClientUp(99L, 1.0);
        CommandeClientRequest request = new CommandeClientRequest(1L, List.of(ligneBadPlat));
        when(ligneCommandeClientService.checkIfPlatExiste(ligneBadPlat)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> commandeClientService.addCommande(request));
        verify(commandeClientRepository, never()).add(any());
    }

    @Test
    void shouldThrowExceptionWhenClientNotFoundDuringAdd() {
        LigneCommandeClientUp ligne = new LigneCommandeClientUp(1L, 1.0);
        CommandeClientRequest request = new CommandeClientRequest(99L, List.of(ligne));
        when(ligneCommandeClientService.checkIfPlatExiste(ligne)).thenReturn(true);
        when(clientRepository.ofId(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> commandeClientService.addCommande(request));
        verify(commandeClientRepository, never()).add(any());
    }

    @Test
    void shouldTriggerStockVerificationWhenChangingStatusToInPreparation() {
        when(commandeClientRepository.ofId(10L)).thenReturn(Optional.of(commandeEnAttente));
        when(ligneCommandeClientService.checkIfStockCanBeRaised(10L)).thenReturn(true);
        when(clientRepository.ofId(1L)).thenReturn(Optional.of(client));
        when(ligneCommandeClientService.getDto(10L)).thenReturn(Collections.emptyList());

        CommandeClientDto result = commandeClientService.changeStatut(10L, StatutCommande.EN_PREPARATION);

        verify(ligneCommandeClientService).checkIfStockCanBeRaised(10L);
        assertEquals(StatutCommande.EN_PREPARATION, result.statut());
        verify(commandeClientRepository).updateStatut(10L, StatutCommande.EN_PREPARATION);
    }

    @Test
    void shouldThrowExceptionWhenStockIsInsufficientForPreparation() {
        when(commandeClientRepository.ofId(10L)).thenReturn(Optional.of(commandeEnAttente));
        when(ligneCommandeClientService.checkIfStockCanBeRaised(10L)).thenReturn(false);

        assertThrows(RuntimeException.class,
                () -> commandeClientService.changeStatut(10L, StatutCommande.EN_PREPARATION));
        verify(commandeClientRepository, never()).updateStatut(any(), any());
    }

    @Test
    void shouldNotTriggerStockVerificationWhenChangingStatusToServed() {
        Commande enPreparation = new Commande(10L, 1L, LocalDate.now(), StatutCommande.EN_PREPARATION);
        when(commandeClientRepository.ofId(10L)).thenReturn(Optional.of(enPreparation));
        when(clientRepository.ofId(1L)).thenReturn(Optional.of(client));
        when(ligneCommandeClientService.getDto(10L)).thenReturn(Collections.emptyList());

        commandeClientService.changeStatut(10L, StatutCommande.SERVIE);

        verify(ligneCommandeClientService, never()).checkIfStockCanBeRaised(any());
        verify(commandeClientRepository).updateStatut(10L, StatutCommande.SERVIE);
    }

    @Test
    void shouldDeleteOrderSuccessfully() {
        when(commandeClientRepository.ofId(10L)).thenReturn(Optional.of(commandeEnAttente));

        commandeClientService.deleteCommande(10L);

        verify(commandeClientRepository).delete(10L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentOrder() {
        when(commandeClientRepository.ofId(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> commandeClientService.deleteCommande(99L));
        verify(commandeClientRepository, never()).delete(any());
    }


    @Test
    void shouldReturnOnlyOrdersWithRequestedStatus() {
        when(commandeClientRepository.ofStatut(StatutCommande.EN_ATTENTE))
                .thenReturn(List.of(commandeEnAttente));
        when(clientRepository.ofId(1L)).thenReturn(Optional.of(client));
        when(ligneCommandeClientService.getDto(10L)).thenReturn(Collections.emptyList());

        Collection<CommandeClientDto> result = commandeClientService.commandeByStatut(StatutCommande.EN_ATTENTE);

        assertEquals(1, result.size());
        assertEquals(StatutCommande.EN_ATTENTE, result.iterator().next().statut());
    }

    @Test
    void shouldReturnEmptyListWhenNoOrdersMatchStatus() {
        when(commandeClientRepository.ofStatut(StatutCommande.SERVIE)).thenReturn(List.of());

        Collection<CommandeClientDto> result = commandeClientService.commandeByStatut(StatutCommande.SERVIE);

        assertTrue(result.isEmpty());
    }
}