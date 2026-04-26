package fr.epf.restaurant.service;

import fr.epf.restaurant.DTO.IngredientWithQuantity;
import fr.epf.restaurant.DTO.RecetteDto;
import fr.epf.restaurant.entity.Plat;
import fr.epf.restaurant.repository.IngredientRepository;
import fr.epf.restaurant.repository.PlatIngredientRepository;
import fr.epf.restaurant.repository.PlatRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public class PlatsService {

    private final PlatRepository platRepository;
    private final PlatIngredientRepository platIngredientRepository;
    private final IngredientRepository ingredientRepository;

    public PlatsService(PlatRepository platRepository, PlatIngredientRepository platIngredientRepository, IngredientRepository ingredientRepository) {
        this.platRepository = platRepository;
        this.platIngredientRepository = platIngredientRepository;
        this.ingredientRepository = ingredientRepository;
    }

    public List<Plat> getPlats(){
        return platRepository.getAll();
    }

    public RecetteDto getRecette(Long id){
        Plat plat = platRepository.ofId(id).orElseThrow(() -> new RuntimeException("Plat introuvable : " + id));
        Map<Long,Double> ingredientIdQuantity = platIngredientRepository.ofPlatId(id);
        Collection<IngredientWithQuantity> ingredients = ingredientIdQuantity.entrySet()
                .stream()
                .map(entry -> new IngredientWithQuantity(
                        ingredientRepository.ofId(entry.getKey()).orElseThrow(() -> new RuntimeException("Aucun ingredient trouvé pour cet id")),
                        entry.getValue()
                ))
                .toList();

        return new RecetteDto(plat.getId(), plat.getNom(),plat.getDescription(),plat.getPrix(), ingredients);
    }
}
