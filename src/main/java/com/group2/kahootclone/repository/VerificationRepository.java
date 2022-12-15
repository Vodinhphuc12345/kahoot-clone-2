package com.group2.kahootclone.repository;

import com.group2.kahootclone.model.auth.Verification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationRepository extends JpaRepository <Verification, Integer> {

}
