package com.flexone.catchwiseserver.service;

import com.flexone.catchwiseserver.domain.Lake;
import com.flexone.catchwiseserver.repository.LakeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LakeService {

  final LakeRepository lakeRepository;

  public void save(Lake lake) {
    lakeRepository.save(lake);
  }
  public void saveAll(Iterable<Lake> lakes) {
    lakeRepository.saveAll(lakes);
  }
  public List<Lake> findAllLakesInState(String stateName) {
    return lakeRepository.findAllLakesInState(stateName);
  }

  public List<Lake> findAllLakesWithinDistance(double lon, double lat, int distance) {
    return lakeRepository.findAllLakesWithinDistance(lon, lat, distance);
  }
}
