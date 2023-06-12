package com.flexone.catchwiseserver.repository;

import com.flexone.catchwiseserver.domain.FishSpecies;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FishSpeciesRepository extends JpaRepository<FishSpecies, Long> {

    FishSpecies findBySpeciesAndGenusAllIgnoreCase(String species, String genus);

}
