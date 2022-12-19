package f3f.dev1.member;

import f3f.dev1.domain.member.application.AuthService;
import f3f.dev1.domain.member.application.EmailCertificationService;
import f3f.dev1.domain.member.model.Member;
import f3f.dev1.domain.member.model.UserLoginType;
import f3f.dev1.domain.model.Address;
import f3f.dev1.domain.scrap.dao.ScrapRepository;
import f3f.dev1.domain.scrap.model.Scrap;
import f3f.dev1.domain.member.application.MemberService;
import f3f.dev1.domain.member.dao.MemberRepository;
import f3f.dev1.domain.member.dto.MemberDTO.LoginRequest;
import f3f.dev1.domain.member.dto.MemberDTO.UpdateUserInfo;
import f3f.dev1.domain.member.dto.MemberDTO.UserInfo;
import f3f.dev1.domain.member.exception.*;
import f3f.dev1.global.error.exception.NotFoundByIdException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    AuthService authService;

    @Autowired
    PasswordEncoder passwordEncoder;


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
        authService.signUp(signUpRequest);
        Optional<Member> byId = memberRepository.findByEmail(signUpRequest.getEmail());
        // then
        assertThat(byId.get().getEmail()).isEqualTo(signUpRequest.getEmail());
    }

    @Test
    @DisplayName("유저 생성시 스크랩도 같이 생성되는지 확인 테스트")
    public void signUpTestWithScrap() throws Exception{
        //given
        SignUpRequest signUpRequest = createSignUpRequest();


        // when
        authService.signUp(signUpRequest);
        Member member = memberRepository.findByEmail(signUpRequest.getEmail()).get();
//        Optional<Scrap> scrapByUserId = scrapRepository.findScrapByUserId(member.getId());
//
//        // then
//        assertThat(member.getId()).isEqualTo(scrapByUserId.get().getUser().getId());
    }







    // 유저 정보 조회 테스트
    @Test
    @DisplayName("유저 정보 가져오는 테스트")
    public void getUserInfoTestSuccess() throws Exception {
        //given
        SignUpRequest signUpRequest = createSignUpRequest();
        authService.signUp(signUpRequest);
        LoginRequest loginRequest = createLoginRequest();

        // when
        UserInfo userInfo = memberService.getUserInfo(memberRepository.findByEmail(signUpRequest.getEmail()).get().getId());

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
        authService.signUp(signUpRequest);
        Member member = memberRepository.findByEmail(signUpRequest.getEmail()).get();


        // then
        assertThrows(NotFoundByIdException.class, () -> memberService.getUserInfo(member.getId() + 1));
    }

    // 유저 정보 업데이트 테스트
    @Test
    @DisplayName("유저 닉네임 업데이트 성공 테스트")
    public void updateUserNicknameTestSuccess() throws Exception{
        //given
        SignUpRequest signUpRequest = createSignUpRequest();
        authService.signUp(signUpRequest);
        Member member = memberRepository.findByEmail(signUpRequest.getEmail()).get();
        UpdateUserInfo updateRequest = createUpdateRequest();

        // when
        memberService.updateUserInfo(updateRequest, member.getId());
        Optional<Member> byId = memberRepository.findByEmail(signUpRequest.getEmail());
        // then
        assertThat(updateRequest.getNickname()).isEqualTo(byId.get().getNickname());
    }

    // 중복된 닉네임 변경 요청으로 실패 테스트
    @Test
    @DisplayName("중복된 닉네임으로 변경 실패 테스트")
    public void updateUserNicknameTestFailByNickname() throws Exception{
        //given
        SignUpRequest signUpRequest = createSignUpRequest();
        authService.signUp(signUpRequest);
        Member member = memberRepository.findByEmail(signUpRequest.getEmail()).get();
        SignUpRequest duplicateNickname = SignUpRequest.builder()
                .nickname("duplicateNickname")
                .address(createAddress())
                .userName("dupliateUser")
                .email("test@test.com")
                .password("12345678")
                .birthDate("999999")
                .userLoginType(UserLoginType.EMAIL)
                .phoneNumber("01012349999").build();
        authService.signUp(duplicateNickname);

        // when
        UpdateUserInfo updateRequest = UpdateUserInfo.builder()
                .nickname("duplicateNickname")
                .phoneNumber("01088888888").address(createAddress()).build();

        // then
        assertThrows(DuplicateNicknameException.class, () -> memberService.updateUserInfo(updateRequest, member.getId()));
    }

    @Test
    @DisplayName("중복된 전화번호로 변경 실패 테스트")
    public void updateUserPhoneNumberTestFailByPhoneNumber() throws Exception{
        //given
        SignUpRequest signUpRequest = createSignUpRequest();
        authService.signUp(signUpRequest);
        Member member = memberRepository.findByEmail(signUpRequest.getEmail()).get();
        SignUpRequest duplicatePhone = SignUpRequest.builder()
                .nickname("duplicateNickname")
                .address(createAddress())
                .userName("dupliateUser")
                .email("test@test.com")
                .password("12345678")
                .birthDate("999999")
                .userLoginType(UserLoginType.EMAIL)
                .phoneNumber("01012349999").build();
        authService.signUp(duplicatePhone);

        // when
        UpdateUserInfo updateRequest = UpdateUserInfo.builder()
                .nickname("newnickname")
                .phoneNumber("01012349999").address(createAddress()).build();

        // then
        assertThrows(DuplicatePhoneNumberExepction.class, () -> memberService.updateUserInfo(updateRequest, member.getId()));
    }

    @Test
    @DisplayName("유저 주소 업데이트 성공 테스트")
    public void updateUserAddressTestSuccess() throws Exception{
        //given
        SignUpRequest signUpRequest = createSignUpRequest();
        authService.signUp(signUpRequest);
        Member member = memberRepository.findByEmail(signUpRequest.getEmail()).get();
        UpdateUserInfo updateUserInfo = UpdateUserInfo.builder()
                .nickname("newNickname")
                .address(Address.builder()
                        .addressName("home")
                        .postalAddress("13556")
                        .latitude("37.37125")
                        .longitude("127.10560").build())
                .build();


        // when
        memberService.updateUserInfo(updateUserInfo, member.getId());
        Optional<Member> byId = memberRepository.findByEmail(signUpRequest.getEmail());
        // then
        assertThat(updateUserInfo.getAddress()).isEqualTo(byId.get().getAddress());
    }



    // 유저 비밀번호 변경 성공 테스트
    @Test
    @DisplayName("유저 비밀번호 변경 성공 테스트")
    public void updateUserPasswordTestSuccess() throws Exception{
        //given
        SignUpRequest signUpRequest = createSignUpRequest();
        authService.signUp(signUpRequest);
        Member member = memberRepository.findByEmail(signUpRequest.getEmail()).get();
        LoginRequest loginRequest = createLoginRequest();
        UpdateUserPassword updateUserPassword = UpdateUserPassword.builder()
                .oldPassword("password")
                .newPassword("newPassword")
                .build();


        // when
        memberService.updateUserPassword(updateUserPassword, member.getId());
        // then

    }

    // 유저 비밀번호 변경 실패 테스트
    @Test
    @DisplayName("잘못된 과거 비밀번호 입력으로 비밀번호 변경 실패")
    public void updateUserPasswordTestFailByOldPassword() throws Exception{
        //given
        SignUpRequest signUpRequest = createSignUpRequest();
        authService.signUp(signUpRequest);
        Member member = memberRepository.findByEmail(signUpRequest.getEmail()).get();
        UpdateUserPassword updateUserPassword = UpdateUserPassword.builder()
                .oldPassword("oldpassword")
                .newPassword("newPassword")
                .build();



        // then
        assertThrows(InvalidPasswordException.class, () -> memberService.updateUserPassword(updateUserPassword, member.getId()));
    }



    // 유저 삭제 테스트
    @Test
    @DisplayName("유저 삭제 성공 테스트")
    public void deleteUserTestSuccess() throws Exception{
        //given
        SignUpRequest signUpRequest = createSignUpRequest();
        authService.signUp(signUpRequest);
        Member member = memberRepository.findByEmail(signUpRequest.getEmail()).get();
        Long userId = member.getId();

        // when
        memberService.deleteUser(userId);
        // then
        assertThrows(NotFoundByIdException.class, () -> memberService.getUserInfo(userId));
    }




}
