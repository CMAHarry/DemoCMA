package com.cma.systemc.config;

import com.cma.systemc.dto.BomItemRequest;
import com.cma.systemc.entity.Product;
import com.cma.systemc.entity.RawMaterial;
import com.cma.systemc.service.BomService;
import com.cma.systemc.service.ProductService;
import com.cma.systemc.service.RawMaterialService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {
    private static final Logger logger = LogManager.getLogger(DataInitializer.class);

    private final RawMaterialService rawMaterialService;
    private final ProductService productService;
    private final BomService bomService;

    public DataInitializer(RawMaterialService rawMaterialService,
                           ProductService productService,
                           BomService bomService) {
        this.rawMaterialService = rawMaterialService;
        this.productService = productService;
        this.bomService = bomService;
    }

    @Override
    public void run(String... args) {
        if (!rawMaterialService.findAll().isEmpty() || !productService.findAll().isEmpty()) {
            return;
        }

        logger.info("Initializing demo master data");

        RawMaterial aluminium = new RawMaterial();
        aluminium.setMaterialCode("MAT-001");
        aluminium.setMaterialName("Aluminium Casing");
        aluminium.setUnitOfMeasure("PCS");
        rawMaterialService.create(aluminium);

        RawMaterial board = new RawMaterial();
        board.setMaterialCode("MAT-002");
        board.setMaterialName("Circuit Board");
        board.setUnitOfMeasure("PCS");
        rawMaterialService.create(board);

        RawMaterial cable = new RawMaterial();
        cable.setMaterialCode("MAT-003");
        cable.setMaterialName("Power Cable");
        cable.setUnitOfMeasure("PCS");
        rawMaterialService.create(cable);

        Product product = new Product();
        product.setProductCode("PRD-001");
        product.setProductName("Demo Finished Product");
        product.setDescription("Finished product used in the ISM 2 manufacturing integration demo.");
        productService.create(product);

        addBom("PRD-001", "MAT-001", "2");
        addBom("PRD-001", "MAT-002", "1");
        addBom("PRD-001", "MAT-003", "1");
    }

    private void addBom(String productCode, String materialCode, String qty) {
        BomItemRequest request = new BomItemRequest();
        request.setProductCode(productCode);
        request.setMaterialCode(materialCode);
        request.setQuantityPerUnit(new BigDecimal(qty));
        bomService.addBomItem(request);
    }
}
