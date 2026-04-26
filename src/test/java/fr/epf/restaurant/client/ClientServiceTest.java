package fr.epf.restaurant.client;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import fr.epf.restaurant.entity.Client;
import fr.epf.restaurant.repository.ClientRepository;
import fr.epf.restaurant.service.ClientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    @Test
    void testGetClientsNominal() {
        // GIVEN
        when(clientRepository.getAll()).thenReturn(List.of(
                new Client(1L, "Doe", "John", "john@doe.fr", "0102")
        ));

        // WHEN
        List<Client> result = clientService.getClients();

        // THEN
        assertFalse(result.isEmpty(), "La liste ne devrait pas être vide");
        assertEquals(1, result.size());
        assertEquals("Doe", result.getFirst().getNom());
    }

    @Test
    void testGetClientsEmpty() {
        // GIVEN
        when(clientRepository.getAll()).thenReturn(Collections.emptyList());

        // WHEN
        List<Client> result = clientService.getClients();

        // THEN
        assertTrue(result.isEmpty(), "La liste devrait être vide");
        verify(clientRepository, times(1)).getAll();
    }

    @Test
    void testGetClientsError() {
        // GIVEN
        when(clientRepository.getAll()).thenThrow(new RuntimeException("Erreur DB"));

        // WHEN & THEN
        assertThrows(RuntimeException.class, () -> clientService.getClients(),
                "Le service devrait propager l'exception du repository");
    }
}
