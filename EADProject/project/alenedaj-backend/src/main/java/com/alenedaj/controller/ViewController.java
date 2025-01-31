package com.alenedaj.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping({"/", "/login"})
    public String loginPage() {
        return "login";  // renders login.html from templates
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register"; // renders register.html
    }

    @GetMapping("/dashboard")
    public String dashboardPage() {
        return "dashboard"; // renders dashboard.html
    }

    @GetMapping("/admin/gas-stations")
    public String adminGasStationsPage() {
        return "admin-gasstations"; // renders admin-gasstations.html
    }
}
