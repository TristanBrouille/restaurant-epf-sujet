package fr.epf.restaurant.service;

import fr.epf.restaurant.dto.LigneCommandeClientDto;
import fr.epf.restaurant.dto.LigneCommandeClientUp;
import fr.epf.restaurant.entity.LigneCommandeClient;
import fr.epf.restaurant.repository.LigneCommandeRepository;
import fr.epf.restaurant.repository.PlatRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class LigneCommandeClientService {

    private final LigneCommandeRepository ligneCommandeRepository;
    private final PlatRepository platRepository;
    private final StockService stockService;

    public LigneCommandeClientService(
            LigneCommandeRepository ligneCommandeRepository,
            PlatRepository platRepository,
            StockService stockService) {
        this.ligneCommandeRepository = ligneCommandeRepository;
        this.platRepository = platRepository;
        this.stockService = stockService;
    }

    public boolean checkIfPlatExiste(
            LigneCommandeClientUp ligne) {
        return platRepository.ofId(ligne.platId())
                .isPresent();
    }

    public Collection<LigneCommandeClientDto> getDto(
            Long id) {
        Collection<LigneCommandeClient> lignes = ligneCommandeRepository.ofCommandeClientId(
                id);
        return lignes.stream()
                .map(ligne -> new LigneCommandeClientDto(
                        ligne.getId(), platRepository.ofId(
                                ligne.getPlatId())
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Aucun plat trouvé pour cet id")),
                        ligne.getQuantite()))
                .toList();
    }

    public Boolean checkIfStockCanBeRaised(
            Long commandeId) {
        Collection<LigneCommandeClient> lignes = ligneCommandeRepository.ofCommandeClientId(
                commandeId);

        return lignes.stream()
                .allMatch(
                        ligne -> stockService.lowerStockOfPlat(
                                ligne.getPlatId()));
    }
}
