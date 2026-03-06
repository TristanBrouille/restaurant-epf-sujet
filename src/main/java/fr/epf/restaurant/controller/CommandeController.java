package fr.epf.restaurant.controller;

import fr.epf.restaurant.DTO.CommandeDto;
import fr.epf.restaurant.DTO.CommandeRequest;
import fr.epf.restaurant.service.CommandeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/api/commandes")
public class CommandeController {

    private final CommandeService commandeService;

    public CommandeController(CommandeService commandeService) {
        this.commandeService = commandeService;
    }

    @GetMapping("/client")
    public Collection<CommandeDto> getCommande(){
        return commandeService.getAllCommande();
    }

    @PostMapping("/client")
    public ResponseEntity<CommandeDto> add(@RequestBody CommandeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commandeService.addCommande(request));
    }

    @GetMapping("/client/{id}")
    public ResponseEntity<CommandeDto> commandeClient(@PathVariable Long id) {
        Optional<CommandeDto> commande = commandeService.getCommande(id);

        if (commande.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }

        return ResponseEntity.ok(commande.get());
    }

    @PutMapping("/client/{id}/preparer")
    public ResponseEntity<CommandeDto> preparer(@PathVariable Long id){
        return null;
    }
}
