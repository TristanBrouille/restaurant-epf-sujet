package fr.epf.restaurant.service;

import fr.epf.restaurant.DTO.LigneCommandeDto;
import fr.epf.restaurant.DTO.LigneCommandeUp;
import fr.epf.restaurant.entity.LigneCommande;
import fr.epf.restaurant.repository.LigneCommandeRepository;
import fr.epf.restaurant.repository.PlatRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class LigneCommandeService {

    private final LigneCommandeRepository ligneCommandeRepository;
    private final PlatRepository platRepository;
    private final StockService stockService;

    public LigneCommandeService(LigneCommandeRepository ligneCommandeRepository, PlatRepository platRepository, StockService stockService) {
        this.ligneCommandeRepository = ligneCommandeRepository;
        this.platRepository = platRepository;
        this.stockService = stockService;
    }

    public boolean checkIfPlatExiste(LigneCommandeUp ligne) {
        return platRepository.ofId(ligne.platId()).isPresent();
    }

    public Collection<LigneCommandeDto> getDto(Long id) {
        Collection<LigneCommande> lignes = ligneCommandeRepository.ofCommandeId(id);
        return lignes.stream()
                .map(ligne -> new LigneCommandeDto(ligne.getId(), platRepository.ofId(ligne.getPlatId())
                        .orElseThrow(() -> new RuntimeException("Aucun plat trouvé pour cet id")), ligne.getQuantite()))
                .toList();
    }

    public Boolean checkIfStockCanBeRaised(Long commandeId) {
        Collection<LigneCommande> lignes = ligneCommandeRepository.ofCommandeId(commandeId);

        return lignes.stream()
                .allMatch(ligne -> stockService.raiseStockOfPlat(ligne.getPlatId()));
    }
}
