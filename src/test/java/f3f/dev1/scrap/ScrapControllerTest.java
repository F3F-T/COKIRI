package f3f.dev1.scrap;

import com.fasterxml.jackson.databind.ObjectMapper;
import f3f.dev1.domain.member.application.MemberService;
import f3f.dev1.domain.member.dto.MemberDTO;
import f3f.dev1.domain.model.Address;
import f3f.dev1.domain.scrap.api.ScrapController;
import f3f.dev1.domain.scrap.application.ScrapService;
import f3f.dev1.domain.scrap.dao.ScrapRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static f3f.dev1.domain.member.model.UserLoginType.EMAIL;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

@WebMvcTest(ScrapController.class)
@MockBean(JpaMetamodelMappingContext.class)
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class ScrapControllerTest {
    @MockBean
    private ScrapService scrapService;

    @MockBean
    private ScrapRepository scrapRepository;

    @MockBean
    private MemberService memberService;

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
    public MemberDTO.SignUpRequest createSignUpRequest() {
        return MemberDTO.SignUpRequest.builder()
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
    public MemberDTO.LoginRequest createLoginRequest() {
        return MemberDTO.LoginRequest.builder()
                .email("userEmail@email.com")
                .password("password")
                .build();
    }

}
