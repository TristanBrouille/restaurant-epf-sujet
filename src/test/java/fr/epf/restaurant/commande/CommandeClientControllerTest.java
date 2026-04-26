package fr.epf.restaurant.commande;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import fr.epf.restaurant.controller.CommandeClientController;
import fr.epf.restaurant.dto.CommandeClientDto;
import fr.epf.restaurant.dto.CommandeClientRequest;
import fr.epf.restaurant.dto.LigneCommandeClientUp;
import fr.epf.restaurant.entity.Client;
import fr.epf.restaurant.entity.StatutCommande;
import fr.epf.restaurant.service.CommandeClientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class CommandeClientControllerTest {

    @Mock
    private CommandeClientService commandeClientService;

    @InjectMocks
    private CommandeClientController commandeClientController;

    private final Client client = new Client(1L, "Dupont", "Alice", "alice@test.fr", "0600");
    private final CommandeClientDto dto = new CommandeClientDto(
            1L, client, LocalDate.now(), StatutCommande.EN_ATTENTE, Collections.emptyList());

    @Test
    void shouldReturnAllOrdersWhenNoStatusProvided() {
        when(commandeClientService.getAllCommande()).thenReturn(List.of(dto));
        Collection<CommandeClientDto> result = commandeClientController.all(null);

        assertEquals(1, result.size());
        verify(commandeClientService).getAllCommande();
        verify(commandeClientService, never()).commandeByStatut(any());
    }

    @Test
    void shouldReturnFilteredOrdersWhenStatusIsProvided() {
        when(commandeClientService.commandeByStatut(StatutCommande.EN_ATTENTE)).thenReturn(List.of(dto));
        Collection<CommandeClientDto> result = commandeClientController.all(StatutCommande.EN_ATTENTE);

        assertEquals(1, result.size());
        verify(commandeClientService).commandeByStatut(StatutCommande.EN_ATTENTE);
        verify(commandeClientService, never()).getAllCommande();
    }

    @Test
    void shouldReturnHttp201WhenAddingNewOrder() {
        LigneCommandeClientUp ligne = new LigneCommandeClientUp(1L, 2.0);
        CommandeClientRequest request = new CommandeClientRequest(1L, List.of(ligne));
        when(commandeClientService.addCommande(request)).thenReturn(dto);

        ResponseEntity<CommandeClientDto> result = commandeClientController.add(request);
        assertEquals(201, result.getStatusCode().value());
        assertNotNull(result.getBody());
        assertEquals(StatutCommande.EN_ATTENTE, result.getBody().statut());
    }

    @Test
    void shouldChangeStatusToInPreparation() {
        CommandeClientDto enPreparation = new CommandeClientDto(
                1L, client, LocalDate.now(), StatutCommande.EN_PREPARATION, Collections.emptyList());
        when(commandeClientService.changeStatut(1L, StatutCommande.EN_PREPARATION)).thenReturn(enPreparation);

        ResponseEntity<CommandeClientDto> result = commandeClientController.preparer(1L);
        assertEquals(200, result.getStatusCode().value());
        assertNotNull(result.getBody());
        assertEquals(StatutCommande.EN_PREPARATION, result.getBody().statut());
        verify(commandeClientService).changeStatut(1L, StatutCommande.EN_PREPARATION);
    }

    @Test
    void shouldChangeStatusToServed() {
        CommandeClientDto servie = new CommandeClientDto(
                1L, client, LocalDate.now(), StatutCommande.SERVIE, Collections.emptyList());
        when(commandeClientService.changeStatut(1L, StatutCommande.SERVIE)).thenReturn(servie);
        ResponseEntity<CommandeClientDto> result = commandeClientController.servir(1L);
        assertEquals(200, result.getStatusCode().value());
        assertNotNull(result.getBody());
        assertEquals(StatutCommande.SERVIE, result.getBody().statut());
        verify(commandeClientService).changeStatut(1L, StatutCommande.SERVIE);
    }

    @Test
    void shouldReturnHttp204WithNoBodyWhenDeletingOrder() {
        doNothing().when(commandeClientService).deleteCommande(1L);
        ResponseEntity<Void> result = commandeClientController.delete(1L);
        assertEquals(204, result.getStatusCode().value());
        assertNull(result.getBody());
        verify(commandeClientService).deleteCommande(1L);
    }
}