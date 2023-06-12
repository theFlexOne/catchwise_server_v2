package com.flexone.catchwiseserver.dto;

import lombok.Data;

import java.util.List;

@Data
public class FishSpeciesJSON {
    private String name;
    private String family;
    private String species;
    private String description;
    private String identification;
    private List<String> commonNames;
}
