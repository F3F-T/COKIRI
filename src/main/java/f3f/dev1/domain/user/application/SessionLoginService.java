package f3f.dev1.domain.user.application;

import f3f.dev1.domain.user.dao.UserRepository;
import f3f.dev1.domain.user.dto.UserDTO;
import f3f.dev1.domain.user.exception.NotFoundByEmailException;
import f3f.dev1.domain.user.exception.UserNotFoundException;
import f3f.dev1.domain.user.model.User;
import f3f.dev1.domain.user.model.UserLevel;
import f3f.dev1.global.common.constants.UserConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

import static f3f.dev1.domain.user.dto.UserDTO.*;
import static f3f.dev1.global.common.constants.UserConstants.AUTH_State;
import static f3f.dev1.global.common.constants.UserConstants.USER_ID;

@Service
@RequiredArgsConstructor
public class SessionLoginService {
    private final HttpSession httpSession;

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public void existsByEmailAndPassword(LoginRequest loginRequest) {
        loginRequest.encrypt();
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        if (!userRepository.existsByEmailAndPassword(email, password)) {
            throw new UserNotFoundException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }
    }

    @Transactional(readOnly = true)
    public void login(LoginRequest loginRequest) {
        existsByEmailAndPassword(loginRequest);
        String email = loginRequest.getEmail();
        setUserLevel(email);
        httpSession.setAttribute(USER_ID, email);
    }

    public void setUserLevel(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다."));

        httpSession.setAttribute(AUTH_State,user.getUserLevel());
    }

    public void logout() {
        httpSession.removeAttribute(USER_ID);

    }

    public UserInfo getCurrentUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(NotFoundByEmailException::new).toUserInfo();

    }

    public String getLoginUser() {
        return (String) httpSession.getAttribute(USER_ID);
    }

    public UserLevel getUserLevel() {
        return (UserLevel) httpSession.getAttribute(AUTH_State);
    }
}
