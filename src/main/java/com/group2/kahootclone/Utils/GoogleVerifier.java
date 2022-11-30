package com.group2.kahootclone.Utils;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.group2.kahootclone.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Slf4j
@Component
public class GoogleVerifier {
    NetHttpTransport transport = new NetHttpTransport();
    JsonFactory jsonFactory = new GsonFactory();
    @Value("${kahoot.clone.google_client_id}")
    String client_id;

    public User verifyToken(String tokenId) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                    .setAudience(Collections.singletonList(client_id))
                    .build();
            GoogleIdToken idToken = null;
            idToken = verifier.verify(tokenId);
            if (idToken != null) {
                Payload payload = idToken.getPayload();
                // Print user identifier
                log.info("User Info: ", payload.getEmail());
                // Get profile information from payload
                String email = payload.getEmail();
                String displayName = (String) payload.get("name");
                String avatar = (String) payload.get("picture");

                return User
                        .builder()
                        .displayName(displayName)
                        .email(email)
                        .avatar(avatar)
                        .build();
            } else {
                return null;
            }
        } catch (GeneralSecurityException | IOException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}
