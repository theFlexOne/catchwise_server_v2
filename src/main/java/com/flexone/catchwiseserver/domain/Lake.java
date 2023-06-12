package com.flexone.catchwiseserver.domain;


import com.bedatadriven.jackson.datatype.jts.serialization.GeometryDeserializer;
import com.bedatadriven.jackson.datatype.jts.serialization.GeometrySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Accessors(chain = true)
@Table(name = "lakes")
public class Lake {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "county_fips")
  private String countyFips;

  @Column(name = "local_id")
  private String localId;

  @Column(name = "nearest_town")
  private String nearestTown;

  @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
  @JoinTable(
          name = "lakes_fish_species",
          joinColumns = @JoinColumn(name = "lake_id"),
          inverseJoinColumns = @JoinColumn(name = "fish_species_id"))
  private List<FishSpecies> fishSpecies = new ArrayList<>();

  @Column(columnDefinition = "GEOMETRY(POINT, 4326)", name = "geom")
  @JsonSerialize(using = GeometrySerializer.class, as = Point.class)
  @JsonDeserialize(using = GeometryDeserializer.class)
  private Point geometry;


}
