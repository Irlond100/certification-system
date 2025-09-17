package com.aviation.certification.service;

import com.aviation.certification.model.Exam;
import com.aviation.certification.model.Specialization;
import com.aviation.certification.model.TestResult;
import com.aviation.certification.model.User;
import com.aviation.certification.repository.ExamRepository;
import com.aviation.certification.repository.TestResultRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TestService {
	private final ExamRepository examRepository;
	private final TestResultRepository testResultRepository;
	
	public TestService(ExamRepository examRepository, TestResultRepository testResultRepository) {
		this.examRepository = examRepository;
		this.testResultRepository = testResultRepository;
	}
	
	// Добавляем недостающие методы
	public List<Exam> getAllTests() {
		return examRepository.findAll();
	}
	
	public Exam saveExam(Exam exam) {
		return examRepository.save(exam);
	}
	
	public Optional<Exam> getExamById(Long id) {
		return examRepository.findById(id);
	}
	
	// Остальные существующие методы остаются без изменений
	public List<Exam> getExamsBySpecialization(Specialization specialization) {
		return examRepository.findBySpecializationAndIsVisible(specialization);
	}
	
	public List<TestResult> getUserTestResults(User user) {
		return testResultRepository.findByUserOrderByCompletedAtDesc(user);
	}
	
	public TestResult saveTestResult(TestResult testResult) {
		return testResultRepository.save(testResult);
	}
}