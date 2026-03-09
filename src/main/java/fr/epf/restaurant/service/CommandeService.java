package fr.epf.restaurant.service;

import fr.epf.restaurant.DTO.CommandeDto;
import fr.epf.restaurant.DTO.CommandeRequest;
import fr.epf.restaurant.entity.Client;
import fr.epf.restaurant.entity.Commande;
import fr.epf.restaurant.entity.StatutCommande;
import fr.epf.restaurant.repository.ClientRepository;
import fr.epf.restaurant.repository.CommandeRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class CommandeService {

    private final CommandeRepository commandeRepository;
    private final ClientRepository clientRepository;
    private final LigneCommandeService ligneCommandeService;
    private final StockService stockService;

    public CommandeService(CommandeRepository commandeRepository, ClientRepository clientRepository, LigneCommandeService ligneCommandeService, StockService stockService) {
        this.commandeRepository = commandeRepository;
        this.clientRepository = clientRepository;
        this.ligneCommandeService = ligneCommandeService;
        this.stockService = stockService;
    }

    public Collection<CommandeDto> getAllCommande() {
        Collection<Commande> commandes = commandeRepository.ofAll();

        return commandes.stream().map(this::toDto
        ).toList();
    }

    public CommandeDto addCommande(CommandeRequest request) {
        boolean allPlatExist = request.lignes()
                .stream()
                .allMatch(ligneCommandeService::checkIfPlatExiste);

        if (!allPlatExist) {
            throw new RuntimeException("Un plat n'existe pas");
        }
        Client client = clientRepository.ofId(request.clientId())
                .orElseThrow(() -> new RuntimeException("Pas de cleint trouvvé"));
        commandeRepository.add(request);
        Commande commande = commandeRepository.getLastCommandeByClient(request.clientId())
                .orElseThrow(() -> new RuntimeException("Pas de commandes trouvé au nom de :" + client.getNom()));
        return toDto(commande);
    }

    public CommandeDto commande(Long commandeId) {
        Commande commande = getCommande(commandeId);
        return toDto(commande);
    }

    public CommandeDto changeStatut(Long commandeId, StatutCommande statut) {
        Commande commande = getCommande(commandeId);
        if (commande.getStatut() == StatutCommande.EN_ATTENTE) {
            if (!ligneCommandeService.checkIfStockCanBeRaised(commandeId)) {
                throw new RuntimeException("Stock insuffisant pour valider la commande");
            }
        }
        commande.setStatut(statut);
        commandeRepository.updateStatut(commandeId, statut);
        return toDto(commande);
    }

    private CommandeDto toDto(Commande commande) {
        Client client = clientRepository.ofId(commande.getClientId())
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));
        return new CommandeDto(commande.getId(), client, commande.getDateCommande(), commande.getStatut(), ligneCommandeService.getDto(commande.getId()));
    }

    private Commande getCommande(Long commandeId) {
        return commandeRepository.ofId(commandeId)
                .orElseThrow(() -> new RuntimeException("Commande non trouvé"));
    }
}