package com.group2.kahootclone.repository;

import com.group2.kahootclone.model.presentation.Presentation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PresentationRepository extends JpaRepository<Presentation, Integer> {
}