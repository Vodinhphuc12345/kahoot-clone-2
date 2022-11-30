package com.group2.kahootclone.reposibility;

import com.group2.kahootclone.model.KahootGroup;
import com.group2.kahootclone.model.User;
import com.group2.kahootclone.model.UserKahootGroup;
import com.group2.kahootclone.model.UserKahootGroupId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserKahootGroupRepository extends JpaRepository<UserKahootGroup, UserKahootGroupId> {
    UserKahootGroup findUserKahootGroupByUserAndKahootGroup(User user, KahootGroup kahootGroup);
}
