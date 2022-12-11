package com.group2.kahootclone.filter.resourceFilter;

import com.group2.kahootclone.model.group.KahootGroup;
import com.group2.kahootclone.model.auth.User;
import com.group2.kahootclone.model.group.UserKahootGroup;
import com.group2.kahootclone.reposibility.KahootGroupRepository;
import com.group2.kahootclone.reposibility.UserKahootGroupRepository;
import com.group2.kahootclone.reposibility.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.group2.kahootclone.constant.Roles.*;

@Component("groupRole")
public class Group {
    @Autowired
    UserKahootGroupRepository userKahootGroupRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    KahootGroupRepository kahootGroupRepository;

    public UserKahootGroup getUserKahootGroup(int userId, int groupId){
        Optional<User> retUser = userRepository.findById(userId);
        User user = retUser.isEmpty() ? null : retUser.get();
        Optional<KahootGroup> kahootGroupRet = kahootGroupRepository.findById(groupId);
        KahootGroup kahootGroup = kahootGroupRet.isEmpty() ? null : kahootGroupRet.get();
        return userKahootGroupRepository.findUserKahootGroupByUserAndKahootGroup(user, kahootGroup);
    }

    public boolean isOwner(Authentication authentication, int groupId) {
        int userId = (int) authentication.getPrincipal();
        UserKahootGroup userKahootGroup = getUserKahootGroup(userId, groupId);
        if (userKahootGroup == null) return false;
        return userKahootGroup.getRole().equals(OWNER.toString());
    }

    public boolean isCoOwner(Authentication authentication, int groupId) {
        int userId = (int) authentication.getPrincipal();
        UserKahootGroup userKahootGroup = getUserKahootGroup(userId, groupId);
        if (userKahootGroup == null) return false;
        return userKahootGroup.getRole().equals(CO_OWNER.toString());
    }

    public boolean isMember(Authentication authentication, int groupId) {
        int userId = (int) authentication.getPrincipal();
        UserKahootGroup userKahootGroup = getUserKahootGroup(userId, groupId);
        if (userKahootGroup == null) return false;
        return userKahootGroup.getRole().equals(MEMBER.toString());
    }
}
