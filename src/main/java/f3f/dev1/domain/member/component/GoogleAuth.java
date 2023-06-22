package f3f.dev1.domain.member.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import f3f.dev1.domain.member.dto.OAuthDTO;
import f3f.dev1.domain.member.dto.OAuthDTO.GoogleOAuthToken;
import f3f.dev1.domain.member.dto.OAuthDTO.GoogleUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GoogleAuth implements SocialAuth{
    @Value("${spring.OAuth2.google.url}")
    private String GOOGLE_SNS_LOGIN_URL;

    @Value("${google.client}")
    private String GOOGLE_SNS_CLIENT_ID;

    @Value("${spring.OAuth2.google.callback-url}")
    private String GOOGLE_SNS_CALLBACK_URL;

    @Value("${google.secret}")
    private String GOOGLE_SNS_CLIENT_SECRET;

    @Value("${spring.OAuth2.google.scope}")
    private String GOOGLE_DATA_ACCESS_SCOPE;

    @Value("${spring.OAuth2.google.token_request}")
    private String GOOGLE_TOKEN_REQUEST_URL;

    @Value("${spring.OAuth2.google.userinfo_request}")
    private String GOOGLE_USERINFO_REQUEST_URL;

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    @Override
    public String getOAuthRedirectURL() {
        Map<String, Object> params = new HashMap<>();
        params.put("scope", GOOGLE_DATA_ACCESS_SCOPE);
        params.put("response_type", "code");
        params.put("client_id", GOOGLE_SNS_CLIENT_ID);
        params.put("redirect_uri", GOOGLE_SNS_CALLBACK_URL);

        String parameterString = params
                .entrySet()
                .stream()
                .map(x -> x.getKey() + "=" + x.getValue())
                .collect(Collectors.joining("&"));
        String redirectURL = GOOGLE_SNS_LOGIN_URL + "?" + parameterString;
        System.out.println("redirectURL = " + redirectURL);
        return redirectURL;
    }

    public ResponseEntity<String> requestAccessToken(String code) {
        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("client_id", GOOGLE_SNS_CLIENT_ID);
        params.put("client_secret", GOOGLE_SNS_CLIENT_SECRET);
        params.put("redirect_uri", GOOGLE_SNS_CALLBACK_URL);
        params.put("grant_type", "authorization_code");
        ResponseEntity<String> response = restTemplate.postForEntity(GOOGLE_TOKEN_REQUEST_URL, params, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response;
        } else {
            return null;
        }

    }

    public GoogleOAuthToken getAccessToken(String token) throws JsonProcessingException {
        return objectMapper.readValue(token, GoogleOAuthToken.class);
    }

    public ResponseEntity<String> requestUserInfo(String token) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + token);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(httpHeaders);
        return restTemplate.exchange(GOOGLE_USERINFO_REQUEST_URL, HttpMethod.GET, request, String.class);
    }

    public GoogleUser getUserInfo(ResponseEntity<String> userInfo) throws JsonProcessingException {
        return objectMapper.readValue(userInfo.getBody(), GoogleUser.class);
    }
}
