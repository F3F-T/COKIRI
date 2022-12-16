package f3f.dev1.domain.token.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TokenDTO {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class TokenInfoDTO {
        private String grantType;

        private String accessToken;

        private Long accessTokenExpiresIn;

        private String refreshToken;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class TokenRequestDTO{
        private String accessToken;
        private String refreshToken;
    }
}
