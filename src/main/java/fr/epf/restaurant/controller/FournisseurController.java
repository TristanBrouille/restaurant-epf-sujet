package fr.epf.restaurant.controller;

import fr.epf.restaurant.DTO.IngredientPrix;
import fr.epf.restaurant.entity.Fournisseur;
import fr.epf.restaurant.service.FournisseurService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api/fournisseurs")
public class FournisseurController {

    private final FournisseurService fournisseurService;

    public FournisseurController(FournisseurService fournisseurService) {
        this.fournisseurService = fournisseurService;
    }

    @GetMapping()
    public Collection<Fournisseur> fournisseurs(){
        return fournisseurService.fournisseurs();
    }

    @GetMapping("/{id}/catalogue")
    public Collection<IngredientPrix> catalogue(@PathVariable Long id){
        return fournisseurService.catalogueFournisseur(id);
    }

}
