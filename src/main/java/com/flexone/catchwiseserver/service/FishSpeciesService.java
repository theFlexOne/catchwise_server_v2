package com.flexone.catchwiseserver.service;

import com.flexone.catchwiseserver.domain.FishSpecies;
import com.flexone.catchwiseserver.repository.FishSpeciesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FishSpeciesService {

    final FishSpeciesRepository fishSpeciesRepository;

    public FishSpecies findByScientificName(String scientificName) {
        String[] scientificNameParts = scientificName.split(" ");
        if (scientificNameParts.length != 2) {
            return null;
        }
        String genus = scientificNameParts[0];
        String species = scientificNameParts[1];
        return fishSpeciesRepository.findBySpeciesAndGenusAllIgnoreCase(species, genus);
    }

    public FishSpecies findOrCreateByScientificName(String scientificName) {
        FishSpecies fishSpecies = findByScientificName(scientificName);
        if (fishSpecies == null) {
            String[] scientificNameParts = scientificName.split(" ");
            if (scientificNameParts.length != 2) {
                return null;
            }
            String genus = scientificNameParts[0];
            String species = scientificNameParts[1];
            fishSpecies = new FishSpecies();
            fishSpecies.setGenus(genus);
            fishSpecies.setSpecies(species);
            save(fishSpecies);
        }
        return fishSpecies;
    }

    public void save(FishSpecies fishSpecies) {
        fishSpeciesRepository.save(fishSpecies);
    }

    public void saveAll(List<FishSpecies> fishSpeciesList) {
        fishSpeciesRepository.saveAll(fishSpeciesList);
    }


}
