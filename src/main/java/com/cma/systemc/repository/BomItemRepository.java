package com.cma.systemc.repository;

import com.cma.systemc.entity.BomItem;
import com.cma.systemc.entity.Product;
import com.cma.systemc.entity.RawMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BomItemRepository extends JpaRepository<BomItem, Long> {
    List<BomItem> findByProductOrderByMaterialMaterialCodeAsc(Product product);
    void deleteByProduct(Product product);
    void deleteByMaterial(RawMaterial material);
}
