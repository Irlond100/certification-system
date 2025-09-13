package com.aviation.certification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import com.aviation.certification.model.Specialization;

@Repository
public interface SpecializationRepository extends JpaRepository<Specialization, Long> {
	Optional<Specialization> findByCode(String code);
}