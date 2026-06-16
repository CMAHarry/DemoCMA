package com.cma.systemc.service;

import com.cma.systemc.dto.RawMaterialRequest;
import com.cma.systemc.entity.RawMaterial;
import com.cma.systemc.repository.RawMaterialRepository;
import com.cma.systemc.repository.BomItemRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RawMaterialService {
    private static final Logger logger = LogManager.getLogger(RawMaterialService.class);

    private final RawMaterialRepository repository;
    private final BomItemRepository bomItemRepository;

    public RawMaterialService(RawMaterialRepository repository, BomItemRepository bomItemRepository) {
        this.repository = repository;
        this.bomItemRepository = bomItemRepository;
    }

    public List<RawMaterial> findAll() {
        return repository.findAll();
    }

    public List<RawMaterial> findActive() {
        return repository.findByActiveTrueOrderByMaterialCodeAsc();
    }

    public RawMaterial findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Raw material not found: " + id));
    }

    public RawMaterial findByCode(String materialCode) {
        return repository.findByMaterialCodeIgnoreCase(materialCode)
                .orElseThrow(() -> new NotFoundException("Raw material not found: " + materialCode));
    }

    @Transactional
    public RawMaterial create(RawMaterial material) {
        String code = normalize(material.getMaterialCode());
        if (repository.existsByMaterialCodeIgnoreCase(code)) {
            throw new IllegalArgumentException("Material code already exists: " + code);
        }
        material.setMaterialCode(code);
        logger.info("Creating raw material [{}]", code);
        return repository.save(material);
    }

    @Transactional
    public RawMaterial create(RawMaterialRequest request) {
        RawMaterial material = new RawMaterial();
        material.setMaterialCode(request.getMaterialCode());
        material.setMaterialName(request.getMaterialName());
        material.setUnitOfMeasure(request.getUnitOfMeasure());
        material.setActive(request.getActive());
        return create(material);
    }

    @Transactional
    public RawMaterial update(Long id, RawMaterial material) {
        RawMaterial existing = findById(id);
        String newCode = normalize(material.getMaterialCode());
        repository.findByMaterialCodeIgnoreCase(newCode)
                .filter(found -> !found.getId().equals(id))
                .ifPresent(found -> { throw new IllegalArgumentException("Material code already exists: " + newCode); });

        existing.setMaterialCode(newCode);
        existing.setMaterialName(material.getMaterialName());
        existing.setUnitOfMeasure(material.getUnitOfMeasure());
        existing.setActive(material.getActive() != null ? material.getActive() : true);
        logger.info("Updating raw material [{}]", newCode);
        return repository.save(existing);
    }

    @Transactional
    public RawMaterial update(Long id, RawMaterialRequest request) {
        RawMaterial material = new RawMaterial();
        material.setMaterialCode(request.getMaterialCode());
        material.setMaterialName(request.getMaterialName());
        material.setUnitOfMeasure(request.getUnitOfMeasure());
        material.setActive(request.getActive());
        return update(id, material);
    }

    @Transactional
    public void delete(Long id) {
        RawMaterial material = findById(id);
        logger.info("Deleting raw material [{}] and related BOM item(s)", material.getMaterialCode());
        bomItemRepository.deleteByMaterial(material);
        repository.delete(material);
    }

    private String normalize(String code) {
        return code == null ? null : code.trim().toUpperCase();
    }
}
