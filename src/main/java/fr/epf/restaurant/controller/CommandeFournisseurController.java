package fr.epf.restaurant.controller;

import fr.epf.restaurant.DTO.CommandeFournisseurDto;
import fr.epf.restaurant.DTO.CommandeFournisseurRequest;
import fr.epf.restaurant.entity.StatutCommande;
import fr.epf.restaurant.service.CommandeFournisseurService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/commandes/fournisseur")
public class CommandeFournisseurController {

    private final CommandeFournisseurService commandeFournisseurService;

    public CommandeFournisseurController(CommandeFournisseurService commandeFournisseurService) {
        this.commandeFournisseurService = commandeFournisseurService;
    }


    @GetMapping()
    public Collection<CommandeFournisseurDto> all(@RequestParam(required = false) StatutCommande statut){
        return statut == null
                ? commandeFournisseurService.getAllCommande()
                : commandeFournisseurService.commandeByStatut(statut);
    }

    @GetMapping("/{id}")
    public CommandeFournisseurDto commandeFournisseur(@PathVariable Long id){
        return commandeFournisseurService.commande(id);
    }

    @PostMapping()
    public ResponseEntity<CommandeFournisseurDto> add(@RequestBody CommandeFournisseurRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(commandeFournisseurService.addCommande(request));
    }

    @PutMapping("/{id}/envoyer")
    public ResponseEntity<CommandeFournisseurDto> envoie(@PathVariable Long id){
        return ResponseEntity.ok(commandeFournisseurService.changeStatut(id, StatutCommande.ENVOYEE));
    }

    @PutMapping("/{id}/recevoir")
    public ResponseEntity<CommandeFournisseurDto> recu(@PathVariable Long id){
        return ResponseEntity.ok(commandeFournisseurService.changeStatut(id, StatutCommande.RECUE));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        commandeFournisseurService.deleteCommande(id);
        return ResponseEntity.noContent().build();
    }

}
