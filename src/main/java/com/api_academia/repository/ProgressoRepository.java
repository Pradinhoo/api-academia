package com.api_academia.repository;

import com.api_academia.model.Progresso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgressoRepository extends JpaRepository<Progresso, Long> {
}
