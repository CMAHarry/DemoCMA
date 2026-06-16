package com.cma.systemc.api;

import com.cma.systemc.dto.ApiResponse;
import com.cma.systemc.dto.BomItemRequest;
import com.cma.systemc.dto.ProductRequest;
import com.cma.systemc.entity.BomItem;
import com.cma.systemc.entity.Product;
import com.cma.systemc.service.BomService;
import com.cma.systemc.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Products API", description = "CRUD API for finished products and product BOM items")
public class ProductApiController {
    private final ProductService productService;
    private final BomService bomService;

    public ProductApiController(ProductService productService, BomService bomService) {
        this.productService = productService;
        this.bomService = bomService;
    }

    @GetMapping
    @Operation(summary = "List products")
    public ApiResponse<List<Product>> list() {
        return ApiResponse.ok("Products retrieved", productService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID")
    public ApiResponse<Product> get(@PathVariable Long id) {
        return ApiResponse.ok("Product retrieved", productService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Create product")
    public ApiResponse<Product> create(@Valid @RequestBody ProductRequest request) {
        return ApiResponse.ok("Product created", productService.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update product")
    public ApiResponse<Product> update(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
        return ApiResponse.ok("Product updated", productService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ApiResponse.ok("Product deleted", null);
    }

    @GetMapping("/{productCode}/bom-items")
    @Operation(summary = "List BOM items by product code")
    public ApiResponse<List<BomItem>> bomItems(@PathVariable String productCode) {
        return ApiResponse.ok("BOM items retrieved", bomService.findByProductCode(productCode));
    }

    @PostMapping("/bom-items")
    @Operation(summary = "Add BOM item to product")
    public ApiResponse<BomItem> addBomItem(@Valid @RequestBody BomItemRequest request) {
        return ApiResponse.ok("BOM item created", bomService.addBomItem(request));
    }

    @DeleteMapping("/bom-items/{id}")
    @Operation(summary = "Delete BOM item")
    public ApiResponse<Void> deleteBomItem(@PathVariable Long id) {
        bomService.deleteBomItem(id);
        return ApiResponse.ok("BOM item deleted", null);
    }
}
