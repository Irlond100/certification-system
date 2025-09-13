package com.aviation.certification.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	@Column(unique = true)
	private String name;
	
	// Конструкторы, геттеры и сеттеры
	public Role() {}
	
	public Role(String name) {
		this.name = name;
	}
	
	@Override
	public String getAuthority() {
		return name;
	}
	
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
}