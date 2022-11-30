package com.group2.kahootclone.Utils;

import com.group2.kahootclone.model.Verification;
import com.group2.kahootclone.object.Response.authController.VerificationResponse;

import java.util.Date;

public class EmailUtils {
    public static String buildVerificationTemplate(String behost, VerificationResponse verification) {
        return com.group2.kahootclone.constant.emailTemplate.verification.TEMPLATE
                .replaceAll("\\{\\{verificationLink\\}\\}", LinkUtils.buildVerificationLink(verification, behost))
                .replaceAll("\\{\\{expiredTime\\}\\}", String.valueOf(new Date(verification.getTimeExpired())));
    }

    public static String buildInvitationTemplate(String fehost, String usernameFrom, String usernameTo, String code, String groupName) {
        return com.group2.kahootclone.constant.emailTemplate.invitation.TEMPLATE
                .replaceAll("\\{\\{invitationLink\\}\\}", LinkUtils.buildInvitationLink(code, fehost))
                .replaceAll("\\{\\{usernameFrom\\}\\}", String.valueOf(usernameFrom))
                .replaceAll("\\{\\{usernameTo\\}\\}", String.valueOf(usernameTo))
                .replaceAll("\\{\\{group\\}\\}", groupName);
    }
}
