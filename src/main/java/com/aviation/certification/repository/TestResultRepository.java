package com.aviation.certification.repository;

import com.aviation.certification.model.Exam;
import com.aviation.certification.model.Specialization;
import com.aviation.certification.model.TestResult;
import com.aviation.certification.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestResultRepository extends JpaRepository<TestResult, Long> {
	
	List<TestResult> findByUser(User user);
	List<TestResult> findByUserOrderByCompletedAtDesc(User user);
	
	@Query("SELECT DISTINCT e FROM Exam e LEFT JOIN FETCH e.questions q LEFT JOIN FETCH e.specializations")
	List<Exam> findAllWithQuestionsAndSpecializations();
	
	@Query("SELECT COUNT(tr) FROM TestResult tr JOIN tr.exam e JOIN e.specializations s WHERE s = :specialization")
	long countBySpecialization(@Param("specialization") Specialization specialization);
	
	@Query("SELECT AVG(tr.score) FROM TestResult tr JOIN tr.exam e JOIN e.specializations s WHERE s = :specialization")
	Double getAverageScoreBySpecialization(@Param("specialization") Specialization specialization);
}