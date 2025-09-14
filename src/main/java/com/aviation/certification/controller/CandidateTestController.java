package com.aviation.certification.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/candidate")
@PreAuthorize("hasRole('CANDIDATE')")
public class CandidateTestController {
	
	@GetMapping("/tests")
	public String availableTests(Model model) {
		// Здесь будет логика получения доступных тестов
		return "candidate/tests";
	}
}
