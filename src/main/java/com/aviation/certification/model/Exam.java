package com.aviation.certification.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tests")
public class Exam {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String title;
	private String description;
	
	@Column(name = "duration_minutes")
	private Integer durationMinutes;
	
	@Column(name = "is_visible")
	private Boolean isVisible;
	
	@ManyToOne
	@JoinColumn(name = "created_by")
	private User createdBy;
	
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "test_specializations",
			joinColumns = @JoinColumn(name = "test_id"),
			inverseJoinColumns = @JoinColumn(name = "specialization_id"))
	private Set<Specialization> specializations = new HashSet<>();
	
	@OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Question> questions = new HashSet<>();
	
	// Конструкторы
	public Exam() {
		this.isVisible = true;
		this.createdAt = LocalDateTime.now();
	}
	
	public Exam(String title, String description, Integer durationMinutes, User createdBy) {
		this();
		this.title = title;
		this.description = description;
		this.durationMinutes = durationMinutes;
		this.createdBy = createdBy;
	}
	
	// Геттеры и сеттеры
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
	
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }
	
	public Integer getDurationMinutes() { return durationMinutes; }
	public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }
	
	public Boolean getIsVisible() { return isVisible; }
	public void setIsVisible(Boolean isVisible) { this.isVisible = isVisible; }
	
	public User getCreatedBy() { return createdBy; }
	public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }
	
	public LocalDateTime getCreatedAt() { return createdAt; }
	public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
	
	public Set<Specialization> getSpecializations() { return specializations; }
	public void setSpecializations(Set<Specialization> specializations) { this.specializations = specializations; }
	
	public Set<Question> getQuestions() { return questions; }
	public void setQuestions(Set<Question> questions) { this.questions = questions; }
	
	// Метод для добавления специализации
	public void addSpecialization(Specialization specialization) {
		this.specializations.add(specialization);
		specialization.getExams().add(this);
	}
	
	// Метод для удаления специализации
	public void removeSpecialization(Specialization specialization) {
		this.specializations.remove(specialization);
		specialization.getExams().remove(this);
	}
}