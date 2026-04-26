package fr.epf.restaurant.service;

import fr.epf.restaurant.DTO.CommandeFournisseurDto;
import fr.epf.restaurant.DTO.CommandeFournisseurRequest;
import fr.epf.restaurant.entity.Commande;
import fr.epf.restaurant.entity.Fournisseur;
import fr.epf.restaurant.entity.StatutCommande;
import fr.epf.restaurant.repository.CommandeFournisseurRepository;
import fr.epf.restaurant.repository.FournisseurRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class CommandeFournisseurService {

    private final CommandeFournisseurRepository commandeFournisseurRepository;
    private final LigneCommandeFournisseurService ligneCommandeFournisseurService;
    private final StockService stockService;
    private final FournisseurRepository fournisseurRepository;

    public CommandeFournisseurService(CommandeFournisseurRepository commandeFournisseurRepository, LigneCommandeFournisseurService ligneCommandeFournisseurService, StockService stockService, FournisseurRepository fournisseurRepository) {
        this.commandeFournisseurRepository = commandeFournisseurRepository;
        this.ligneCommandeFournisseurService = ligneCommandeFournisseurService;
        this.stockService = stockService;
        this.fournisseurRepository = fournisseurRepository;
    }

    public CommandeFournisseurDto changeStatut(Long commandeId, StatutCommande statut) {
        Commande commande = getCommande(commandeId);
        if (statut == StatutCommande.RECUE) {
            stockService.increaseStock(commandeId);
        }
        commande.setStatut(statut);
        commandeFournisseurRepository.updateStatut(commandeId, statut);
        return toDto(commande);
    }

    public Collection<CommandeFournisseurDto> getAllCommande() {
        Collection<Commande> commandes = commandeFournisseurRepository.ofAll();
        return commandes.stream().map(this::toDto).toList();
    }

    public Collection<CommandeFournisseurDto> commandeByStatut(StatutCommande statut) {
        Collection<Commande> commandes = commandeFournisseurRepository.ofStatut(statut);
        return commandes.stream().map(this::toDto).toList();
    }

    public CommandeFournisseurDto commande(Long commandeId) {
        Commande commande = getCommande(commandeId);
        return toDto(commande);
    }

    public CommandeFournisseurDto addCommande(CommandeFournisseurRequest request) {
        boolean allIngredientExist = request.lignes()
                .stream()
                .allMatch(ligneCommandeFournisseurService::checkIfIngredientExiste);

        if (!allIngredientExist) {
            throw new RuntimeException("Un ingredient n'existe pas");
        }
        Fournisseur fournisseur = fournisseurRepository.ofId(request.fournisseurId())
                .orElseThrow(() -> new RuntimeException("Pas de fournisseur trouvé"));
        commandeFournisseurRepository.add(request);
        Commande commande = commandeFournisseurRepository.getLastCommandeByFournisseur(request.fournisseurId())
                .orElseThrow(() -> new RuntimeException("Pas de commandes trouvé au nom de :" + fournisseur.getNom()));
        return toDto(commande);
    }

    private Commande getCommande(Long commandeId) {
        return commandeFournisseurRepository.ofId(commandeId)
                .orElseThrow(() -> new RuntimeException("Commande non trouvé"));
    }

    private CommandeFournisseurDto toDto(Commande commande) {
        Fournisseur fournisseur = fournisseurRepository.ofId(commande.getClientId())
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));
        return new CommandeFournisseurDto(commande.getId(), fournisseur, commande.getDateCommande(), commande.getStatut(), ligneCommandeFournisseurService.getDto(commande.getId()));
    }

    public void deleteCommande(Long id) {
        commandeFournisseurRepository.ofId(id)
                .orElseThrow(() -> new RuntimeException("Commande non trouvé"));
        commandeFournisseurRepository.delete(id);
    }
}
