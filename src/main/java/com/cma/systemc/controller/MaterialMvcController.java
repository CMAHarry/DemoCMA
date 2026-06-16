package com.cma.systemc.controller;

import com.cma.systemc.entity.RawMaterial;
import com.cma.systemc.service.RawMaterialService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/materials")
public class MaterialMvcController {
    private final RawMaterialService service;

    public MaterialMvcController(RawMaterialService service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("materials", service.findAll());
        return "materials/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("material", new RawMaterial());
        model.addAttribute("pageTitle", "Add Raw Material");
        model.addAttribute("formAction", "/materials");
        return "materials/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("material") RawMaterial material,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Add Raw Material");
            model.addAttribute("formAction", "/materials");
            return "materials/form";
        }
        service.create(material);
        redirectAttributes.addFlashAttribute("success", "Raw material created successfully.");
        return "redirect:/materials";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("material", service.findById(id));
        model.addAttribute("pageTitle", "Edit Raw Material");
        model.addAttribute("formAction", "/materials/" + id);
        return "materials/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("material") RawMaterial material,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Edit Raw Material");
            model.addAttribute("formAction", "/materials/" + id);
            return "materials/form";
        }
        service.update(id, material);
        redirectAttributes.addFlashAttribute("success", "Raw material updated successfully.");
        return "redirect:/materials";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        service.delete(id);
        redirectAttributes.addFlashAttribute("success", "Raw material deleted successfully.");
        return "redirect:/materials";
    }
}
