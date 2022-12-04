package com.group2.kahootclone.reposibility;

import com.group2.kahootclone.model.Presentation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PresentationRepository extends JpaRepository<Presentation, Integer> {
}
