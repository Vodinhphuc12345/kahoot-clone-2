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

    public UserKahootGroup getUserKahootGroup(int userId, int groupId) {
        Optional<User> retUser = userRepository.findById(userId);
        User user = retUser.orElse(null);
        Optional<KahootGroup> kahootGroupRet = kahootGroupRepository.findById(groupId);
        KahootGroup kahootGroup = kahootGroupRet.orElse(null);
        return userKahootGroupRepository.findUserKahootGroupByUserAndKahootGroup(user, kahootGroup);
    }


    public com.group2.kahootclone.model.Presentation getPresentation(int presentationId) {
        Optional<com.group2.kahootclone.model.Presentation> presentationRes = presentationRepository.findById(presentationId);
        return presentationRes.orElse(null);
    }

    public boolean isCreator(Authentication authentication, int presentationId) {
        com.group2.kahootclone.model.Presentation presentation = getPresentation(presentationId);
        if (presentation == null) return false;
        int userId = (int) authentication.getPrincipal();
        return userId == presentation.getUser().getId();
    }

    public boolean isSharingCreator(Authentication authentication, int presentationId) {
        com.group2.kahootclone.model.Presentation presentation = getPresentation(presentationId);
        if (presentation == null || !(presentation.getKahootGroup() == null || presentation.getKahootGroup().isEmpty()))
            return false;
        int userId = (int) authentication.getPrincipal();

        for (KahootGroup kahootGroup : presentation.getKahootGroup()) {
            UserKahootGroup userKahootGroup = getUserKahootGroup(userId, kahootGroup.getId());
            if (userKahootGroup.getRole().equals(OWNER.name()) || userKahootGroup.getRole().equals(CO_OWNER.name()))
                return true;
        }
        return false;
    }

    public boolean isParticipant(Authentication authentication, int presentationId) {
        com.group2.kahootclone.model.Presentation presentation = getPresentation(presentationId);
        if (presentation == null) return false;
        if (presentation.getKahootGroup() == null || presentation.getKahootGroup().isEmpty()) return true;
        int userId = (int) authentication.getPrincipal();

        for (KahootGroup kahootGroup : presentation.getKahootGroup()) {
            UserKahootGroup userKahootGroup = getUserKahootGroup(userId, kahootGroup.getId());
            if (userKahootGroup != null)
                return true;
        }
        return false;
    }
}
