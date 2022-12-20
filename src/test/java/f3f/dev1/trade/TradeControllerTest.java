package f3f.dev1.trade;

import com.fasterxml.jackson.databind.ObjectMapper;
import f3f.dev1.domain.model.Address;
import f3f.dev1.domain.trade.api.TradeController;
import f3f.dev1.domain.trade.application.TradeService;
import f3f.dev1.domain.member.application.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static f3f.dev1.domain.model.TradeStatus.TRADING;
import static f3f.dev1.domain.trade.dto.TradeDTO.CreateTradeDto;
import static f3f.dev1.domain.trade.dto.TradeDTO.UpdateTradeDto;
import static f3f.dev1.domain.member.dto.MemberDTO.*;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

@WebMvcTest(TradeController.class)
@MockBean(JpaMetamodelMappingContext.class)
public class TradeControllerTest {
    @MockBean
    private MemberService memberService;

    @MockBean
    private TradeService tradeService;

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

    public CreateTradeDto createTradeDto(Long sellerId, Long buyerId, Long postId) {
        return CreateTradeDto.builder()
                .sellerId(sellerId)
                .buyerId(buyerId)
                .postId(postId)
                .build();
    }

    public UpdateTradeDto createUpdateTradeDto(Long tradeId) {
        return UpdateTradeDto.builder()
                .tradeId(tradeId)
                .tradeStatus(TRADING).build();
    }

    @Test
    @DisplayName("트레이드 생성 성공 테스트")
    public void createTradeTestSuccess() throws Exception{
        //given


        // when

        // then
    }
}
