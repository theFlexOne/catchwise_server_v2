package com.flexone.catchwiseserver.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;


@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@RequiredArgsConstructor
public class LakeJSON {

  String name;
  String countyFips;
  String localId;
  String nearestTown;
  LatLng coordinates;

  @JsonProperty("fish")
  List<FishSpecies> fishSpecies;

  List<Component> components;

  @Data
  public static class LatLng {
    @JsonProperty("latitude")
    double lat;
    @JsonProperty("longitude")
    double lng;
  }

  @Data
  public static class FishSpecies {
    String name;
    String species;
  }

  @Data
  private static class Component {
    String localId;
    String name;
    LatLng coordinates;

    @JsonProperty("fish")
    List<FishSpecies> fishSpecies;
  }
}
