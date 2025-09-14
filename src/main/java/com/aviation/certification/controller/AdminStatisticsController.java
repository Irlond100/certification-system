package com.aviation.certification.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.aviation.certification.model.Specialization;
import com.aviation.certification.repository.*;
import java.util.*;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminStatisticsController {
	private final UserRepository userRepository;
	private final ExamRepository examRepository;
	private final TestResultRepository testResultRepository;
	private final SpecializationRepository specializationRepository;
	
	public AdminStatisticsController(UserRepository userRepository, ExamRepository examRepository,
			TestResultRepository testResultRepository,
			SpecializationRepository specializationRepository) {
		this.userRepository = userRepository;
		this.examRepository = examRepository;
		this.testResultRepository = testResultRepository;
		this.specializationRepository = specializationRepository;
	}
	
	@GetMapping("/statistics")
	public String showStatistics(Model model) {
		// Базовая статистика
		long totalUsers = userRepository.count();
		long totalTests = examRepository.count();
		long totalResults = testResultRepository.count();
		
		model.addAttribute("totalUsers", totalUsers);
		model.addAttribute("totalTests", totalTests);
		model.addAttribute("totalResults", totalResults);
		
		// Статистика по специализациям (заглушка)
		List<Map<String, Object>> specializationStats = new ArrayList<>();
		List<Specialization> specializations = specializationRepository.findAll();
		
		for (Specialization spec : specializations) {
			Map<String, Object> stat = new HashMap<>();
			stat.put("specializationName", spec.getName());
			stat.put("testCount", 5); // Заглушка
			stat.put("averageScore", 75); // Заглушка
			stat.put("attemptCount", 10); // Заглушка
			
			specializationStats.add(stat);
		}
		
		model.addAttribute("specializationStats", specializationStats);
		
		return "admin/statistics";
	}
}