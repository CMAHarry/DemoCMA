package com.cma.systemc.repository;

import com.cma.systemc.entity.RawMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RawMaterialRepository extends JpaRepository<RawMaterial, Long> {
    Optional<RawMaterial> findByMaterialCodeIgnoreCase(String materialCode);
    boolean existsByMaterialCodeIgnoreCase(String materialCode);
    List<RawMaterial> findByActiveTrueOrderByMaterialCodeAsc();
}
