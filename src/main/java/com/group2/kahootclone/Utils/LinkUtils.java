package com.group2.kahootclone.Utils;

import com.group2.kahootclone.object.Response.authController.VerificationResponse;

public class LinkUtils {
    public static String buildVerificationLink(VerificationResponse verification, String behost) {
        return String.format("%s/api/v1/auth/verification?verificationId=%s&code=%s",
                behost,
                verification.getId(),
                verification.getCode());
    }

    public static String buildInvitationLink(String code, String fehost) {
        return String.format("%s/invitation?code=%s",
                fehost,
                code);
    }
}
