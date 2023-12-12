package com.my.worldwave.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class OAuth2UnlinkService {

    private final String UNLINK_URL_GOOGLE;
    private final RestTemplate restTemplate;

    public OAuth2UnlinkService(@Value("${oauth2.google.unlink-url}") String unlinkUrlGoogle, RestTemplate restTemplate) {
        this.UNLINK_URL_GOOGLE = unlinkUrlGoogle;
        this.restTemplate = restTemplate;
    }

    public void unlinkGoogle(String oAuth2accessToken) {
        String unlinkUrl = UNLINK_URL_GOOGLE + oAuth2accessToken;
        try {
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(unlinkUrl, null, String.class);
            if (!responseEntity.getStatusCode().is2xxSuccessful()) {
                handleUnlinkFailure();
            }
        } catch (Exception e) {
            handleUnlinkFailure();
        }

    }

    private void handleUnlinkFailure() {
        throw new OAuth2AuthenticationException("연동 해제에 실패했습니다. 다시 로그인 해주세요.");
    }

}
