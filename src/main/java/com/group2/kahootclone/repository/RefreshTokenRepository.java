package com.group2.kahootclone.repository;

import com.group2.kahootclone.model.auth.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    RefreshToken findByToken(String token);
    void deleteRefreshTokensByToken(String token);
}
