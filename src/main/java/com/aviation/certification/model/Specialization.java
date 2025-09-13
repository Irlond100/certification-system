package com.aviation.certification.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "specializations")
public class Specialization {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	@Column(unique = true)
	private String code;
	
	@NotBlank
	private String name;
	
	private String description;
	
	@ManyToMany(mappedBy = "specializations", fetch = FetchType.LAZY)
	private Set<Exam> exams = new HashSet<>();
	
	// Конструкторы, геттеры и сеттеры
	public Specialization() {}
	
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public String getCode() { return code; }
	public void setCode(String code) { this.code = code; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }
	public Set<Exam> getExams() { return exams; }
	public void setExams(Set<Exam> exams) { this.exams = exams; }
}