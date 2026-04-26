package fr.epf.restaurant.service;

import fr.epf.restaurant.entity.Client;
import fr.epf.restaurant.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(
            ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> getClients() {
        return clientRepository.getAll();
    }

}
