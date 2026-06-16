package com.cma.systemc.api;

import com.cma.systemc.dto.ApiResponse;
import com.cma.systemc.dto.RawMaterialRequest;
import com.cma.systemc.entity.RawMaterial;
import com.cma.systemc.service.RawMaterialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materials")
@Tag(name = "Raw Materials API", description = "CRUD API for raw materials/components")
public class RawMaterialApiController {
    private final RawMaterialService service;

    public RawMaterialApiController(RawMaterialService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "List raw materials")
    public ApiResponse<List<RawMaterial>> list() {
        return ApiResponse.ok("Materials retrieved", service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get raw material by ID")
    public ApiResponse<RawMaterial> get(@PathVariable Long id) {
        return ApiResponse.ok("Material retrieved", service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Create raw material")
    public ApiResponse<RawMaterial> create(@Valid @RequestBody RawMaterialRequest request) {
        return ApiResponse.ok("Material created", service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update raw material")
    public ApiResponse<RawMaterial> update(@PathVariable Long id, @Valid @RequestBody RawMaterialRequest request) {
        return ApiResponse.ok("Material updated", service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete raw material")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ApiResponse.ok("Material deleted", null);
    }
}
