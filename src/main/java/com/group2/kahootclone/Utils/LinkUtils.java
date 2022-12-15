package com.group2.kahootclone.Utils;

import com.group2.kahootclone.object.Response.authController.VerificationResponse;

public class LinkUtils {
    public static String buildVerificationLink(VerificationResponse verification, String fehost) {
        return String.format("%s/verification?verificationId=%s&code=%s",
                fehost,
                verification.getId(),
                verification.getCode());
    }

    public static String buildGroupInvitationLink(String code, String fehost) {
        return String.format("%s/invitation?code=%s",
                fehost,
                code);
    }

    public static String buildPresentationCollaboratorInvitationLink(String code, String fehost) {
        return String.format("%s/presentation/collaborator?code=%s",
                fehost,
                code);
    }

    public static String buildConfirmationLink(String code, String fehost) {
        return String.format("%s/password/confirmation?code=%s",
                fehost,
                code);
    }
}
