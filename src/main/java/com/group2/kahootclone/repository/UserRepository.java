package com.group2.kahootclone.repository;

import com.group2.kahootclone.model.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findUsersByUsername(String username);
    User findUsersByUsernameAndEmail(String username, String email);
    User findUsersByUsernameAndActive (String username, boolean active);

    User findUserByEmailAndProvider(Object email, String provider);

    User findUserByUsername(String username);
}
