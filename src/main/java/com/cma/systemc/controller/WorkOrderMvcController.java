package com.cma.systemc.controller;

import com.cma.systemc.entity.WorkOrder;
import com.cma.systemc.entity.WorkOrderStatus;
import com.cma.systemc.service.WorkOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/work-orders")
public class WorkOrderMvcController {
    private final WorkOrderService service;

    public WorkOrderMvcController(WorkOrderService service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("workOrders", service.findAll());
        return "workorders/list";
    }

    @GetMapping("/{workOrderNo}")
    public String details(@PathVariable String workOrderNo, Model model) {
        WorkOrder workOrder = service.findByWorkOrderNo(workOrderNo);
        model.addAttribute("workOrder", workOrder);
        model.addAttribute("materials", service.findMaterials(workOrder));
        model.addAttribute("statuses", WorkOrderStatus.values());
        return "workorders/details";
    }

    @PostMapping("/{workOrderNo}/complete")
    public String complete(@PathVariable String workOrderNo, RedirectAttributes redirectAttributes) {
        var request = new com.cma.systemc.dto.UpdateWorkOrderStatusRequest();
        request.setStatus(WorkOrderStatus.COMPLETED);
        request.setRemarks("Manufacturing completed from dashboard");
        service.updateStatus(workOrderNo, request);
        redirectAttributes.addFlashAttribute("success", "Work order marked as completed.");
        return "redirect:/work-orders/" + workOrderNo;
    }
}
