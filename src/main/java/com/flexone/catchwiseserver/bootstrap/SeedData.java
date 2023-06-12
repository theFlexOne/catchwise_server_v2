package com.flexone.catchwiseserver.bootstrap;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.flexone.catchwiseserver.domain.County;
import com.flexone.catchwiseserver.domain.FishSpecies;
import com.flexone.catchwiseserver.domain.Lake;
import com.flexone.catchwiseserver.domain.State;
import com.flexone.catchwiseserver.dto.FishSpeciesJSON;
import com.flexone.catchwiseserver.dto.LakeJSON;
import com.flexone.catchwiseserver.service.FishSpeciesService;
import com.flexone.catchwiseserver.service.LakeService;
import com.flexone.catchwiseserver.service.StateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.wololo.geojson.Feature;
import org.wololo.geojson.FeatureCollection;
import org.wololo.jts2geojson.GeoJSONReader;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class SeedData implements CommandLineRunner {

    private final StateService stateService;
    private final LakeService lakeService;
    private final FishSpeciesService fishSpeciesService;
    private final GeoJSONReader reader = new GeoJSONReader();

    @Override
    public void run(String... args) throws IOException {
        log.info("Loading seed data...");
        try {
            seedStatesAndCounties(
                    "src/main/resources/data/US_States.geojson",
                    "src/main/resources/data/US_Counties.geojson"
            );
            seedFishSpecies("src/main/resources/data/Fish_Species_Data.json");
            seedLakes("src/main/resources/data/MN_Lakes_Local_Data.json");
        } catch (IOException e) {
            e.printStackTrace();
            log.info("**SEEDING FAILED**");
            return;
        }
        log.info("Finished loading seed data.");
    }

    private void seedStatesAndCounties(String statesPath, String countiesPath) throws IOException {
        log.info("Loading states data...");
        FeatureCollection statesFeatureCollection = importFeatureCollection(statesPath);
        log.info("Loading counties data...");
        FeatureCollection countyFeatureCollection = importFeatureCollection(countiesPath);

        Feature[] features = statesFeatureCollection.getFeatures();
        List<State> statesList = new ArrayList<>();
        for (Feature feature : features) {
            Map<String, Object> properties = feature.getProperties();
            MultiPolygon stateGeometry = (MultiPolygon) reader.read(feature.getGeometry());
            List<County> countyList = new ArrayList<>();
            for (Feature countyFeature : countyFeatureCollection.getFeatures()) {

                Map<String, Object> countyProperties = countyFeature.getProperties();
                MultiPolygon countyGeometry = (MultiPolygon) reader.read(countyFeature.getGeometry());

                if (feature.getProperties().get("STATEFP").equals(countyProperties.get("STATEFP"))) {
                    County newCounty = new County()
                            .setName((String) countyProperties.get("NAME"))
                            .setFipsCode((String) countyProperties.get("COUNTYFP"))
                            .setAnsiCode((String) countyProperties.get("COUNTYNS"))
                            .setGeometry(countyGeometry);

                    countyList.add(newCounty);
                }
            }
            State newState = new State()
                    .setName((String) properties.get("NAME"))
                    .setAbbreviation((String) properties.get("STUSPS"))
                    .setFipsCode((String) properties.get("STATEFP"))
                    .setAnsiCode((String) properties.get("STATENS"))
                    .setGeometry(stateGeometry)
                    .addCounties(countyList);

            statesList.add(newState);
        }
        log.info("Saving States data...");
        stateService.saveAll(statesList);
    }

    private void seedLakes(String pathName) throws IOException {
        CollectionType collectionType = new ObjectMapper()
                .getTypeFactory().constructCollectionType(List.class, LakeJSON.class);
        log.info("Loading Lakes data...");
        List<LakeJSON> lakeJSONList = new ObjectMapper().readValue(Paths.get(pathName).toFile(), collectionType);
        List<Lake> lakeList = mapLakeJSONListToLakeList(lakeJSONList);

        log.info("Saving Lakes data...");
        lakeService.saveAll(lakeList);

    }

    private void seedFishSpecies(String pathName) throws IOException {
        CollectionType collectionType = new ObjectMapper()
                .getTypeFactory().constructCollectionType(List.class, FishSpeciesJSON.class);
        List<FishSpeciesJSON> fishSpeciesJSONList = new ObjectMapper().readValue(Paths.get(pathName).toFile(), collectionType);
        List<FishSpecies> fishSpeciesList = fishSpeciesJSONList.stream().map(json -> {
            String[] scientificName = json.getSpecies().split(" ");
            return new FishSpecies()
                    .setName(json.getName())
                    .setGenus(scientificName[0])
                    .setSpecies(scientificName[1])
                    .setDescription(json.getDescription())
                    .setIdentification(json.getIdentification())
                    .setOtherNames(json.getCommonNames());
        }).toList();
        fishSpeciesService.saveAll(fishSpeciesList);
    }

    private List<Lake> mapLakeJSONListToLakeList(List<LakeJSON> lakeJSONList) throws IOException {
        List<Lake> lakeList = new ArrayList<>();
        for (LakeJSON lakeJSON : lakeJSONList) {
            if (lakeJSON == null) {
                continue;
            }

            Coordinate coordinate = new Coordinate(lakeJSON.getCoordinates().getLng(), lakeJSON.getCoordinates().getLat());
            Point point = new Point(coordinate, null, 4326);

            List<FishSpecies> lakeFishSpeciesList = new ArrayList<>();
            for (LakeJSON.FishSpecies fishSpeciesJSON : lakeJSON.getFishSpecies()) {
                lakeFishSpeciesList.add(fishSpeciesService.findOrCreateByScientificName(fishSpeciesJSON.getSpecies()));
            }


            lakeList.add(new Lake()
                    .setName(lakeJSON.getName())
                    .setCountyFips(lakeJSON.getCountyFips())
                    .setLocalId(lakeJSON.getLocalId())
                    .setNearestTown(lakeJSON.getNearestTown())
                    .setGeometry(point)
                    .setFishSpecies(lakeFishSpeciesList));
        }
        return lakeList;
    }

    private FeatureCollection importFeatureCollection(String pathName) throws IOException {
        return new ObjectMapper().readValue(Paths.get(pathName).toFile(), FeatureCollection.class);
    }


}
