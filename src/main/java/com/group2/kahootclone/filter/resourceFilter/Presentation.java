package com.group2.kahootclone.filter.resourceFilter;

import com.group2.kahootclone.model.KahootGroup;
import com.group2.kahootclone.model.User;
import com.group2.kahootclone.model.UserKahootGroup;
import com.group2.kahootclone.reposibility.KahootGroupRepository;
import com.group2.kahootclone.reposibility.PresentationRepository;
import com.group2.kahootclone.reposibility.UserKahootGroupRepository;
import com.group2.kahootclone.reposibility.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.group2.kahootclone.constant.Roles.*;

@Component("presentationRole")
public class Presentation {
    @Autowired
    UserKahootGroupRepository userKahootGroupRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    KahootGroupRepository kahootGroupRepository;

    @Autowired
    PresentationRepository presentationRepository;

    public UserKahootGroup getUserKahootGroup(int userId, int groupId){
        Optional<User> retUser = userRepository.findById(userId);
        User user = retUser.orElse(null);
        Optional<KahootGroup> kahootGroupRet = kahootGroupRepository.findById(groupId);
        KahootGroup kahootGroup = kahootGroupRet.orElse(null);
        return userKahootGroupRepository.findUserKahootGroupByUserAndKahootGroup(user, kahootGroup);
    }


    public com.group2.kahootclone.model.Presentation getKahootGroupByPresentationId (int presentationId){
        Optional<com.group2.kahootclone.model.Presentation> presentationRes = presentationRepository.findById(presentationId);
        return presentationRes.orElse(null);
    }

    public boolean isOwner(Authentication authentication, int presentationId) {
        com.group2.kahootclone.model.Presentation presentation = getKahootGroupByPresentationId(presentationId);
        if (presentation == null) return false;
        int userId = (int) authentication.getPrincipal();
        UserKahootGroup userKahootGroup = getUserKahootGroup(userId,presentation.getKahootGroup().getId());
        if (userKahootGroup == null) return false;
        return userKahootGroup.getRole().equals(OWNER.toString());
    }

    public boolean isCoOwner(Authentication authentication, int presentationId) {
        com.group2.kahootclone.model.Presentation presentation = getKahootGroupByPresentationId(presentationId);
        if (presentation == null) return false;
        int userId = (int) authentication.getPrincipal();
        UserKahootGroup userKahootGroup = getUserKahootGroup(userId,presentation.getKahootGroup().getId());
        if (userKahootGroup == null) return false;
        return userKahootGroup.getRole().equals(CO_OWNER.toString());
    }

    public boolean isMember(Authentication authentication, int presentationId) {
        com.group2.kahootclone.model.Presentation presentation = getKahootGroupByPresentationId(presentationId);
        if (presentation == null) return false;
        int userId = (int) authentication.getPrincipal();
        UserKahootGroup userKahootGroup = getUserKahootGroup(userId,presentation.getKahootGroup().getId());
        if (userKahootGroup == null) return false;
        return userKahootGroup.getRole().equals(MEMBER.toString());
    }
}
