package com.alenedaj.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard/admin")
    public String adminDashboard(Model model) {
        // Optionally add model attributes (e.g., list of gas stations via AJAX)
        return "admin-dashboard"; // resolves to admin-dashboard.html
    }

    @GetMapping("/dashboard/user")
    public String userDashboard(Model model) {
        // Optionally add model attributes
        return "user-dashboard"; // resolves to user-dashboard.html
    }
}
