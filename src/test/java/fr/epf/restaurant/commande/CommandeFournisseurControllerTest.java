package fr.epf.restaurant.commande;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import fr.epf.restaurant.controller.CommandeFournisseurController;
import fr.epf.restaurant.dto.CommandeFournisseurDto;
import fr.epf.restaurant.dto.CommandeFournisseurRequest;
import fr.epf.restaurant.dto.LigneCommandeFournisseurUp;
import fr.epf.restaurant.entity.Fournisseur;
import fr.epf.restaurant.entity.StatutCommande;
import fr.epf.restaurant.service.CommandeFournisseurService;
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
class CommandeFournisseurControllerTest {

    @Mock
    private CommandeFournisseurService commandeFournisseurService;

    @InjectMocks
    private CommandeFournisseurController commandeFournisseurController;

    private final Fournisseur fournisseur = new Fournisseur(1L, "Metro", "contact", "metro@test.fr");
    private final CommandeFournisseurDto dto = new CommandeFournisseurDto(
            1L, fournisseur, LocalDate.now(), StatutCommande.EN_ATTENTE, Collections.emptyList());

    @Test
    void shouldReturnAllSupplierOrdersWhenNoStatusProvided() {
        when(commandeFournisseurService.getAllCommande()).thenReturn(List.of(dto));

        Collection<CommandeFournisseurDto> result = commandeFournisseurController.all(null);

        assertEquals(1, result.size());
        verify(commandeFournisseurService).getAllCommande();
        verify(commandeFournisseurService, never()).commandeByStatut(any());
    }

    @Test
    void shouldReturnFilteredOrdersWhenStatusIsProvided() {
        when(commandeFournisseurService.commandeByStatut(StatutCommande.ENVOYEE)).thenReturn(List.of(dto));

        Collection<CommandeFournisseurDto> result = commandeFournisseurController.all(StatutCommande.ENVOYEE);

        assertEquals(1, result.size());
        verify(commandeFournisseurService).commandeByStatut(StatutCommande.ENVOYEE);
        verify(commandeFournisseurService, never()).getAllCommande();
    }

    @Test
    void shouldReturnHttp201WithResponseBodyWhenAddingOrder() {
        LigneCommandeFournisseurUp ligne = new LigneCommandeFournisseurUp(1L, 10.0, 1.5);
        CommandeFournisseurRequest request = new CommandeFournisseurRequest(1L, List.of(ligne));
        when(commandeFournisseurService.addCommande(request)).thenReturn(dto);

        ResponseEntity<CommandeFournisseurDto> result = commandeFournisseurController.add(request);

        assertEquals(201, result.getStatusCode().value());
        assertNotNull(result.getBody());
    }

    @Test
    void shouldChangeStatusToSent() {
        CommandeFournisseurDto envoyee = new CommandeFournisseurDto(
                1L, fournisseur, LocalDate.now(), StatutCommande.ENVOYEE, Collections.emptyList());
        when(commandeFournisseurService.changeStatut(1L, StatutCommande.ENVOYEE)).thenReturn(envoyee);

        ResponseEntity<CommandeFournisseurDto> result = commandeFournisseurController.envoie(1L);

        assertEquals(200, result.getStatusCode().value());
        assertNotNull(result.getBody());
        assertEquals(StatutCommande.ENVOYEE, result.getBody().statut());
        verify(commandeFournisseurService).changeStatut(1L, StatutCommande.ENVOYEE);
    }

    @Test
    void shouldChangeStatusToReceived() {
        CommandeFournisseurDto recue = new CommandeFournisseurDto(
                1L, fournisseur, LocalDate.now(), StatutCommande.RECUE, Collections.emptyList());
        when(commandeFournisseurService.changeStatut(1L, StatutCommande.RECUE)).thenReturn(recue);

        ResponseEntity<CommandeFournisseurDto> result = commandeFournisseurController.recu(1L);

        assertEquals(200, result.getStatusCode().value());
        assertNotNull(result.getBody());
        assertEquals(StatutCommande.RECUE, result.getBody().statut());
        verify(commandeFournisseurService).changeStatut(1L, StatutCommande.RECUE);
    }

    @Test
    void shouldReturnHttp204WithNoBodyWhenDeletingOrder() {
        doNothing().when(commandeFournisseurService).deleteCommande(1L);

        ResponseEntity<Void> result = commandeFournisseurController.delete(1L);

        assertEquals(204, result.getStatusCode().value());
        assertNull(result.getBody());
        verify(commandeFournisseurService).deleteCommande(1L);
    }
}