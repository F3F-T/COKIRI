package f3f.dev1.member;

import com.amazonaws.services.s3.AmazonS3Client;
import com.fasterxml.jackson.databind.ObjectMapper;
import f3f.dev1.domain.member.api.MemberAuthController;
import f3f.dev1.domain.member.api.MemberController;
import f3f.dev1.domain.member.application.AuthService;
import f3f.dev1.domain.member.application.EmailCertificationService;
import f3f.dev1.domain.member.application.MemberService;
import f3f.dev1.domain.member.application.OAuth2UserService;
import f3f.dev1.domain.member.dao.MemberRepository;
import f3f.dev1.domain.member.exception.DuplicateNicknameException;
import f3f.dev1.domain.member.exception.DuplicatePhoneNumberExepction;
import f3f.dev1.domain.member.exception.InvalidPasswordException;
import f3f.dev1.domain.member.exception.UserNotFoundException;
import f3f.dev1.domain.address.model.Address;
import f3f.dev1.domain.member.model.UserLoginType;
import f3f.dev1.domain.token.dto.TokenDTO;
import f3f.dev1.global.common.annotation.WithMockCustomUser;
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

import java.util.ArrayList;
import java.util.Optional;

import static f3f.dev1.domain.member.dto.MemberDTO.*;
import static f3f.dev1.domain.member.model.UserLoginType.EMAIL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

@WebMvcTest({MemberController.class, MemberAuthController.class})
@MockBean(JpaMetamodelMappingContext.class)
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class MemberControllerTest {
    @MockBean
    private MemberService memberService;

    @MockBean
    private OAuth2UserService oAuth2UserService;

    @MockBean
    private EmailCertificationService emailCertificationService;

    @MockBean
    private AuthService authService;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private AmazonS3Client amazonS3Client;
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
                .email("userTest@email.com")
                .birthDate("990128")
                .password("password")
                .userLoginType(EMAIL)
                .build();
    }

    // 로그인 DTO 생성 메소드
    public LoginRequest createLoginRequest() {
        return LoginRequest.builder()
                .email("userTest@email.com")
                .password("12345678")
                .build();
    }

    // 업데이트 DTO 생성 메소드
    public UpdateUserInfo createUpdateRequest() {
        return UpdateUserInfo.builder()
                .address(createAddress())
                .nickname("newNickname")
                .phoneNumber("01088888888")
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
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document("auth/signup/successful", requestFields(
                        fieldWithPath("userName").description("The user's name"),
                        fieldWithPath("nickname").description("The user's nickname"),
                        fieldWithPath("phoneNumber").description("The user's phoneNumber"),
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
//        SignUpRequest signUpRequest = createSignUpRequest();
//        authService.signUp(signUpRequest);
        LoginRequest loginRequest = createLoginRequest();
        UserLoginDto userLoginDto = UserLoginDto.builder()
                .userInfo(UserInfoWithAddress.builder()
                        .userDetail(UserDetail.builder()
                                .userName("username")
                                .scrapId(1L)
                                .loginType(EMAIL)
                                .nickname("nickname")
                                .imageUrl("imageUrl")
                                .description("description")
                                .phoneNumber("01012341234")
                                .birthDate("990101")
                                .build())
                        .address(new ArrayList<>())
                        .build())
                .tokenInfo(TokenDTO.TokenIssueDTO.builder()
                        .accessToken("accessToken")
                        .accessTokenExpiresIn(1L)
                        .grantType("type").build()).build();
        when(authService.login(any())).thenReturn(userLoginDto);
        // then
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("auth/login/successful", requestFields(
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
        doThrow(new UserNotFoundException()).when(authService).login(any());

        // then
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andDo(document("user/login/fail/wrongEmail", requestFields(
                        fieldWithPath("email").description("Wrong email for login"),
                        fieldWithPath("password").description("login password")
                )));

    }


    @Test
    @DisplayName("로그인 실패 테스트 - 잘못된 비밀번호")
    public void loginTestFailByPassword() throws Exception{
        //given
        LoginRequest loginRequest = createLoginRequest();

        // when
        doThrow(new UserNotFoundException()).when(authService).login(any());

        // then
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andDo(document("auth/login/fail/wrongPassword", requestFields(
                        fieldWithPath("email").description("Correct user email"),
                        fieldWithPath("password").description("Wrong password")
                )));
    }

    @Test
    @DisplayName("회원 정보 조회 성공 테스트")
    @WithMockCustomUser
    public void getUserInfoTestSuccess() throws Exception{
        //given
        UserInfoWithAddress userInfo = UserInfoWithAddress.builder()
                .userDetail(UserDetail.builder().id(1L).userName("userName").birthDate("990101").description("description").email("email").imageUrl("imageUrl").nickname("phoneNumber").loginType(EMAIL).scrapId(1L).build())
                .address(new ArrayList<>()).build();


        // when
        doReturn(userInfo).when(memberService).getUserInfo(any());
        // then
        mockMvc.perform(get("/user"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user/get-info/success", responseFields(
                        fieldWithPath("userDetail.id").description("Id of user"),
                        fieldWithPath("userDetail.loginType").description("login type of user"),
                        fieldWithPath("userDetail.userName").description("name of user"),
                        fieldWithPath("userDetail.scrapId").description("Id of scrap"),
                        fieldWithPath("userDetail.email").description("email of user"),
                        fieldWithPath("address").description("The user's address class"),
                        fieldWithPath("userDetail.nickname").description("The user's nickname"),
                        fieldWithPath("userDetail.phoneNumber").description("The user's phoneNumber"),
                        fieldWithPath("userDetail.imageUrl").description("The user's image url"),
                        fieldWithPath("userDetail.description").description("user description"),
                        fieldWithPath("userDetail.birthDate").description("The user's birthdate")

                )));
    }

    @Test
    @DisplayName("로그인하지 않아서 유저 정보 조회 실패 테스트")
    public void getUserInfoTestFailByNonLogin() throws Exception{
        //given
        UserInfo userInfo = UserInfo.builder()
                .id(1L)
                .loginType(EMAIL)
                .scrapId(2L)
                .userName("userName")
                .email("email")
                .nickname("nickname")
                .phoneNumber("01012345678").build();

        // then
        mockMvc.perform(get("/user"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andDo(document("user/get-info/fail/non-login", responseFields(
                        fieldWithPath("status").description("status of http response"),
                        fieldWithPath("message").description("description of error message")
                )));
    }

    @Test
    @DisplayName("유저 정보 업데이트 성공 테스트")
    @WithMockCustomUser
    public void updateUserInfoTestSuccess() throws Exception{
        // given
        SignUpRequest signUpRequest = createSignUpRequest();
        authService.signUp(signUpRequest);
        UpdateUserInfo updateRequest = createUpdateRequest();
        // when
        given(memberRepository.findById(any())).willReturn(Optional.ofNullable(signUpRequest.toEntity()));
        // then
        mockMvc.perform(patch("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user/update/success",requestFields(
                        fieldWithPath("userId").description("id value of user"),
                        fieldWithPath("address.id").description("id value of address"),
                        fieldWithPath("address.member").description("member of address"),
                        fieldWithPath("address").description("The user's address class"),
                        fieldWithPath("address.addressName").description("The user's address name"),
                        fieldWithPath("address.postalAddress").description("The user's postal address"),
                        fieldWithPath("address.latitude").description("latitude of user address"),
                        fieldWithPath("address.longitude").description("longitude of user address"),
                        fieldWithPath("nickname").description("The user's nickname"),
                        fieldWithPath("phoneNumber").description("The user's phoneNumber")
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
                .andExpect(status().isBadRequest())
                .andDo(document("user/update/fail/non-login", requestFields(
                        fieldWithPath("userId").description("id value of user"),
                        fieldWithPath("address.id").description("id value of address"),
                        fieldWithPath("address.member").description("member value of address"),
                        fieldWithPath("address").description("The user's address class"),
                        fieldWithPath("address.addressName").description("The user's address name"),
                        fieldWithPath("address.postalAddress").description("The user's postal address"),
                        fieldWithPath("address.latitude").description("latitude of user address"),
                        fieldWithPath("address.longitude").description("longitude of user address"),
                        fieldWithPath("nickname").description("The user's nickname"),
                        fieldWithPath("phoneNumber").description("The user's phoneNumber")
                )));

    }

    @Test
    @DisplayName("중복된 닉네임으로 유저 정보 변경 실패 테스트")
    @WithMockCustomUser
    public void updateUserInfoTestFailByDuplicateNickname() throws Exception{
        //given
        SignUpRequest signUpRequest = createSignUpRequest();
        authService.signUp(signUpRequest);
        UpdateUserInfo updateRequest = createUpdateRequest();


        // when
        doThrow(DuplicateNicknameException.class).when(memberService).updateUserInfo(any(), any());
        // then
        mockMvc.perform(patch("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andDo(document("user/update/fail/duplicate-nickname", requestFields(
                        fieldWithPath("userId").description("id value of user"),
                        fieldWithPath("address.id").description("id value of address"),
                        fieldWithPath("address.member").description("member of address"),
                        fieldWithPath("address").description("The user's address class"),
                        fieldWithPath("address.addressName").description("The user's address name"),
                        fieldWithPath("address.postalAddress").description("The user's postal address"),
                        fieldWithPath("address.latitude").description("latitude of user address"),
                        fieldWithPath("address.longitude").description("longitude of user address"),
                        fieldWithPath("nickname").description("The user's nickname that is duplicate"),
                        fieldWithPath("phoneNumber").description("The user's phoneNumber")
                )));
    }

    @Test
    @DisplayName("중복된 전화번호로 유저 정보 업데이트 실패 테스트")
    @WithMockCustomUser
    public void updateUserInfoTestFailByDuplicatePhone() throws Exception{
        //given
        SignUpRequest signUpRequest = createSignUpRequest();
        authService.signUp(signUpRequest);
        UpdateUserInfo updateRequest = createUpdateRequest();

        // when
        doThrow(DuplicatePhoneNumberExepction.class).when(memberService).updateUserInfo(any(), any());

        // then
        mockMvc.perform(patch("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andDo(document("user/update/fail/duplicate-nickname", requestFields(
                        fieldWithPath("userId").description("id value of user"),
                        fieldWithPath("address").description("The user's address class"),
                        fieldWithPath("address.id").description("id value of address"),
                        fieldWithPath("address.member").description("member of address"),
                        fieldWithPath("address.addressName").description("The user's address name"),
                        fieldWithPath("address.postalAddress").description("The user's postal address"),
                        fieldWithPath("address.latitude").description("latitude of user address"),
                        fieldWithPath("address.longitude").description("longitude of user address"),
                        fieldWithPath("nickname").description("The user's nickname"),
                        fieldWithPath("phoneNumber").description("The user's phoneNumber that is duplicate")
                )));
    }

    @Test
    @DisplayName("유저 비밀번호 변경 성공 테스트")
    @WithMockCustomUser
    public void updateUserPasswordTestSuccess() throws Exception{
        //given
        SignUpRequest signUpRequest = createSignUpRequest();
        authService.signUp(signUpRequest);

        // when

        UpdateUserPassword updateUserPassword = UpdateUserPassword.builder()
                .oldPassword("password")
                .newPassword("12345678")
                .build();

        // then
        mockMvc.perform(patch("/user/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserPassword))
                ).andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user/update-password/success", requestFields(
                        fieldWithPath("userId").description("id value of user"),
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
                .andExpect(status().isBadRequest())
                .andDo(document("user/update-password/fail/non-login", requestFields(
                        fieldWithPath("userId").description("id value of user"),
                        fieldWithPath("oldPassword").description("Previous password"),
                        fieldWithPath("newPassword").description("New password")
                )));
    }

    @Test
    @DisplayName("과거에 틀린 비밀번호 입력으로 비밀번호 변경 실패 테스트")
    @WithMockCustomUser
    public void updateUserPasswordTestFailByWrongOldPassword() throws Exception{
        //given
        SignUpRequest signUpRequest = createSignUpRequest();
        authService.signUp(signUpRequest);
        UpdateUserPassword updateUserPassword = UpdateUserPassword.builder()
                .oldPassword("password")
                .newPassword("12345678")
                .build();

        // when
        doThrow(InvalidPasswordException.class).when(memberService).updateUserPassword(any(), any());

        // then
        mockMvc.perform(patch("/user/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserPassword)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andDo(document("user/update-password/fail/wrong-password", requestFields(
                        fieldWithPath("userId").description("id value of user"),
                        fieldWithPath("oldPassword").description("Previous password that is wrong"),
                        fieldWithPath("newPassword").description("New password")
                )));
    }

    @Test
    @DisplayName("회원 삭제 요청 성공 테스트")
    @WithMockCustomUser
    public void deleteUserTestSuccess() throws Exception{
        //given
        SignUpRequest signUpRequest = createSignUpRequest();
        authService.signUp(signUpRequest);

        // then
        mockMvc.perform(delete("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString("")))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user/delete-user/success"));
    }

    @Test
    @DisplayName("로그인 하지 않은 상태에서 회원 삭제 요청 실패 테스트")
    public void deleteUserTestFailByNonLogin() throws Exception{
        //given


        // then
        mockMvc.perform(delete("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString("")))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andDo(document("user/delete-user/fail"));
    }

    @Test
    @DisplayName("이메일 찾기 성공 테스트")
    public void findEmailTestSuccess() throws Exception{
        //given
        given(memberRepository.findByUserNameAndPhoneNumber(any(), any())).willReturn(Optional.ofNullable(createSignUpRequest().toEntity()));

        // then
        mockMvc.perform(post("/auth/find/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createFindEmailDto())))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("auth/find-email/success", requestFields(
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
        mockMvc.perform(post("/auth/find/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createFindEmailDto())))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andDo(document("auth/find-email/fail/no-user", requestFields(
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
        mockMvc.perform(post("/auth/find/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createFindPasswordDto())))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("auth/find-password/success", requestFields(
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
        mockMvc.perform(post("/auth/find/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createFindPasswordDto())))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andDo(document("auth/find-password/fail/no-user", requestFields(
                        fieldWithPath("userName").description("The name of user that doesn't exists or match with email and phoneNumber"),
                        fieldWithPath("phoneNumber").description("The phone number of user that doesn't exists or match with userName and email"),
                        fieldWithPath("email").description("The email of user that doesn't exists or match with userName and phoneNumber")
                )));
    }

    @Test
    @DisplayName("이메일 중복 확인 성공 테스트 - true 리턴")
    public void checkDuplicateEmailTestReturnTrue() throws Exception{
        //given
        CheckEmailDto build = CheckEmailDto.builder().email("test@email.com").build();


        // when
        doReturn(new RedunCheckDto(true)).when(memberService).existsByEmail(any());
        // then
        mockMvc.perform(post("/auth/check-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(build)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("auth/check-email/success", requestFields(
                        fieldWithPath("email").description("email to check redundance")
                ), responseFields(
                        fieldWithPath("exists").description("boolean value of existence")
                )));

    }

    @Test
    @DisplayName("이메일 중복 확인 실패 테스트 - false 리턴")
    public void checkDuplicateEmailTestReturnFalse() throws Exception{
        //given
        CheckEmailDto build = CheckEmailDto.builder().email("test@email.com").build();


        // when
        doReturn(new RedunCheckDto(false)).when(memberService).existsByEmail(any());
        // then
        mockMvc.perform(post("/auth/check-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(build)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("auth/check-email/fail", requestFields(
                        fieldWithPath("email").description("email to check redundance")
                ), responseFields(
                        fieldWithPath("exists").description("boolean value of existence")
                )));
    }

    @Test
    @DisplayName("전화번호 중복 확인 성공 테스트 - true 리턴")
    public void checkDuplicatePhoneTestReturnTrue() throws Exception{
        //given
        CheckPhoneNumberDto checkPhoneNumberDto = CheckPhoneNumberDto.builder().phoneNumber("01012345678").build();

        // when
        doReturn(new RedunCheckDto(true)).when(memberService).existsByPhoneNumber(any());
        // then
        mockMvc.perform(post("/auth/check-phone")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(checkPhoneNumberDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("auth/check-email/success", requestFields(
                        fieldWithPath("phoneNumber").description("phoneNumber to check redundance")
                ), responseFields(
                        fieldWithPath("exists").description("boolean value of existence")
                )));

    }

    @Test
    @DisplayName("전화번호 중복 확인 실패 테스트 - false 리턴")
    public void checkDuplicatePhoneTestReturnFalse() throws Exception{
        //given
        CheckPhoneNumberDto checkPhoneNumberDto = CheckPhoneNumberDto.builder().phoneNumber("01012345678").build();

        // when
        doReturn(new RedunCheckDto(false)).when(memberService).existsByPhoneNumber(any());
        // then
        mockMvc.perform(post("/auth/check-phone")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(checkPhoneNumberDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("auth/check-email/fail", requestFields(
                        fieldWithPath("phoneNumber").description("phoneNumber to check redundance")
                ), responseFields(
                        fieldWithPath("exists").description("boolean value of existence")
                )));
    }

    @Test
    @DisplayName("닉네임 중복 확인 성공 테스트 - true 리턴")
    public void checkDuplicateNicknameTestReturnTrue() throws Exception{
        //given
        CheckNicknameDto checkNicknameDto = new CheckNicknameDto("nickname");


        // when
        doReturn(new RedunCheckDto(true)).when(memberService).existsByNickname(any());

        // then
        mockMvc.perform(post("/auth/check-nickname")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(checkNicknameDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("auth/check-nickname/success", requestFields(
                        fieldWithPath("nickname").description("nickname to check redundance")
                ), responseFields(
                        fieldWithPath("exists").description("boolean value of existence")
                )));
    }

    @Test
    @DisplayName("닉네임 중복 확인 실패 테스트 - false 리턴")
    public void checkDuplicateNicknameTestReturnFalse() throws Exception{
        //given
        CheckNicknameDto checkNicknameDto = new CheckNicknameDto("nickname");


        // when
        doReturn(new RedunCheckDto(false)).when(memberService).existsByNickname(any());

        // then
        mockMvc.perform(post("/auth/check-nickname")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(checkNicknameDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("auth/check-nickname/success", requestFields(
                        fieldWithPath("nickname").description("nickname to check redundance")
                ), responseFields(
                        fieldWithPath("exists").description("boolean value of existence")
                )));
    }
}
