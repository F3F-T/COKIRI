package f3f.dev1.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class OAuthDTO {
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class SocialLoginUrlDto {
        public String url;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class GoogleOAuthToken {
        private String access_token;
        private int expires_in;
        private String scope;
        private String token_type;
        private String id_token;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class GoogleUser{
        public String id;
        public String email;
        public Boolean verifiedEmail;
        public String name;
        public String givenName;
        public String familyName;
        public String picture;
        public String locale;
    }

}
