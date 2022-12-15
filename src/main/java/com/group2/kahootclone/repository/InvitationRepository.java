package com.group2.kahootclone.repository;

import com.group2.kahootclone.model.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Integer> {
    Invitation findByCode(String code);
}
