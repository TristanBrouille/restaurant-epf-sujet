package fr.epf.restaurant.controller;

import fr.epf.restaurant.DTO.CommandeDto;
import fr.epf.restaurant.DTO.CommandeRequest;
import fr.epf.restaurant.entity.StatutCommande;
import fr.epf.restaurant.service.CommandeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/commandes/client")
public class CommandeClientController {

    private final CommandeService commandeService;

    public CommandeClientController(CommandeService commandeService) {
        this.commandeService = commandeService;
    }

    @GetMapping()
    public Collection<CommandeDto> getCommande(){
        return commandeService.getAllCommande();
    }

    @PostMapping()
    public ResponseEntity<CommandeDto> add(@RequestBody CommandeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commandeService.addCommande(request));
    }

    @GetMapping("/{statut}")
    public Collection<CommandeDto> statut(@PathVariable StatutCommande statut){
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommandeDto> commandeClient(@PathVariable Long id) {
        return ResponseEntity.ok(commandeService.commande(id));
    }

    @PutMapping("/{id}/preparer")
    public ResponseEntity<CommandeDto> preparer(@PathVariable Long id){
        return ResponseEntity.ok(commandeService.changeStatut(id, StatutCommande.EN_PREPARATION));
    }

    @PutMapping("/{id}/servir")
    public ResponseEntity<CommandeDto> servir(@PathVariable Long id){
        return ResponseEntity.ok(commandeService.changeStatut(id, StatutCommande.SERVIE));
    }
}
