package com.group2.kahootclone.repository;

import com.group2.kahootclone.model.group.KahootGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KahootGroupRepository extends JpaRepository<KahootGroup, Integer> {
}
