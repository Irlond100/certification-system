package com.aviation.certification.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.aviation.certification.model.Specialization;
import com.aviation.certification.repository.SpecializationRepository;
import java.util.List;

@Controller
@RequestMapping("/admin/specializations")
@PreAuthorize("hasRole('ADMIN')")
public class AdminSpecializationController {
	private final SpecializationRepository specializationRepository;
	
	public AdminSpecializationController(SpecializationRepository specializationRepository) {
		this.specializationRepository = specializationRepository;
	}
	
	@GetMapping
	public String listSpecializations(Model model) {
		List<Specialization> specializations = specializationRepository.findAll();
		model.addAttribute("specializations", specializations);
		return "admin/specializations";
	}
}