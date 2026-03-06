package fr.epf.restaurant.service;

import fr.epf.restaurant.DTO.LigneCommandeDto;
import fr.epf.restaurant.repository.LigneCommandeRepository;
import fr.epf.restaurant.repository.PlatRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class LigneCommandeService {

    private final LigneCommandeRepository ligneCommandeRepository;
    private final PlatRepository platRepository;

    public LigneCommandeService(LigneCommandeRepository ligneCommandeRepository, PlatRepository platRepository) {
        this.ligneCommandeRepository = ligneCommandeRepository;
        this.platRepository = platRepository;
    }

    public boolean checkIfPlatExiste(LigneCommandeDto ligne) {
        return platRepository.ofId(ligne.platId()).isPresent();
    }

    public Collection<LigneCommandeDto> getDto(Long id) {
        return ligneCommandeRepository.ofCommandeId(id);
    }
}
