package com.group2.kahootclone.reposibility;

import com.group2.kahootclone.model.KahootGroup;
import com.group2.kahootclone.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KahootGroupRepository extends JpaRepository<KahootGroup, Integer> {
}
