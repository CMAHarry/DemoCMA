package com.cma.systemc.controller;

import com.cma.systemc.service.ProductService;
import com.cma.systemc.service.RawMaterialService;
import com.cma.systemc.service.WorkOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
    private final ProductService productService;
    private final RawMaterialService rawMaterialService;
    private final WorkOrderService workOrderService;

    public DashboardController(ProductService productService,
                               RawMaterialService rawMaterialService,
                               WorkOrderService workOrderService) {
        this.productService = productService;
        this.rawMaterialService = rawMaterialService;
        this.workOrderService = workOrderService;
    }

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        model.addAttribute("productCount", productService.findAll().size());
        model.addAttribute("materialCount", rawMaterialService.findAll().size());
        model.addAttribute("workOrderCount", workOrderService.findAll().size());
        model.addAttribute("latestWorkOrders", workOrderService.findAll());
        return "dashboard";
    }
}
