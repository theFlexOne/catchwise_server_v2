package com.flexone.catchwiseserver.repository;

import com.flexone.catchwiseserver.domain.County;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CountyRepository extends JpaRepository<County, Long> {

Optional<County> findByFipsCode(String fipsCode);


}
