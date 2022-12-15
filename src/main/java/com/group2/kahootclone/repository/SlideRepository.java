package com.group2.kahootclone.repository;

import com.group2.kahootclone.model.presentation.Slide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SlideRepository extends JpaRepository<Slide, Integer> {
}
