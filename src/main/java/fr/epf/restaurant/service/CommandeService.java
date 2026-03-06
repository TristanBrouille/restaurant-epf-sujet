package fr.epf.restaurant.service;

import fr.epf.restaurant.DTO.CommandeDto;
import fr.epf.restaurant.DTO.CommandeRequest;
import fr.epf.restaurant.DTO.LigneCommandeDto;
import fr.epf.restaurant.entity.Client;
import fr.epf.restaurant.entity.Commande;
import fr.epf.restaurant.repository.ClientRepository;
import fr.epf.restaurant.repository.CommandeRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class CommandeService {

    private final CommandeRepository commandeRepository;
    private final ClientRepository clientRepository;
    private final LigneCommandeService ligneCommandeService;

    public CommandeService(CommandeRepository commandeRepository, ClientRepository clientRepository, LigneCommandeService ligneCommandeService) {
        this.commandeRepository = commandeRepository;
        this.clientRepository = clientRepository;
        this.ligneCommandeService = ligneCommandeService;
    }

    public Collection<CommandeDto> getAllCommande() {
        Collection<Commande> commandes = commandeRepository.ofAll();

        return commandes.stream()
                .map(cmd -> {
                    Client client = clientRepository.ofId(cmd.getClientId())
                            .orElseThrow(() -> new RuntimeException("Client non trouvé : " + cmd.getClientId()));

                    return new CommandeDto(
                            cmd.getId(),
                            client,
                            cmd.getDateCommande(),
                            cmd.getStatut(),
                            ligneCommandeService.getDto(cmd.getId())

                    );
                })
                .toList();
    }

    public CommandeDto addCommande(CommandeRequest request) {
        boolean allPlatExist = request.lignes().stream()
                .allMatch(ligneCommandeService::checkIfPlatExiste);

        if (!allPlatExist) {
            throw new RuntimeException("Un plat n'existe pas");
        }
        Client client = clientRepository.ofId(request.clientId()).orElseThrow(() -> new RuntimeException("Pas de cleint trouvvé"));
        commandeRepository.add(request);
        Commande commande = commandeRepository.getLastCommandeByClient(request.clientId()).orElseThrow(() -> new RuntimeException("Pas de commandes trouvé au nom de :" + client.getNom()));
        return new CommandeDto(commande.getId(), clientRepository.ofId(request.clientId()).orElseThrow(), commande.getDateCommande(), commande.getStatut(), request.lignes());
    }

    public Optional<CommandeDto> getCommande(Long clientId) {
        Optional<Client> clientOpt = clientRepository.ofId(clientId);
        if (clientOpt.isEmpty()) return Optional.empty();

        Optional<Commande> commandeOpt = commandeRepository.getLastCommandeByClient(clientId);
        if (commandeOpt.isEmpty()) return Optional.empty();

        Client client = clientOpt.get();
        Commande commande = commandeOpt.get();

        CommandeDto dto = new CommandeDto(
                commande.getId(),
                client,
                commande.getDateCommande(),
                commande.getStatut(),
                ligneCommandeService.getDto(commande.getId())
        );

        return Optional.of(dto);
    }
}