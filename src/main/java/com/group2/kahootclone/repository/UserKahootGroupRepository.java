package com.group2.kahootclone.repository;

import com.group2.kahootclone.model.group.KahootGroup;
import com.group2.kahootclone.model.auth.User;
import com.group2.kahootclone.model.group.UserKahootGroup;
import com.group2.kahootclone.model.group.UserKahootGroupId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserKahootGroupRepository extends JpaRepository<UserKahootGroup, UserKahootGroupId> {
    UserKahootGroup findUserKahootGroupByUserAndKahootGroup(User user, KahootGroup kahootGroup);
}
