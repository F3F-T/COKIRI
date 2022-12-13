package f3f.dev1.user;


import com.fasterxml.jackson.databind.ObjectMapper;
import f3f.dev1.domain.model.Address;
import f3f.dev1.domain.user.api.UserController;
import f3f.dev1.domain.user.application.SessionLoginService;
import f3f.dev1.domain.user.application.UserService;
import f3f.dev1.domain.user.dao.UserRepository;
import f3f.dev1.domain.user.exception.DuplicateEmailException;
import f3f.dev1.domain.user.exception.DuplicateNicknameException;
import f3f.dev1.domain.user.exception.DuplicatePhoneNumberExepction;
import f3f.dev1.domain.user.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static f3f.dev1.domain.user.dto.UserDTO.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

@WebMvcTest(UserController.class)
@MockBean(JpaMetamodelMappingContext.class)
public class UserControllerTest {
    @MockBean
    private UserService userService;
    @MockBean
    private SessionLoginService sessionLoginService;

    @MockBean
    private UserRepository userRepository;

    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    public void setup(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(sharedHttpSession())
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
                .build();
    }

    // 이메일 찾기 DTO 생성 메소드
    public FindEmailDto createFindEmailDto() {
        return FindEmailDto.builder()
                .username("username")
                .phoneNumber("01012345678").build();
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
                .andExpect(status().isCreated());


    }


    @Test
    @DisplayName("이메일 중복으로 인한 회원가입 실패 테스트")
    public void signUpTestFailByEmail() throws Exception{
        //given

        SignUpRequest differentUser = createSignUpRequest();

        // when

        doThrow(new DuplicateEmailException()).when(userService).signUp(any());

        // then
        mockMvc.perform(post("/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(differentUser)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("닉네임 중복으로 인한 회원가입 실패")
    public void signUpTestFailByNickname() throws Exception{
        //given
        SignUpRequest differentUser = createSignUpRequest();

        // when
        doThrow(new DuplicateNicknameException()).when(userService).signUp(any());

        // then
        mockMvc.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(differentUser)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("중복된 전화번호로 회원가입 실패 테스트")
    public void signUpTestFailByPhoneNumber() throws Exception{
        //given
        SignUpRequest differentUser = createSignUpRequest();

        // when
        doThrow(new DuplicatePhoneNumberExepction()).when(userService).signUp(any());

        // then
        mockMvc.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(differentUser)))
                .andDo(print())
                .andExpect(status().isConflict());
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
                .andExpect(status().isOk());
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
                .andExpect(status().isNotFound());

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
                .andExpect(status().isNotFound());
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
                .andExpect(status().isOk());
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
                .andExpect(status().isUnauthorized());

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
                .andExpect(status().isOk());
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
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("회원 삭제 요청 성공 테스트")
    public void deleteUserTestSuccess() throws Exception{
        //given
        given(sessionLoginService.getLoginUser()).willReturn(createSignUpRequest().getEmail());

        // then
        mockMvc.perform(delete("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(""))).andDo(print()).andExpect(status().isOk());
    }

    @Test
    @DisplayName("이메일 찾기 성공 테스트")
    public void findEmailTestSuccess() throws Exception{
        //given
        given(userRepository.findByUserNameAndPhoneNumber(any(), any())).willReturn(Optional.ofNullable(createSignUpRequest().toEntity()));

        // then
        mockMvc.perform(post("/user/find/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createFindEmailDto())))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("존재하지 않는 유저로 인한 이메일 찾기 실패 테스트")
    public void findEmailTestFailByNullUser() throws Exception{
        // given
        doThrow(UserNotFoundException.class).when(userService).findUserEmail(any());

        // then
        mockMvc.perform(post("/user/find/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createFindEmailDto())))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
