package fr.epf.restaurant.controller;

import fr.epf.restaurant.dto.CommandeClientDto;
import fr.epf.restaurant.dto.CommandeClientRequest;
import fr.epf.restaurant.entity.StatutCommande;
import fr.epf.restaurant.service.CommandeClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api/commandes/client")
public class CommandeClientController {

    private final CommandeClientService commandeClientService;

    public CommandeClientController(
            CommandeClientService commandeClientService) {
        this.commandeClientService = commandeClientService;
    }

    @GetMapping()
    public Collection<CommandeClientDto> all(
            @RequestParam(required = false) StatutCommande statut) {
        return statut == null
                ? commandeClientService.getAllCommande()
                : commandeClientService.commandeByStatut(
                statut);
    }

    @PostMapping()
    public ResponseEntity<CommandeClientDto> add(
            @RequestBody CommandeClientRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commandeClientService.addCommande(
                        request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommandeClientDto> commandeClient(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                commandeClientService.commande(id));
    }

    @PutMapping("/{id}/preparer")
    public ResponseEntity<CommandeClientDto> preparer(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                commandeClientService.changeStatut(id,
                        StatutCommande.EN_PREPARATION));
    }

    @PutMapping("/{id}/servir")
    public ResponseEntity<CommandeClientDto> servir(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                commandeClientService.changeStatut(id,
                        StatutCommande.SERVIE));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {
        commandeClientService.deleteCommande(id);
        return ResponseEntity.noContent().build();
    }
}
