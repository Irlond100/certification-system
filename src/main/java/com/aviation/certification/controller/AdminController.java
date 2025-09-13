package com.aviation.certification.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
	
	@GetMapping("/dashboard")
	public String adminDashboard(Model model) {
		// Здесь можно добавить данные для админ-панели
		return "admin/dashboard";
	}
	
	@GetMapping("/users")
	public String manageUsers() {
		return "admin/users";
	}
	
	@GetMapping("/tests")
	public String manageTests() {
		return "admin/tests";
	}
	
	@GetMapping("/statistics")
	public String viewStatistics() {
		return "admin/statistics";
	}
}
