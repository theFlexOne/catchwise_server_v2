package com.flexone.catchwiseserver.domain;

import javax.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Entity
@Data
@Accessors(chain = true)
@Table(name = "fish_species")
public class FishSpecies {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "family")
    private String family;

    @Column(name = "genus")
    private String genus;

    @Column(name = "species")
    private String species;

    @Column(name = "description", length = 10000)
    private String description;

    @Column(name = "identification", length = 10000)
    private String identification;

    @ElementCollection
    @CollectionTable(name = "other_fish_species_names", joinColumns = @JoinColumn(name = "fish_species_id"))
    private List<String> otherNames;

    public String getScientificName() {
        return this.genus + " " + this.species;
    }

}
