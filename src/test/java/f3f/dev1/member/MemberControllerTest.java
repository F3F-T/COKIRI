package f3f.dev1.member;


import com.fasterxml.jackson.databind.ObjectMapper;
import f3f.dev1.domain.member.application.EmailCertificationService;
import f3f.dev1.domain.model.Address;
import f3f.dev1.domain.member.api.MemberController;
import f3f.dev1.domain.member.application.SessionLoginService;
import f3f.dev1.domain.member.application.MemberService;
import f3f.dev1.domain.member.dao.MemberRepository;
import f3f.dev1.domain.member.exception.DuplicateEmailException;
import f3f.dev1.domain.member.exception.DuplicateNicknameException;
import f3f.dev1.domain.member.exception.DuplicatePhoneNumberExepction;
import f3f.dev1.domain.member.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static f3f.dev1.domain.member.dto.MemberDTO.*;
import static f3f.dev1.domain.member.model.UserLoginType.EMAIL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

@WebMvcTest(MemberController.class)
@MockBean(JpaMetamodelMappingContext.class)
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class MemberControllerTest {
    @MockBean
    private MemberService memberService;
    @MockBean
    private SessionLoginService sessionLoginService;

    @MockBean
    private EmailCertificationService emailCertificationService;

    @MockBean
    private MemberRepository memberRepository;

    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    public void setup(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentationContextProvider) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(sharedHttpSession())
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .build();
    }

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
                .userLoginType(EMAIL)
                .build();
    }

    // 로그인 DTO 생성 메소드
    public LoginRequest createLoginRequest() {
        return LoginRequest.builder()
                .email("userEmail@email.com")
                .password("password")
                .build();
    }

    // 업데이트 DTO 생성 메소드
    public UpdateUserInfo createUpdateRequest() {
        return UpdateUserInfo.builder()
                .address(createAddress())
                .nickname("newNickname")
                .build();
    }

    // 이메일 찾기 DTO 생성 메소드
    public FindEmailDto createFindEmailDto() {
        return FindEmailDto.builder()
                .userName("username")
                .phoneNumber("01012345678").build();
    }
    // 비밀번호 찾기 DTO 생성 메소드
    public FindPasswordDto createFindPasswordDto() {
        return FindPasswordDto.builder()
                .userName("username")
                .phoneNumber("01012345678")
                .email("userEmail@email.com").build();
    }

    @Test
    @DisplayName("회원 가입 성공 테스트")
    public void signUpTestSuccess() throws Exception{
        //given
        SignUpRequest signUpRequest = createSignUpRequest();

        // then
        mockMvc.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document("user/signup/successful", requestFields(
                        fieldWithPath("userName").description("The user's name"),
                        fieldWithPath("address").description("The user's address class"),
                        fieldWithPath("address.addressName").description("The user's address name"),
                        fieldWithPath("address.postalAddress").description("The user's postal address"),
                        fieldWithPath("address.latitude").description("latitude of user address"),
                        fieldWithPath("address.longitude").description("longitude of user address"),
                        fieldWithPath("nickname").description("The user's nickname"),
                        fieldWithPath("phoneNumber").description("The user's phoneNumber"),
                        fieldWithPath("email").description("The user's email"),
                        fieldWithPath("password").description("The user's password"),
                        fieldWithPath("birthDate").description("The user's birthDate"),
                        fieldWithPath("userLoginType").description("The user's loginType")
                )));


    }


    @Test
    @DisplayName("이메일 중복으로 인한 회원가입 실패 테스트")
    public void signUpTestFailByEmail() throws Exception{
        //given

        SignUpRequest differentUser = createSignUpRequest();

        // when

        doThrow(new DuplicateEmailException()).when(memberService).signUp(any());

        // then
        mockMvc.perform(post("/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(differentUser)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andDo(document("user/signup/fail/duplicateEmail", requestFields(
                        fieldWithPath("userName").description("The user's name"),
                        fieldWithPath("address").description("The user's address class"),
                        fieldWithPath("address.addressName").description("The user's address name"),
                        fieldWithPath("address.postalAddress").description("The user's postal address"),
                        fieldWithPath("address.latitude").description("latitude of user address"),
                        fieldWithPath("address.longitude").description("longitude of user address"),
                        fieldWithPath("nickname").description("The user's nickname"),
                        fieldWithPath("phoneNumber").description("The user's phoneNumber"),
                        fieldWithPath("email").description("The user's email, which already in server"),
                        fieldWithPath("password").description("The user's password"),
                        fieldWithPath("birthDate").description("The user's birthDate"),
                        fieldWithPath("userLoginType").description("The user's loginType")
                )));
    }

    @Test
    @DisplayName("닉네임 중복으로 인한 회원가입 실패")
    public void signUpTestFailByNickname() throws Exception{
        //given
        SignUpRequest differentUser = createSignUpRequest();

        // when
        doThrow(new DuplicateNicknameException()).when(memberService).signUp(any());

        // then
        mockMvc.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(differentUser)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andDo(document("user/signup/fail/duplicateNickname",requestFields(
                        fieldWithPath("userName").description("The user's name"),
                        fieldWithPath("address").description("The user's address class"),
                        fieldWithPath("address.addressName").description("The user's address name"),
                        fieldWithPath("address.postalAddress").description("The user's postal address"),
                        fieldWithPath("address.latitude").description("latitude of user address"),
                        fieldWithPath("address.longitude").description("longitude of user address"),
                        fieldWithPath("nickname").description("The user's nickname, nickname already in server."),
                        fieldWithPath("phoneNumber").description("The user's phoneNumber"),
                        fieldWithPath("email").description("The user's email"),
                        fieldWithPath("password").description("The user's password"),
                        fieldWithPath("birthDate").description("The user's birthDate"),
                        fieldWithPath("userLoginType").description("The user's loginType")
                )));
    }

    @Test
    @DisplayName("중복된 전화번호로 회원가입 실패 테스트")
    public void signUpTestFailByPhoneNumber() throws Exception{
        //given
        SignUpRequest differentUser = createSignUpRequest();

        // when
        doThrow(new DuplicatePhoneNumberExepction()).when(memberService).signUp(any());

        // then
        mockMvc.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(differentUser)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andDo(document("user/signup/fail/duplicatePhoneNumber", requestFields(
                        fieldWithPath("userName").description("The user's name"),
                        fieldWithPath("address").description("The user's address class"),
                        fieldWithPath("address.addressName").description("The user's address name"),
                        fieldWithPath("address.postalAddress").description("The user's postal address"),
                        fieldWithPath("address.latitude").description("latitude of user address"),
                        fieldWithPath("address.longitude").description("longitude of user address"),
                        fieldWithPath("nickname").description("The user's nickname"),
                        fieldWithPath("phoneNumber").description("The user's phoneNumber, which is already in server"),
                        fieldWithPath("email").description("The user's email"),
                        fieldWithPath("password").description("The user's password"),
                        fieldWithPath("birthDate").description("The user's birthDate"),
                        fieldWithPath("userLoginType").description("The user's loginType")
                )));
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    public void loginTestSuccess() throws Exception{
        //given
        LoginRequest loginRequest = createLoginRequest();
        // then
        mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user/login/success", requestFields(
                        fieldWithPath("email").description("User's id for login which is email"),
                        fieldWithPath("password").description("User's login password"))
                ));
    }

    @Test
    @DisplayName("로그인 실패 테스트 - 잘못된 이메일")
    public void loginTestFailByEmail() throws Exception{
        //given
        LoginRequest loginRequest = createLoginRequest();

        // when
        doThrow(new UserNotFoundException()).when(sessionLoginService).login(any());

        // then
        mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andDo(document("user/login/fail/wrongEmail", requestFields(
                        fieldWithPath("email").description("Wrong email for login"),
                        fieldWithPath("password").description("login password")
                )));

    }

    // TODO: 컨트롤러 테스트 추가하고, trade 테스트 작성하기
    @Test
    @DisplayName("로그인 실패 테스트 - 잘못된 비밀번호")
    public void loginTestFailByPassword() throws Exception{
        //given
        LoginRequest loginRequest = createLoginRequest();

        // when
        doThrow(new UserNotFoundException()).when(sessionLoginService).login(any());

        // then
        mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andDo(document("user/login/fail/wrongPassword", requestFields(
                        fieldWithPath("email").description("Correct user email"),
                        fieldWithPath("password").description("Wrong password")
                )));
    }

    @Test
    @DisplayName("유저 정보 업데이트 성공 테스트")
    public void updateUserInfoTestSuccess() throws Exception{
        // given
        UpdateUserInfo updateRequest = createUpdateRequest();
        // when

        given(sessionLoginService.getLoginUser()).willReturn(createSignUpRequest().getEmail());
        // then
        mockMvc.perform(patch("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user/update/success",requestFields(
                        fieldWithPath("address").description("The user's address class"),
                        fieldWithPath("address.addressName").description("The user's address name"),
                        fieldWithPath("address.postalAddress").description("The user's postal address"),
                        fieldWithPath("address.latitude").description("latitude of user address"),
                        fieldWithPath("address.longitude").description("longitude of user address"),
                        fieldWithPath("nickname").description("The user's nickname")
                )));
    }

    @Test
    @DisplayName("로그인 안한 상태에서 유저 업데이트 실패 테스트")
    public void updateUserInfoTestFailByLogin() throws Exception{
        //given
        UpdateUserInfo updateRequest = createUpdateRequest();

        // then
        mockMvc.perform(patch("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andDo(document("user/update/fail/non-login", requestFields(
                        fieldWithPath("address").description("The user's address class"),
                        fieldWithPath("address.addressName").description("The user's address name"),
                        fieldWithPath("address.postalAddress").description("The user's postal address"),
                        fieldWithPath("address.latitude").description("latitude of user address"),
                        fieldWithPath("address.longitude").description("longitude of user address"),
                        fieldWithPath("nickname").description("The user's nickname")
                )));

    }

    @Test
    @DisplayName("유저 비밀번호 변경 성공 테스트")
    public void updateUserPasswordTestSuccess() throws Exception{
        //given
        UpdateUserPassword updateUserPassword = UpdateUserPassword.builder()
                .oldPassword("password")
                .newPassword("12345678")
                .build();

        // when

        given(sessionLoginService.getLoginUser()).willReturn(createSignUpRequest().getEmail());


        // then
        mockMvc.perform(patch("/user/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserPassword))
                ).andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user/update-password/success", requestFields(
                        fieldWithPath("oldPassword").description("Previous password"),
                        fieldWithPath("newPassword").description("New password")
                )));

    }

    @Test
    @DisplayName("로그인 하지 않은 상태에서 비밀번호 변경 실패 테스트")
    public void updateUserPasswordTestFailByLogin() throws Exception{
        //given
        UpdateUserPassword updateUserPassword = UpdateUserPassword.builder()
                .oldPassword("password")
                .newPassword("12345678")
                .build();

        // then
        mockMvc.perform(patch("/user/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserPassword))
                ).andDo(print())
                .andExpect(status().isUnauthorized())
                .andDo(document("user/update-password/fail/non-login", requestFields(
                        fieldWithPath("oldPassword").description("Previous password"),
                        fieldWithPath("newPassword").description("New password")
                )));
    }

    @Test
    @DisplayName("회원 삭제 요청 성공 테스트")
    public void deleteUserTestSuccess() throws Exception{
        //given
        given(sessionLoginService.getLoginUser()).willReturn(createSignUpRequest().getEmail());

        // then
        mockMvc.perform(delete("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString("")))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user/delete-user/success"));
    }

    @Test
    @DisplayName("이메일 찾기 성공 테스트")
    public void findEmailTestSuccess() throws Exception{
        //given
        given(memberRepository.findByUserNameAndPhoneNumber(any(), any())).willReturn(Optional.ofNullable(createSignUpRequest().toEntity()));

        // then
        mockMvc.perform(post("/user/find/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createFindEmailDto())))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user/find-email/success", requestFields(
                        fieldWithPath("userName").description("The name of user"),
                        fieldWithPath("phoneNumber").description("The phoneNumber of user")
                )));
    }

    @Test
    @DisplayName("존재하지 않는 유저로 인한 이메일 찾기 실패 테스트")
    public void findEmailTestFailByNullUser() throws Exception{
        // given
        doThrow(UserNotFoundException.class).when(memberService).findUserEmail(any());

        // then
        mockMvc.perform(post("/user/find/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createFindEmailDto())))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andDo(document("user/find-email/fail/no-user", requestFields(
                        fieldWithPath("userName").description("The name of user that doesn't exists or doesn't match with phoneNumber"),
                        fieldWithPath("phoneNumber").description("The phoneNumber of user that doesn't exists or doesn't match with username")
                )));
    }

    @Test
    @DisplayName("비밀번호 찾기 성공 테스트")
    public void findPasswordTestSuccess() throws Exception{
        //given
        given(memberRepository.findByUserNameAndPhoneNumberAndEmail(any(), any(), any())).willReturn(Optional.ofNullable(createSignUpRequest().toEntity()));

        // then
        mockMvc.perform(post("/user/find/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createFindPasswordDto())))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user/find-password/success", requestFields(
                        fieldWithPath("userName").description("The name of user"),
                        fieldWithPath("phoneNumber").description("The phone number of user"),
                        fieldWithPath("email").description("The email of user")
                )));
    }

    @Test
    @DisplayName("존재하지 않는 유저로 비밀번호 찾기 실패 테스트")
    public void findPasswordTestFailByUser() throws Exception{
        //given
        doThrow(UserNotFoundException.class).when(memberService).findUserPassword(any());

        // then
        mockMvc.perform(post("/user/find/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createFindPasswordDto())))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andDo(document("user/find-password/fail/no-user", requestFields(
                        fieldWithPath("userName").description("The name of user that doesn't exists or match with email and phoneNumber"),
                        fieldWithPath("phoneNumber").description("The phone number of user that doesn't exists or match with userName and email"),
                        fieldWithPath("email").description("The email of user that doesn't exists or match with userName and phoneNumber")
                )));
    }
}
