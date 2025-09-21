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
		try {
			long totalUsers = userRepository.count();
			long totalTests = examRepository.count();
			long totalResults = testResultRepository.count();
			
			model.addAttribute("totalUsers", totalUsers);
			model.addAttribute("totalTests", totalTests);
			model.addAttribute("totalResults", totalResults);
			
			List<Map<String, Object>> specializationStats = new ArrayList<>();
			List<Specialization> specializations = specializationRepository.findAll();
			
			for (Specialization spec : specializations) {
				Map<String, Object> stat = new HashMap<>();
				stat.put("specializationName", spec.getName());
				
				// Реальная статистика
				long testCount = examRepository.countBySpecialization(spec);
				long attemptCount = testResultRepository.countBySpecialization(spec);
				
				stat.put("testCount", testCount);
				stat.put("attemptCount", attemptCount);
				
				// Проверка на наличие данных
				if (attemptCount > 0) {
					// Здесь должна быть логика расчета среднего балла
					stat.put("averageScore", "75"); // Заглушка
				} else {
					stat.put("averageScore", "Нет данных");
				}
				
				specializationStats.add(stat);
			}
			
			model.addAttribute("specializationStats", specializationStats);
			
		} catch (Exception e) {
			model.addAttribute("error", "Ошибка при загрузке статистики: " + e.getMessage());
		}
		
		return "admin/statistics";
	}
}