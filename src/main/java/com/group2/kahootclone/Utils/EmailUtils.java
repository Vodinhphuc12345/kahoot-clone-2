package com.group2.kahootclone.Utils;

import com.group2.kahootclone.object.Response.authController.PasswordConfirmationResponse;
import com.group2.kahootclone.object.Response.authController.VerificationResponse;

import java.util.Date;

public class EmailUtils {
    public static String buildVerificationTemplate(String fehost, VerificationResponse verification) {
        return com.group2.kahootclone.constant.emailTemplate.verification.TEMPLATE
                .replaceAll("\\{\\{verificationLink\\}\\}", LinkUtils.buildVerificationLink(verification, fehost))
                .replaceAll("\\{\\{expiredTime\\}\\}", String.valueOf(new Date(verification.getTimeExpired())));
    }

    public static String buildInvitationTemplate(String fehost, String usernameFrom, String usernameTo, String code, String groupName) {
        return com.group2.kahootclone.constant.emailTemplate.invitation.TEMPLATE
                .replaceAll("\\{\\{invitationLink\\}\\}", LinkUtils.buildGroupInvitationLink(code, fehost))
                .replaceAll("\\{\\{usernameFrom\\}\\}", String.valueOf(usernameFrom))
                .replaceAll("\\{\\{usernameTo\\}\\}", String.valueOf(usernameTo))
                .replaceAll("\\{\\{groupName\\}\\}", groupName);
    }

    public static String buildPasswordConfirmationTemplate(String fehost, PasswordConfirmationResponse response) {
        return com.group2.kahootclone.constant.emailTemplate.changePasswordConfirm.TEMPLATE
                .replaceAll("\\{\\{confirmationLink\\}\\}", LinkUtils.buildConfirmationLink(response.getCode(), fehost));
    }
}
