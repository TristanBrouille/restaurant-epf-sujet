package fr.epf.restaurant.client;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import fr.epf.restaurant.controller.ClientController;
import fr.epf.restaurant.entity.Client;
import fr.epf.restaurant.service.ClientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class ClientControllerTest {

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController clientController;

    @Test
    void shouldReturnClientList() {
        Client mockClient = new Client(1L, "Nom", "Prenom", "email@test.com", "0102030405");
        when(clientService.getClients()).thenReturn(
                List.of(mockClient));
        List<Client> result = clientController.getClients();
        assertEquals(1, result.size());
        assertEquals("Nom", result.getFirst().getNom());
        verify(clientService, times(1)).getClients();
    }
}