package f3f.dev1.user;

import f3f.dev1.domain.model.Address;
import f3f.dev1.domain.scrap.dao.ScrapRepository;
import f3f.dev1.domain.scrap.model.Scrap;
import f3f.dev1.domain.user.application.UserService;
import f3f.dev1.domain.user.dao.UserRepository;
import f3f.dev1.domain.user.dto.UserDTO.LoginRequest;
import f3f.dev1.domain.user.dto.UserDTO.UpdateUserInfo;
import f3f.dev1.domain.user.dto.UserDTO.UserInfo;
import f3f.dev1.domain.user.exception.DuplicateEmailException;
import f3f.dev1.domain.user.exception.DuplicatePhoneNumberExepction;
import f3f.dev1.domain.user.exception.InvalidPasswordException;
import f3f.dev1.domain.user.exception.NotFoundByEmailException;
import f3f.dev1.domain.user.model.User;
import f3f.dev1.global.error.exception.NotFoundByIdException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static f3f.dev1.domain.user.dto.UserDTO.SignUpRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    ScrapRepository scrapRepository;

    // 주소 오브젝트 생성
    public Address createAddress() {
        return Address.builder()
                .addressName("address")
                .postalAddress("13556")
                .latitude("37.49455")
                .longitude("127.12170")
                .build();
    }

    // 회원가입 DTO 생성 메소드
    public SignUpRequest createSignUpRequest() {
        return SignUpRequest.builder()
                .userName("username")
                .nickname("nickname")
                .phoneNumber("01012345678")
                .email("userEmail@email.com")
                .address(createAddress())
                .password("password")
                .build();
    }

    // 로그인 DTO 생성 메소드
    public LoginRequest createLoginRequest() {
        return LoginRequest.builder()
                .email("userEmail@email.com")
                .password("password").build();
    }

    // 업데이트 DTO 생성 메소드
    public UpdateUserInfo createUpdateRequest(Long userId) {
        return UpdateUserInfo.builder()
                .id(userId)
                .address(createAddress())
                .nickname("newNickname")
                .build();
    }

    // 회원가입 테스트
    @Test
    @DisplayName("유저 생성 성공 테스트")
    public void signUpTestSuccess() throws Exception {
        //given
        SignUpRequest signUpRequest = createSignUpRequest();


        // when
        Long userId = userService.signUp(signUpRequest);
        Optional<User> byId = userRepository.findById(userId);
        // then
        assertThat(byId.get().getId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("유저 생성시 스크랩도 같이 생성되는지 확인 테스트")
    public void signUpTestWithScrap() throws Exception{
        //given
        SignUpRequest signUpRequest = createSignUpRequest();


        // when
        Long userId = userService.signUp(signUpRequest);
        Optional<Scrap> scrapByUserId = scrapRepository.findScrapByUserId(userId);

        // then
        assertThat(userId).isEqualTo(scrapByUserId.get().getUser().getId());
    }

    @Test
    @DisplayName("중복 이메일로 유저 생성 실패 테스트")
    public void signUpTestFailByEmail() throws Exception {
        //given
        SignUpRequest signUpRequest = createSignUpRequest();
        Long userId = userService.signUp(signUpRequest);


        // then
        SignUpRequest differentRequest = SignUpRequest.builder()
                .email("userEmail@email.com")
                .userName("differentUser")
                .nickname("differentUser")
                .phoneNumber("differentUser")
                .address(createAddress())
                .password("differentUser")
                .build();

        assertThrows(DuplicateEmailException.class, () -> userService.signUp(differentRequest));
    }

    @Test
    @DisplayName("중복 핸드폰 번호로 유저 생성 실패 테스트")
    public void signUpTestFailByPhoneNumber() throws Exception {
        //given
        SignUpRequest signUpRequest = createSignUpRequest();
        Long userId = userService.signUp(signUpRequest);

        // when
        SignUpRequest differentRequest = SignUpRequest.builder()
                .email("differentUser")
                .userName("differentUser")
                .nickname("differentUser")
                .phoneNumber("01012345678")
                .address(createAddress())
                .password("differentUser")
                .build();

        // then
        assertThrows(DuplicatePhoneNumberExepction.class, () -> userService.signUp(differentRequest));
    }

    // 로그인 테스트
    @Test
    @DisplayName("이메일, 비밀번호로 존재하는 유저 검증 성공 테스트")
    public void loginTestSuccess() throws Exception {
        //given
        SignUpRequest signUpRequest = createSignUpRequest();
        Long userId = userService.signUp(signUpRequest);


        // when
        LoginRequest loginRequest = createLoginRequest();


        // then
        assertThat(userId).isEqualTo(userService.login(loginRequest));
    }

    @Test
    @DisplayName("아이디 값(이메일)으로 존재하는 유저가 없어서 로그인 실패 테스트")
    public void loginTestFailByEmail() throws Exception {
        //given
        SignUpRequest signUpRequest = createSignUpRequest();
        Long userId = userService.signUp(signUpRequest);


        // when
        LoginRequest differentUser = LoginRequest.builder()
                .email("uniqueEmail")
                .password("password").build();
        // then
        assertThrows(NotFoundByEmailException.class, () -> userService.login(differentUser));
    }

    @Test
    @DisplayName("잘못된 비밀번호 값으로 로그인 실패 테스트")
    public void loginTestFailByPassword() throws Exception {
        //given
        SignUpRequest signUpRequest = createSignUpRequest();
        Long userId = userService.signUp(signUpRequest);

        // when
        LoginRequest differentUser = LoginRequest.builder()
                .email("userEmail@email.com")
                .password("wrongPassword").build();

        // then
        assertThrows(InvalidPasswordException.class, () -> userService.login(differentUser));
    }

    // 유저 정보 조회 테스트
    @Test
    @DisplayName("유저 정보 가져오는 테스트")
    public void getUserInfoTestSuccess() throws Exception {
        //given
        SignUpRequest signUpRequest = createSignUpRequest();
        Long userId = userService.signUp(signUpRequest);


        // when

        UserInfo userInfo = userService.getUserInfo(userId);

        // then
        assertArrayEquals(new String[]{
                signUpRequest.getUserName(),
                signUpRequest.getNickname(),
                signUpRequest.getEmail(),
                signUpRequest.getPhoneNumber()}, new String[]{
                userInfo.getUserName(),
                userInfo.getNickname(),
                userInfo.getEmail(),
                userInfo.getPhoneNumber()
        });
    }

    @Test
    @DisplayName("유저 정보 조회 실패 테스트 - 존재하지 않는 아이디로 요청")
    public void getUserInfoTestFailById() throws Exception{
        //given
        SignUpRequest signUpRequest = createSignUpRequest();
        Long userId = userService.signUp(signUpRequest);


        // then
        assertThrows(NotFoundByIdException.class, () -> userService.getUserInfo(userId + 1));
    }

    // 유저 정보 업데이트 테스트
    @Test
    @DisplayName("유저 닉네임 업데이트 성공 테스트")
    public void updateUserNicknameTestSuccess() throws Exception{
        //given
        SignUpRequest signUpRequest = createSignUpRequest();
        Long userId = userService.signUp(signUpRequest);
        UpdateUserInfo updateRequest = createUpdateRequest(userId);

        // when
        String s = userService.updateUserInfo(updateRequest);
        Optional<User> byId = userRepository.findById(userId);
        // then
        assertThat(updateRequest.getNickname()).isEqualTo(byId.get().getNickname());
    }

    @Test
    @DisplayName("유저 주소 업데이트 성공 테스트")
    public void updateUserAddressTestSuccess() throws Exception{
        //given
        SignUpRequest signUpRequest = createSignUpRequest();
        Long userId = userService.signUp(signUpRequest);
        UpdateUserInfo updateUserInfo = UpdateUserInfo.builder()
                .nickname("nickname")
                .address(Address.builder()
                        .addressName("home")
                        .postalAddress("13556")
                        .latitude("37.37125")
                        .longitude("127.10560").build())
                .id(userId)
                .build();


        // when
        String s = userService.updateUserInfo(updateUserInfo);
        Optional<User> byId = userRepository.findById(userId);
        // then
        assertThat(updateUserInfo.getAddress()).isEqualTo(byId.get().getAddress());
    }

    @Test
    @DisplayName("존재하지 않는 아이디의 유저 업데이트 실패 테스트")
    public void updateUserInfoTestFailById() throws Exception{
        //given
        SignUpRequest signUpRequest = createSignUpRequest();
        Long userId = userService.signUp(signUpRequest);
        UpdateUserInfo updateUserInfo = UpdateUserInfo.builder()
                .nickname("nickname")
                .address(Address.builder()
                        .addressName("home")
                        .postalAddress("13556")
                        .latitude("37.37125")
                        .longitude("127.10560").build())
                .id(userId+1)
                .build();


        // then
        assertThrows(NotFoundByIdException.class, () -> userService.updateUserInfo(updateUserInfo));
    }

    // 유저 삭제 테스트
    @Test
    @DisplayName("유저 삭제 성공 테스트")
    public void deleteUserTestSuccess() throws Exception{
        //given
        SignUpRequest signUpRequest = createSignUpRequest();
        Long userId = userService.signUp(signUpRequest);

        // when
        String s = userService.deleteUser(userId);
        // then
        assertThrows(NotFoundByIdException.class, () -> userService.getUserInfo(userId));
    }

    @Test
    @DisplayName("존재하지 않는 아이디로 삭제 실패 테스트")
    public void deleteUserTestFailById() throws Exception{
        //given
        SignUpRequest signUpRequest = createSignUpRequest();
        Long userId = userService.signUp(signUpRequest);

        // then
        assertThrows(NotFoundByIdException.class, () -> userService.deleteUser(userId + 1));
    }



}
