package f3f.dev1.domain.user.api;

import f3f.dev1.domain.user.application.SessionLoginService;
import f3f.dev1.domain.user.application.UserService;
import f3f.dev1.domain.user.dto.UserDTO;
import f3f.dev1.domain.user.dto.UserDTO.SignUpRequest;
import f3f.dev1.domain.user.dto.UserDTO.UserInfo;
import f3f.dev1.global.common.annotation.LoginCheck;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static f3f.dev1.domain.user.dto.UserDTO.LoginRequest;
import static f3f.dev1.global.common.constants.ResponseConstants.CREATE;
import static f3f.dev1.global.common.constants.ResponseConstants.OK;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final SessionLoginService sessionLoginService;

    @PostMapping(value = "/user/signup")
    public String signUp(@RequestBody SignUpRequest signUpRequest) {
        userService.signUp(signUpRequest);
        return CREATE;
    }

    @PostMapping(value = "/user/login")
    public Long login(@RequestBody LoginRequest loginRequest) {

        return sessionLoginService.login(loginRequest);
    }
    @LoginCheck
    @DeleteMapping(value = "/user/logout")
    public String logout() {
        sessionLoginService.logout();
        return OK;
    }

    @LoginCheck
    @GetMapping(value = "/user/{userId}")
    public UserInfo getUserInfo(@PathVariable Long userId) {
        return sessionLoginService.getCurrentUser(userId).toUserInfo();

    }
}
