package com.group2.kahootclone.filter.resourceFilter;

import com.group2.kahootclone.model.group.KahootGroup;
import com.group2.kahootclone.model.auth.User;
import com.group2.kahootclone.model.group.UserKahootGroup;
import com.group2.kahootclone.repository.KahootGroupRepository;
import com.group2.kahootclone.repository.PresentationRepository;
import com.group2.kahootclone.repository.UserKahootGroupRepository;
import com.group2.kahootclone.repository.UserRepository;
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

    public UserKahootGroup getUserKahootGroup(int userId, int groupId) {
        Optional<User> retUser = userRepository.findById(userId);
        User user = retUser.orElse(null);
        Optional<KahootGroup> kahootGroupRet = kahootGroupRepository.findById(groupId);
        KahootGroup kahootGroup = kahootGroupRet.orElse(null);
        return userKahootGroupRepository.findUserKahootGroupByUserAndKahootGroup(user, kahootGroup);
    }


    public com.group2.kahootclone.model.presentation.Presentation getPresentation(int presentationId) {
        Optional<com.group2.kahootclone.model.presentation.Presentation> presentationRes = presentationRepository.findById(presentationId);
        return presentationRes.orElse(null);
    }

    public boolean isCreator(Authentication authentication, int presentationId) {
        com.group2.kahootclone.model.presentation.Presentation presentation = getPresentation(presentationId);
        if (presentation == null) return false;
        int userId = (int) authentication.getPrincipal();
        return userId == presentation.getUser().getId();
    }

    public boolean isCollaborator(Authentication authentication, int presentationId) {
        int userId = (int) authentication.getPrincipal();
        com.group2.kahootclone.model.presentation.Presentation presentation = getPresentation(presentationId);
        if (presentation == null) return false;
        User user = presentation.getCollaborators().stream().filter(c -> c.getId() == userId).findAny().orElse(null);
        return user != null;
    }

    public boolean isSupporter(Authentication authentication, int presentationId) {
        com.group2.kahootclone.model.presentation.Presentation presentation = getPresentation(presentationId);
        if (presentation == null || presentation.getPresentingGroup() == null)
            return false;
        int userId = (int) authentication.getPrincipal();

        KahootGroup kahootGroup = presentation.getPresentingGroup();
        UserKahootGroup userKahootGroup = getUserKahootGroup(userId, kahootGroup.getId());
        return userKahootGroup.getRole().equals(OWNER.name()) || userKahootGroup.getRole().equals(CO_OWNER.name());
    }

    public boolean isParticipant(Authentication authentication, int presentationId) {
        com.group2.kahootclone.model.presentation.Presentation presentation = getPresentation(presentationId);
        if (presentation == null) return false;
        if (presentation.getPresentingGroup() == null) return true;
        int userId = (int) authentication.getPrincipal();

        KahootGroup kahootGroup = presentation.getPresentingGroup();
        UserKahootGroup userKahootGroup = getUserKahootGroup(userId, kahootGroup.getId());
        return userKahootGroup != null;
    }
}
