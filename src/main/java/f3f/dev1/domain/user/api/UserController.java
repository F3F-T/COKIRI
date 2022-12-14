package f3f.dev1.domain.user.api;

import f3f.dev1.domain.user.application.SessionLoginService;
import f3f.dev1.domain.user.application.UserService;
import f3f.dev1.global.common.annotation.LoginCheck;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static f3f.dev1.domain.user.dto.UserDTO.*;
import static f3f.dev1.global.common.constants.ResponseConstants.CREATE;
import static f3f.dev1.global.common.constants.ResponseConstants.OK;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final SessionLoginService sessionLoginService;
    // 이메일 찾기
    // TODO: 던지는 예외 메시지도 한번 다듬어야될 것 같음, 같은 예외 재사용이 많아서 비밀번호 찾기에서 유저 못 찾았는데 ID 비밀번호 확인하라는 메시지가 출력됨
    @PostMapping(value = "/user/find/email")
    public ResponseEntity<EncryptEmailDto> findEmail(@RequestBody FindEmailDto findEmailDto) {
        EncryptEmailDto userEmail = userService.findUserEmail(findEmailDto);
        return new ResponseEntity<>(userEmail, HttpStatus.OK);
    }

    // 비밀번호 찾기

    @PostMapping(value = "/user/find/password")
    public ResponseEntity<ReturnPasswordDto> findPassword(@RequestBody FindPasswordDto findPasswordDto) {
        ReturnPasswordDto userPassword = userService.findUserPassword(findPasswordDto);
        return new ResponseEntity<>(userPassword, HttpStatus.OK);


    }

    // 회원가입
    @PostMapping(value = "/user/signup")
    public ResponseEntity<String> signUp(@RequestBody SignUpRequest signUpRequest) {
        // TODO: 회원가입할때 정보 검증을 프론트에서 할지 백에서할지 결정해야함

        userService.signUp(signUpRequest);
        return CREATE;
    }
    // 로그인
    @PostMapping(value = "/user/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        sessionLoginService.login(loginRequest);
        return OK;
    }
    // 로그아웃
    @LoginCheck
    @DeleteMapping(value = "/user/logout")
    public ResponseEntity<String> logout() {
        sessionLoginService.logout();
        return OK;
    }
    // 유저 정보 조회
    @LoginCheck
    @GetMapping(value = "/user")
    public UserInfo getUserInfo() {
        return userService.findUserInfoByEmail(sessionLoginService.getLoginUser()).toUserInfo();

    }
    // 유저 정보 수정
    @LoginCheck
    @PatchMapping(value = "/user")
    public ResponseEntity<UserInfo> updateUserInfo(@RequestBody UpdateUserInfo updateUserInfo) {

        return new ResponseEntity<>(userService.updateUserInfo(updateUserInfo), HttpStatus.OK);
    }
    // 유저 삭제
    @LoginCheck
    @DeleteMapping(value = "/user")
    public ResponseEntity<String> deleteUser() {
        return new ResponseEntity<>(userService.deleteUser(), HttpStatus.OK);

    }
    // 유저 비밀번호 변정
    @LoginCheck
    @PatchMapping(value = "/user/password")
    public ResponseEntity<String> updateUserPassword(@RequestBody UpdateUserPassword updateUserPassword) {


        return new ResponseEntity<>(userService.updateUserPassword(updateUserPassword), HttpStatus.OK);
    }


}
