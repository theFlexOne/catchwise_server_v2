package com.flexone.catchwiseserver.service;

import com.flexone.catchwiseserver.domain.County;
import com.flexone.catchwiseserver.repository.CountyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CountyService {

  final CountyRepository countyRepository;


  public Optional<County> findByFipsCode(String fipsCode) {
    return countyRepository.findByFipsCode(fipsCode);
  }


  public void save(County county) {
    countyRepository.save(county);
  }
  public void saveAll(List<County> counties) {
    countyRepository.saveAll(counties);
  }

}
