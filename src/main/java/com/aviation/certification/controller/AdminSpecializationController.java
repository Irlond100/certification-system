package com.aviation.certification.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.aviation.certification.model.Specialization;
import com.aviation.certification.repository.SpecializationRepository;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
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
		try {
			List<Specialization> specializations = specializationRepository.findAll();
			model.addAttribute("specializations", specializations);
			// Убедимся, что объект всегда существует
			if (!model.containsAttribute("newSpecialization")) {
				model.addAttribute("newSpecialization", new Specialization());
			}
			return "admin/specializations";
		} catch (Exception e) {
			model.addAttribute("error", "Ошибка при загрузке специализаций: " + e.getMessage());
			return "admin/specializations";
		}
	}
	
	@PostMapping("/create")
	public String createSpecialization(@ModelAttribute("newSpecialization") Specialization specialization,
			Model model) {
		try {
			// Проверка на уникальность кода
			if (specializationRepository.findByCode(specialization.getCode()).isPresent()) {
				model.addAttribute("error", "Специализация с таким кодом уже существует");
				return listSpecializations(model);
			}
			
			specializationRepository.save(specialization);
			return "redirect:/admin/specializations?success";
		} catch (DataIntegrityViolationException e) {
			model.addAttribute("error", "Ошибка: специализация с таким кодом уже существует");
			return listSpecializations(model);
		} catch (Exception e) {
			model.addAttribute("error", "Ошибка при создании специализации: " + e.getMessage());
			return listSpecializations(model);
		}
	}
	
	@PostMapping("/edit/{id}")
	public String updateSpecialization(@PathVariable Long id, @ModelAttribute Specialization specialization, Model model) {
		try {
			Optional<Specialization> existingSpec = specializationRepository.findById(id);
			if (existingSpec.isPresent()) {
				Specialization spec = existingSpec.get();
				
				// Проверка на уникальность кода (если код изменился)
				if (!spec.getCode().equals(specialization.getCode()) &&
						specializationRepository.findByCode(specialization.getCode()).isPresent()) {
					model.addAttribute("error", "Специализация с таким кодом уже существует");
					model.addAttribute("specializations", specializationRepository.findAll());
					return "admin/specializations";
				}
				
				spec.setName(specialization.getName());
				spec.setDescription(specialization.getDescription());
				spec.setCode(specialization.getCode());
				specializationRepository.save(spec);
			}
			return "redirect:/admin/specializations?success";
		} catch (Exception e) {
			model.addAttribute("error", "Ошибка при обновлении специализации: " + e.getMessage());
			model.addAttribute("specializations", specializationRepository.findAll());
			return "admin/specializations";
		}
	}
	
	@PostMapping("/delete/{id}")
	public String deleteSpecialization(@PathVariable Long id, Model model) {
		try {
			specializationRepository.deleteById(id);
			return "redirect:/admin/specializations?success";
		} catch (DataIntegrityViolationException e) {
			model.addAttribute("error", "Невозможно удалить специализацию: есть связанные данные");
			return listSpecializations(model);
		} catch (Exception e) {
			model.addAttribute("error", "Ошибка при удалении специализации: " + e.getMessage());
			return listSpecializations(model);
		}
	}
}