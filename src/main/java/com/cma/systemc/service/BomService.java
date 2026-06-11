package com.cma.systemc.service;

import com.cma.systemc.dto.BomItemRequest;
import com.cma.systemc.entity.BomItem;
import com.cma.systemc.entity.Product;
import com.cma.systemc.entity.RawMaterial;
import com.cma.systemc.repository.BomItemRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BomService {
    private static final Logger logger = LogManager.getLogger(BomService.class);

    private final BomItemRepository bomItemRepository;
    private final ProductService productService;
    private final RawMaterialService rawMaterialService;

    public BomService(BomItemRepository bomItemRepository,
                      ProductService productService,
                      RawMaterialService rawMaterialService) {
        this.bomItemRepository = bomItemRepository;
        this.productService = productService;
        this.rawMaterialService = rawMaterialService;
    }

    public List<BomItem> findByProduct(Product product) {
        return bomItemRepository.findByProductOrderByMaterialMaterialCodeAsc(product);
    }

    public List<BomItem> findByProductCode(String productCode) {
        Product product = productService.findByCode(productCode);
        return findByProduct(product);
    }

    @Transactional
    public BomItem addBomItem(BomItemRequest request) {
        Product product = productService.findByCode(request.getProductCode());
        RawMaterial material = rawMaterialService.findByCode(request.getMaterialCode());

        BomItem item = new BomItem();
        item.setProduct(product);
        item.setMaterial(material);
        item.setQuantityPerUnit(request.getQuantityPerUnit());
        logger.info("Adding BOM item product [{}], material [{}]", product.getProductCode(), material.getMaterialCode());
        return bomItemRepository.save(item);
    }

    @Transactional
    public void deleteBomItem(Long id) {
        logger.info("Deleting BOM item [{}]", id);
        bomItemRepository.deleteById(id);
    }
}
