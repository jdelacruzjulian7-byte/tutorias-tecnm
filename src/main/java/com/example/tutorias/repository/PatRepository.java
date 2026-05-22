package com.example.tutorias.repository;

import com.example.tutorias.model.Pat;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatRepository extends JpaRepository<Pat, Long> {
	
	List<Pat> findBySemestre_Id(Long semestreId);
	List<Pat> findByCarrera_Id(Long carreraId);
	List<Pat> findByVersionContainingIgnoreCase(String version);
}