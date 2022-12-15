package com.group2.kahootclone.repository;

import com.group2.kahootclone.model.auth.PasswordConfirmation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordConfirmationRepository extends JpaRepository<PasswordConfirmation, Integer> {
    PasswordConfirmation findPasswordConfirmationByCode (String code);
}
