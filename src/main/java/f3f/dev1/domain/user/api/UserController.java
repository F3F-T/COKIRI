package f3f.dev1.domain.user.api;

import f3f.dev1.domain.user.application.UserService;
import f3f.dev1.domain.user.dto.UserDTO;
import f3f.dev1.domain.user.dto.UserDTO.SignUpRequest;
import f3f.dev1.domain.user.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static f3f.dev1.global.common.constants.ResponseConstants.OK;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final SessionManager sessionManager;

    @PostMapping(value = "/user/signup")
    public String signUp(@RequestBody SignUpRequest signUpRequest) {
        userService.signUp(signUpRequest);
        return OK;
    }

    @PostMapping(value = "/user/login")
    public String login(@RequestBody UserDTO.LoginRequest loginRequest) {
        userService.login(loginRequest);
        return OK;
    }
}
