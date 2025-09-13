package com.aviation.certification.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "test_results")
public class TestResult {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "test_id")
	private Exam exam;
	
	private Integer score;
	
	@Column(name = "max_score")
	private Integer maxScore;
	
	@Column(name = "started_at")
	private LocalDateTime startedAt;
	
	@Column(name = "completed_at")
	private LocalDateTime completedAt;
	
	// Конструкторы
	public TestResult() {}
	
	public TestResult(User user, Exam exam, Integer score, Integer maxScore) {
		this.user = user;
		this.exam = exam;
		this.score = score;
		this.maxScore = maxScore;
		this.startedAt = LocalDateTime.now();
	}
	
	// Геттеры и сеттеры
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	
	public User getUser() { return user; }
	public void setUser(User user) { this.user = user; }
	
	public Exam getExam() { return exam; }
	public void setExam(Exam exam) { this.exam = exam; }
	
	public Integer getScore() { return score; }
	public void setScore(Integer score) { this.score = score; }
	
	public Integer getMaxScore() { return maxScore; }
	public void setMaxScore(Integer maxScore) { this.maxScore = maxScore; }
	
	public LocalDateTime getStartedAt() { return startedAt; }
	public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }
	
	public LocalDateTime getCompletedAt() { return completedAt; }
	public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
	
	public void completeTest() {
		this.completedAt = LocalDateTime.now();
	}
}