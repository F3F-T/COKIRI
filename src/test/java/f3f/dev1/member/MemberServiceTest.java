package f3f.dev1.member;

import f3f.dev1.domain.member.application.EmailCertificationService;
import f3f.dev1.domain.member.model.Member;
import f3f.dev1.domain.model.Address;
import f3f.dev1.domain.scrap.dao.ScrapRepository;
import f3f.dev1.domain.scrap.model.Scrap;
import f3f.dev1.domain.member.application.MemberService;
import f3f.dev1.domain.member.dao.MemberRepository;
import f3f.dev1.domain.member.dto.MemberDTO.LoginRequest;
import f3f.dev1.domain.member.dto.MemberDTO.UpdateUserInfo;
import f3f.dev1.domain.member.dto.MemberDTO.UserInfo;
import f3f.dev1.domain.member.exception.*;
import f3f.dev1.global.config.SHA256Encryptor;
import f3f.dev1.global.error.exception.NotFoundByIdException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static f3f.dev1.domain.member.dto.MemberDTO.SignUpRequest;
import static f3f.dev1.domain.member.dto.MemberDTO.UpdateUserPassword;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
public class MemberServiceTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;


    @Autowired
    EmailCertificationService emailCertificationService;

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
                .birthDate("990128")
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
    public UpdateUserInfo createUpdateRequest() {
        return UpdateUserInfo.builder()
                .address(createAddress())
                .nickname("newNickname")
                .phoneNumber("01088888888")
                .build();
    }

    // 회원가입 테스트
    @Test
    @DisplayName("유저 생성 성공 테스트")
    public void signUpTestSuccess() throws Exception {
        //given
        SignUpRequest signUpRequest = createSignUpRequest();


        // when
        Long userId = memberService.signUp(signUpRequest);
        Optional<Member> byId = memberRepository.findById(userId);
        // then
        assertThat(byId.get().getId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("유저 생성시 스크랩도 같이 생성되는지 확인 테스트")
    public void signUpTestWithScrap() throws Exception{
        //given
        SignUpRequest signUpRequest = createSignUpRequest();


        // when
        Long userId = memberService.signUp(signUpRequest);
        Optional<Scrap> scrapByUserId = scrapRepository.findScrapByUserId(userId);

        // then
        assertThat(userId).isEqualTo(scrapByUserId.get().getUser().getId());
    }

    @Test
    @DisplayName("중복 이메일로 유저 생성 실패 테스트")
    public void signUpTestFailByEmail() throws Exception {
        //given
        SignUpRequest signUpRequest = createSignUpRequest();
        Long userId = memberService.signUp(signUpRequest);


        // then
        SignUpRequest differentRequest = SignUpRequest.builder()
                .email("userEmail@email.com")
                .userName("differentUser")
                .nickname("differentUser")
                .phoneNumber("differentUser")
                .address(createAddress())
                .password("differentUser")
                .build();

        assertThrows(DuplicateEmailException.class, () -> memberService.signUp(differentRequest));
    }

    @Test
    @DisplayName("중복 핸드폰 번호로 유저 생성 실패 테스트")
    public void signUpTestFailByPhoneNumber() throws Exception {
        //given
        SignUpRequest signUpRequest = createSignUpRequest();
        Long userId = memberService.signUp(signUpRequest);

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
        assertThrows(DuplicatePhoneNumberExepction.class, () -> memberService.signUp(differentRequest));
    }

    // 중복 닉네임으로 유저 생성 실패 테스트
    @Test
    @DisplayName("중복 닉네임으로 유저 생성 실패 테스트")
    public void signUpTestFailByNickname() throws Exception{
        //given
        SignUpRequest signUpRequest = createSignUpRequest();
        Long userId = memberService.signUp(signUpRequest);

        // when
        SignUpRequest differentRequest = SignUpRequest.builder()
                .email("differentUser")
                .userName("differentUser")
                .nickname("nickname")
                .phoneNumber("01056781234")
                .address(createAddress())
                .password("differentUser")
                .build();
        // then
        assertThrows(DuplicateNicknameException.class, () -> memberService.signUp(differentRequest));
    }

//    // 로그인 테스트
//    @Test
//    @DisplayName("이메일, 비밀번호로 존재하는 유저 검증 성공 테스트")
//    public void loginTestSuccess() throws Exception {
//        //given
//        SignUpRequest signUpRequest = createSignUpRequest();
//        Long userId = memberService.signUp(signUpRequest);
//
//
//        // when
//        LoginRequest loginRequest = createLoginRequest();
//        Long loginUserId = sessionLoginService.login(loginRequest);
//
//
//        // then
//        assertThat(userId).isEqualTo(memberRepository.findByEmail(sessionLoginService.getLoginUser()).get().getId());
//    }
//
//    @Test
//    @DisplayName("아이디 값(이메일)으로 존재하는 유저가 없어서 로그인 실패 테스트")
//    public void loginTestFailByEmail() throws Exception {
//        //given
//        SignUpRequest signUpRequest = createSignUpRequest();
//        Long userId = memberService.signUp(signUpRequest);
//
//
//        // when
//        LoginRequest differentUser = LoginRequest.builder()
//                .email("uniqueEmail")
//                .password("password").build();
//        // then
//        assertThrows(UserNotFoundException.class, () -> sessionLoginService.login(differentUser));
//    }
//
//    @Test
//    @DisplayName("잘못된 비밀번호 값으로 로그인 실패 테스트")
//    public void loginTestFailByPassword() throws Exception {
//        //given
//        SignUpRequest signUpRequest = createSignUpRequest();
//        Long userId = memberService.signUp(signUpRequest);
//
//        // when
//        LoginRequest differentUser = LoginRequest.builder()
//                .email("userEmail@email.com")
//                .password("wrongPassword").build();
//
//        // then
//        assertThrows(UserNotFoundException.class, () -> sessionLoginService.login(differentUser));
//    }
//
//    // 유저 정보 조회 테스트
//    @Test
//    @DisplayName("유저 정보 가져오는 테스트")
//    public void getUserInfoTestSuccess() throws Exception {
//        //given
//        SignUpRequest signUpRequest = createSignUpRequest();
//        Long userId = memberService.signUp(signUpRequest);
//        LoginRequest loginRequest = createLoginRequest();
//
//        // when
//        sessionLoginService.login(loginRequest);
//        UserInfo userInfo = memberService.getUserInfo(memberRepository.findByEmail(sessionLoginService.getLoginUser()).get().getId());
//
//        // then
//        assertArrayEquals(new String[]{
//                signUpRequest.getUserName(),
//                signUpRequest.getNickname(),
//                signUpRequest.getEmail(),
//                signUpRequest.getPhoneNumber()}, new String[]{
//                userInfo.getUserName(),
//                userInfo.getNickname(),
//                userInfo.getEmail(),
//                userInfo.getPhoneNumber()
//        });
//    }
//
//    @Test
//    @DisplayName("유저 정보 조회 실패 테스트 - 존재하지 않는 아이디로 요청")
//    public void getUserInfoTestFailById() throws Exception{
//        //given
//        SignUpRequest signUpRequest = createSignUpRequest();
//        Long userId = memberService.signUp(signUpRequest);
//
//
//        // then
//        assertThrows(NotFoundByIdException.class, () -> memberService.getUserInfo(userId + 1));
//    }
//
//    // 유저 정보 업데이트 테스트
//    @Test
//    @DisplayName("유저 닉네임 업데이트 성공 테스트")
//    public void updateUserNicknameTestSuccess() throws Exception{
//        //given
//        SignUpRequest signUpRequest = createSignUpRequest();
//        Long userId = memberService.signUp(signUpRequest);
//        LoginRequest loginRequest = createLoginRequest();
//        sessionLoginService.login(loginRequest);
//        UpdateUserInfo updateRequest = createUpdateRequest();
//
//        // when
//        memberService.updateUserInfo(updateRequest);
//        Optional<Member> byId = memberRepository.findById(userId);
//        // then
//        assertThat(updateRequest.getNickname()).isEqualTo(byId.get().getNickname());
//    }
//
//    // 중복된 닉네임 변경 요청으로 실패 테스트
//    @Test
//    @DisplayName("중복된 닉네임으로 변경 실패 테스트")
//    public void updateUserNicknameTestFailByNickname() throws Exception{
//        //given
//        SignUpRequest signUpRequest = createSignUpRequest();
//        Long userId = memberService.signUp(signUpRequest);
//        LoginRequest loginRequest = createLoginRequest();
//        sessionLoginService.login(loginRequest);
//
//        // when
//        UpdateUserInfo updateRequest = UpdateUserInfo.builder()
//                .nickname("nickname")
//                .phoneNumber("01088888888").address(createAddress()).build();
//
//        // then
//        assertThrows(DuplicateNicknameException.class, () -> memberService.updateUserInfo(updateRequest));
//    }
//
//    @Test
//    @DisplayName("중복된 전화번호로 변경 실패 테스트")
//    public void updateUserPhoneNumberTestFailByPhoneNumber() throws Exception{
//        //given
//        SignUpRequest signUpRequest = createSignUpRequest();
//        Long userId = memberService.signUp(signUpRequest);
//        LoginRequest loginRequest = createLoginRequest();
//        sessionLoginService.login(loginRequest);
//
//        // when
//        UpdateUserInfo updateRequest = UpdateUserInfo.builder()
//                .nickname("newnickname")
//                .phoneNumber("01012345678").address(createAddress()).build();
//
//        // then
//        assertThrows(DuplicatePhoneNumberExepction.class, () -> memberService.updateUserInfo(updateRequest));
//    }
//
//    @Test
//    @DisplayName("유저 주소 업데이트 성공 테스트")
//    public void updateUserAddressTestSuccess() throws Exception{
//        //given
//        SignUpRequest signUpRequest = createSignUpRequest();
//        Long userId = memberService.signUp(signUpRequest);
//        LoginRequest loginRequest = createLoginRequest();
//        sessionLoginService.login(loginRequest);
//        UpdateUserInfo updateUserInfo = UpdateUserInfo.builder()
//                .nickname("newNickname")
//                .address(Address.builder()
//                        .addressName("home")
//                        .postalAddress("13556")
//                        .latitude("37.37125")
//                        .longitude("127.10560").build())
//                .build();
//
//
//        // when
//        memberService.updateUserInfo(updateUserInfo);
//        Optional<Member> byId = memberRepository.findById(userId);
//        // then
//        assertThat(updateUserInfo.getAddress()).isEqualTo(byId.get().getAddress());
//    }
//
//    @Test
//    @DisplayName("로그인하지 않는 아이디의 유저 업데이트 실패 테스트")
//    public void updateUserInfoTestFailById() throws Exception{
//        //given
//        SignUpRequest signUpRequest = createSignUpRequest();
//        Long userId = memberService.signUp(signUpRequest);
//        UpdateUserInfo updateUserInfo = UpdateUserInfo.builder()
//                .nickname("nickname")
//                .address(Address.builder()
//                        .addressName("home")
//                        .postalAddress("13556")
//                        .latitude("37.37125")
//                        .longitude("127.10560").build())
//                .build();
//
//
//        // then
//        assertThrows(UserNotFoundByEmailException.class, () -> memberService.updateUserInfo(updateUserInfo));
//    }
//
//    // 유저 비밀번호 변경 성공 테스트
//    @Test
//    @DisplayName("유저 비밀번호 변경 성공 테스트")
//    public void updateUserPasswordTestSuccess() throws Exception{
//        //given
//        SignUpRequest signUpRequest = createSignUpRequest();
//        Long userId = memberService.signUp(signUpRequest);
//        LoginRequest loginRequest = createLoginRequest();
//        sessionLoginService.login(loginRequest);
//        UpdateUserPassword updateUserPassword = UpdateUserPassword.builder()
//                .oldPassword("password")
//                .newPassword("newPassword")
//                .build();
//
//
//        // when
//        memberService.updateUserPassword(updateUserPassword);
//        SHA256Encryptor sha256Encryptor = new SHA256Encryptor();
//        // then
//        assertThat(sha256Encryptor.encrypt("newPassword")).isEqualTo(memberRepository.findById(userId).get().getPassword());
//    }
//
//    // 유저 비밀번호 변경 실패 테스트
//    @Test
//    @DisplayName("잘못된 과거 비밀번호 입력으로 비밀번호 변경 실패")
//    public void updateUserPasswordTestFailByOldPassword() throws Exception{
//        //given
//        SignUpRequest signUpRequest = createSignUpRequest();
//        Long userId = memberService.signUp(signUpRequest);
//        LoginRequest loginRequest = createLoginRequest();
//        sessionLoginService.login(loginRequest);
//        UpdateUserPassword updateUserPassword = UpdateUserPassword.builder()
//                .oldPassword("oldpassword")
//                .newPassword("newPassword")
//                .build();
//
//
//
//        // then
//        assertThrows(InvalidPasswordException.class, () -> memberService.updateUserPassword(updateUserPassword));
//    }
//
//    // 로그인하지 않은 요청으로 유저 비밀번호 변경 실패 테스트
//    @Test
//    @DisplayName("로그인 하지 않은 요청으로 유저 비밀번호 변경 실패")
//    public void updateUserPasswordTestFailByNonLogin() throws Exception{
//        //given
//        SignUpRequest signUpRequest = createSignUpRequest();
//        Long userId = memberService.signUp(signUpRequest);
//        // when
//        UpdateUserPassword updateUserPassword = UpdateUserPassword.builder()
//                .oldPassword("password")
//                .newPassword("newPassword")
//                .build();
//        // then
//        assertThrows(UserNotFoundByEmailException.class, () -> memberService.updateUserPassword(updateUserPassword));
//    }
//
//    // 유저 삭제 테스트
//    @Test
//    @DisplayName("유저 삭제 성공 테스트")
//    public void deleteUserTestSuccess() throws Exception{
//        //given
//        SignUpRequest signUpRequest = createSignUpRequest();
//        Long userId = memberService.signUp(signUpRequest);
//        LoginRequest loginRequest = createLoginRequest();
//        sessionLoginService.login(loginRequest);
//
//        // when
//        memberService.deleteUser();
//        // then
//        assertThrows(NotFoundByIdException.class, () -> memberService.getUserInfo(userId));
//    }
//
//    @Test
//    @DisplayName("로그인하지 않은 유저로 삭제 실패 테스트")
//    public void deleteUserTestFailById() throws Exception{
//        //given
//        SignUpRequest signUpRequest = createSignUpRequest();
//        Long userId = memberService.signUp(signUpRequest);
//
//
//        // then
//        assertThrows(UserNotFoundByEmailException.class, () -> memberService.deleteUser());
//    }



}
