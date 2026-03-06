package fr.epf.restaurant.controller;

import fr.epf.restaurant.DTO.RecetteDto;
import fr.epf.restaurant.entity.Plat;
import fr.epf.restaurant.service.PlatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
    @RequestMapping("/api/plats")
    public class PlatController {

        private final PlatsService platsService;

        public PlatController(PlatsService platsService) {
            this.platsService = platsService;
        }

        @GetMapping()
        public List<Plat> plats(){
            return platsService.getPlats();
        }

        @GetMapping("/{id}")
        public ResponseEntity<RecetteDto> recette(@PathVariable Long id) {
            RecetteDto recette = platsService.getRecette(id);

            if (recette == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(recette);
        }

}
