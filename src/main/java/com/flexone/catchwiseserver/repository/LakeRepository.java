package com.flexone.catchwiseserver.repository;

import com.flexone.catchwiseserver.domain.Lake;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LakeRepository  extends JpaRepository<Lake, Long> {

    @Query(value = "SELECT l.* FROM lakes AS l WHERE ST_Within(l.geom, (SELECT s.geom FROM states AS s WHERE s.name = 'Minnesota'));", nativeQuery = true)
    List<Lake> findAllLakesInState(@Param("stateName") String stateName);

    @Query(value = "SELECT * FROM lakes as l WHERE ST_DWithin(l.geom\\:\\:geography, ST_SetSRID(ST_MakePoint(:lng, :lat), 4326)\\:\\:geography, :distance);", nativeQuery = true)
    List<Lake> findAllLakesWithinDistance(@Param("lng") double lng, @Param("lat") double lat, @Param("distance") double distance);


}


/**
    1. Find all lakes within a given bounding box with a given buffer distance
 */