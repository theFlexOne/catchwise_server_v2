package com.flexone.catchwiseserver.service;

import com.flexone.catchwiseserver.domain.State;
import com.flexone.catchwiseserver.repository.StateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StateService {

  final StateRepository stateRepository;

  public void save(State state) {
    stateRepository.save(state);
  }
  public void saveAll(List<State> states) {
    stateRepository.saveAll(states);
  }

}
