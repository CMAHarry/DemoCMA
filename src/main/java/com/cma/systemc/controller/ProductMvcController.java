package com.cma.systemc.controller;

import com.cma.systemc.dto.BomItemRequest;
import com.cma.systemc.entity.Product;
import com.cma.systemc.service.BomService;
import com.cma.systemc.service.ProductService;
import com.cma.systemc.service.RawMaterialService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
@RequestMapping("/products")
public class ProductMvcController {
    private final ProductService productService;
    private final RawMaterialService rawMaterialService;
    private final BomService bomService;

    public ProductMvcController(ProductService productService,
                                RawMaterialService rawMaterialService,
                                BomService bomService) {
        this.productService = productService;
        this.rawMaterialService = rawMaterialService;
        this.bomService = bomService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", productService.findAll());
        return "products/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("pageTitle", "Add Product");
        model.addAttribute("formAction", "/products");
        return "products/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("product") Product product,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Add Product");
            model.addAttribute("formAction", "/products");
            return "products/form";
        }
        productService.create(product);
        redirectAttributes.addFlashAttribute("success", "Product created successfully.");
        return "redirect:/products";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.findById(id));
        model.addAttribute("pageTitle", "Edit Product");
        model.addAttribute("formAction", "/products/" + id);
        return "products/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("product") Product product,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Edit Product");
            model.addAttribute("formAction", "/products/" + id);
            return "products/form";
        }
        productService.update(id, product);
        redirectAttributes.addFlashAttribute("success", "Product updated successfully.");
        return "redirect:/products";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        productService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Product deleted successfully.");
        return "redirect:/products";
    }

    @GetMapping("/{id}/bom")
    public String bom(@PathVariable Long id, Model model) {
        Product product = productService.findById(id);
        BomItemRequest request = new BomItemRequest();
        request.setProductCode(product.getProductCode());
        request.setQuantityPerUnit(BigDecimal.ONE);

        model.addAttribute("product", product);
        model.addAttribute("bomItems", bomService.findByProduct(product));
        model.addAttribute("materials", rawMaterialService.findActive());
        model.addAttribute("bomItem", request);
        return "products/bom";
    }

    @PostMapping("/{id}/bom")
    public String addBomItem(@PathVariable Long id,
                             @ModelAttribute("bomItem") BomItemRequest request,
                             RedirectAttributes redirectAttributes) {
        Product product = productService.findById(id);
        request.setProductCode(product.getProductCode());
        bomService.addBomItem(request);
        redirectAttributes.addFlashAttribute("success", "BOM item added successfully.");
        return "redirect:/products/" + id + "/bom";
    }

    @PostMapping("/{productId}/bom/{bomItemId}/delete")
    public String deleteBomItem(@PathVariable Long productId,
                                @PathVariable Long bomItemId,
                                RedirectAttributes redirectAttributes) {
        bomService.deleteBomItem(bomItemId);
        redirectAttributes.addFlashAttribute("success", "BOM item deleted successfully.");
        return "redirect:/products/" + productId + "/bom";
    }
}
