package com.aviation.certification.controller;

import com.aviation.certification.model.Specialization;
import com.aviation.certification.repository.SpecializationRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
		model.addAttribute("specializations", specializationRepository.findAll());
		return "admin/specializations";
	}
	
	@GetMapping("/create")
	public String showCreateForm(Model model) {
		model.addAttribute("specialization", new Specialization());
		return "admin/specialization-form";
	}
	
	@PostMapping("/create")
	public String createSpecialization(@ModelAttribute Specialization specialization) {
		specializationRepository.save(specialization);
		return "redirect:/admin/specializations";
	}
	
	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable Long id, Model model) {
		Optional<Specialization> specialization = specializationRepository.findById(id);
		if (specialization.isPresent()) {
			model.addAttribute("specialization", specialization.get());
			return "admin/specialization-form";
		}
		return "redirect:/admin/specializations";
	}
	
	@PostMapping("/edit/{id}")
	public String updateSpecialization(@PathVariable Long id, @ModelAttribute Specialization specialization) {
		specialization.setId(id);
		specializationRepository.save(specialization);
		return "redirect:/admin/specializations";
	}
	
	@GetMapping("/delete/{id}")
	public String deleteSpecialization(@PathVariable Long id) {
		specializationRepository.deleteById(id);
		return "redirect:/admin/specializations";
	}
}
