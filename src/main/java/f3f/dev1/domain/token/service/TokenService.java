package f3f.dev1.domain.token.service;

import f3f.dev1.domain.token.dao.RefreshTokenRepository;
import f3f.dev1.domain.token.exception.InvalidAccessTokenException;
import f3f.dev1.domain.token.exception.LogoutUserException;
import f3f.dev1.domain.token.model.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken save(RefreshToken refreshToken) {
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken findById(String userId) {
        return refreshTokenRepository.findById(userId).orElseThrow(LogoutUserException::new);
    }

    public RefreshToken findByAccessToken(String accessToken) {
        return refreshTokenRepository.findByAccessToken(accessToken).orElseThrow(InvalidAccessTokenException::new);
    }

    public RefreshToken update(String refreshToken, String userId) {
        RefreshToken token = refreshTokenRepository.findById(userId).orElseThrow(LogoutUserException::new);
        token.update(refreshToken);
        return token;

    }

    public void delete(String userId) {
        RefreshToken token = refreshTokenRepository.findById(userId).orElseThrow(LogoutUserException::new);
        refreshTokenRepository.delete(token);
    }
}

