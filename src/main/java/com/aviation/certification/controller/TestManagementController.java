package com.aviation.certification.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.aviation.certification.model.*;
import com.aviation.certification.service.TestService;
import com.aviation.certification.repository.*;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/tests")
@PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
public class TestManagementController {
	
	private final TestService testService;
	private final SpecializationRepository specializationRepository;
	private final QuestionRepository questionRepository;
	private final AnswerRepository answerRepository;
	private final ExamRepository examRepository;
	
	public TestManagementController(TestService testService,
			SpecializationRepository specializationRepository,
			QuestionRepository questionRepository,
			AnswerRepository answerRepository,ExamRepository examRepository) {
		this.testService = testService;
		this.specializationRepository = specializationRepository;
		this.questionRepository = questionRepository;
		this.answerRepository = answerRepository;
		this.examRepository = examRepository;
	}
	
	@GetMapping("/manage")
	public String manageTests(Model model) {
		model.addAttribute("tests", testService.getAllTests());
		return "admin/test-management";
	}
	
	@GetMapping("/create")
	public String showCreateTestForm(Model model) {
		model.addAttribute("exam", new Exam());
		model.addAttribute("specializations", specializationRepository.findAll());
		return "admin/test-create";
	}
	
	@PostMapping("/create")
	public String createTest(@ModelAttribute Exam exam,
			@RequestParam("specializations") List<Long> specializationIds,
			Authentication authentication) {
		// Установите создателя теста
		User currentUser = (User) authentication.getPrincipal();
		exam.setCreatedBy(currentUser);
		
		// Добавьте специализации
		for (Long specId : specializationIds) {
			Specialization spec = specializationRepository.findById(specId)
					.orElseThrow(() -> new RuntimeException("Specialization not found"));
			exam.addSpecialization(spec);
		}
		
		testService.saveExam(exam);
		return "redirect:/admin/tests/manage";
	}
	
	@GetMapping("/edit/{id}")
	public String editTest(@PathVariable Long id, Model model) {
		Optional<Exam> exam = testService.getExamById(id);
		if (exam.isPresent()) {
			model.addAttribute("exam", exam.get());
			model.addAttribute("specializations", specializationRepository.findAll());
			return "admin/test-edit";
		}
		return "redirect:/admin/tests/manage";
	}
	
	@PostMapping("/edit/{id}")
	public String updateTest(@PathVariable Long id, @ModelAttribute Exam exam,
			@RequestParam("specializations") List<Long> specializationIds) {
		exam.setId(id);
		
		// Очистите и добавьте специализации
		exam.getSpecializations().clear();
		for (Long specId : specializationIds) {
			Specialization spec = specializationRepository.findById(specId)
					.orElseThrow(() -> new RuntimeException("Specialization not found"));
			exam.addSpecialization(spec);
		}
		
		testService.saveExam(exam);
		return "redirect:/admin/tests/manage";
	}
	
	@GetMapping("/questions/{testId}")
	public String manageQuestions(@PathVariable Long testId, Model model) {
		Optional<Exam> exam = testService.getExamById(testId);
		if (exam.isPresent()) {
			model.addAttribute("exam", exam.get());
			model.addAttribute("question", new Question());
			return "admin/question-management";
		}
		return "redirect:/admin/tests/manage";
	}
	
	@PostMapping("/questions/{testId}/add")
	public String addQuestion(@PathVariable Long testId, @ModelAttribute Question question,
			@RequestParam("answers") List<String> answerTexts,
			@RequestParam("correctAnswers") List<Integer> correctAnswerIndices) {
		Optional<Exam> exam = testService.getExamById(testId);
		if (exam.isPresent()) {
			question.setExam(exam.get());
			questionRepository.save(question);
			
			// Добавьте ответы
			for (int i = 0; i < answerTexts.size(); i++) {
				Answer answer = new Answer();
				answer.setQuestion(question);
				answer.setText(answerTexts.get(i));
				answer.setIsCorrect(correctAnswerIndices.contains(i));
				answerRepository.save(answer);
			}
			
			return "redirect:/admin/tests/questions/" + testId;
		}
		return "redirect:/admin/tests/manage";
	}
	
	@GetMapping("/delete/{id}")
	public String deleteTest(@PathVariable Long id) {
		testService.getExamById(id).ifPresent(exam -> {
			// Удалите связанные вопросы и ответы перед удалением теста
			exam.getQuestions().forEach(question -> {
				question.getAnswers().forEach(answerRepository::delete);
				questionRepository.delete(question);
			});
			examRepository.delete(exam);
		});
		return "redirect:/admin/tests/manage";
	}
}