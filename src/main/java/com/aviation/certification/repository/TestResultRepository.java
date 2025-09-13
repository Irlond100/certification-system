package com.aviation.certification.repository;

import com.aviation.certification.model.TestResult;
import com.aviation.certification.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestResultRepository extends JpaRepository<TestResult, Long> {
	List<TestResult> findByUser(User user);
	List<TestResult> findByUserOrderByCompletedAtDesc(User user);
}