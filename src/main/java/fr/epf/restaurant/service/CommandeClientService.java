package fr.epf.restaurant.service;

import fr.epf.restaurant.dto.CommandeClientDto;
import fr.epf.restaurant.dto.CommandeClientRequest;
import fr.epf.restaurant.entity.Client;
import fr.epf.restaurant.entity.Commande;
import fr.epf.restaurant.entity.StatutCommande;
import fr.epf.restaurant.repository.ClientRepository;
import fr.epf.restaurant.repository.CommandeClientRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class CommandeClientService {

    private final CommandeClientRepository commandeClientRepository;
    private final ClientRepository clientRepository;
    private final LigneCommandeClientService ligneCommandeClientService;

    public CommandeClientService(
            CommandeClientRepository commandeRepository,
            ClientRepository clientRepository,
            LigneCommandeClientService ligneCommandeClientService) {
        this.commandeClientRepository = commandeRepository;
        this.clientRepository = clientRepository;
        this.ligneCommandeClientService = ligneCommandeClientService;
    }

    public Collection<CommandeClientDto> getAllCommande() {
        Collection<Commande> commandes = commandeClientRepository.ofAll();

        return commandes.stream().map(this::toDto
        ).toList();
    }

    public CommandeClientDto addCommande(
            CommandeClientRequest request) {
        boolean allPlatExist = request.lignes()
                .stream()
                .allMatch(
                        ligneCommandeClientService::checkIfPlatExiste);

        if (!allPlatExist) {
            throw new RuntimeException(
                    "Un plat n'existe pas");
        }
        Client client = clientRepository.ofId(
                        request.clientId())
                .orElseThrow(() -> new RuntimeException(
                        "Pas de cleint trouvvé"));
        commandeClientRepository.add(request);
        Commande commande = commandeClientRepository.getLastCommandeByClient(
                        request.clientId())
                .orElseThrow(() -> new RuntimeException(
                        "Pas de commandes trouvé au nom de :" + client.getNom()));
        return toDto(commande);
    }

    public CommandeClientDto commande(Long commandeId) {
        Commande commande = getCommande(commandeId);
        return toDto(commande);
    }

    public CommandeClientDto changeStatut(Long commandeId,
                                          StatutCommande statut) {
        Commande commande = getCommande(commandeId);
        if (commande.getStatut() == StatutCommande.EN_ATTENTE) {
            if (!ligneCommandeClientService.checkIfStockCanBeRaised(
                    commandeId)) {
                throw new RuntimeException(
                        "Stock insuffisant pour valider la commande");
            }
        }
        commande.setStatut(statut);
        commandeClientRepository.updateStatut(commandeId,
                statut);
        return toDto(commande);
    }

    public Collection<CommandeClientDto> commandeByStatut(
            StatutCommande statut) {
        Collection<Commande> commandes = commandeClientRepository.ofStatut(
                statut);
        return commandes.stream().map(this::toDto).toList();
    }

    public void deleteCommande(Long id) {
        commandeClientRepository.ofId(id)
                .orElseThrow(() -> new RuntimeException(
                        "Commande non trouvé"));
        commandeClientRepository.delete(id);
    }

    private CommandeClientDto toDto(Commande commande) {
        Client client = clientRepository.ofId(
                        commande.getClientId())
                .orElseThrow(() -> new RuntimeException(
                        "Client non trouvé"));
        return new CommandeClientDto(commande.getId(),
                client, commande.getDateCommande(),
                commande.getStatut(),
                ligneCommandeClientService.getDto(
                        commande.getId()));
    }

    private Commande getCommande(Long commandeId) {
        return commandeClientRepository.ofId(commandeId)
                .orElseThrow(() -> new RuntimeException(
                        "Commande non trouvé"));
    }


}