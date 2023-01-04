package f3f.dev1.trade;

import com.fasterxml.jackson.databind.ObjectMapper;
import f3f.dev1.domain.member.application.AuthService;
import f3f.dev1.domain.member.dao.MemberRepository;
import f3f.dev1.domain.member.model.Member;
import f3f.dev1.domain.model.Address;
import f3f.dev1.domain.model.TradeStatus;
import f3f.dev1.domain.post.dao.PostRepository;
import f3f.dev1.domain.post.dto.PostDTO;
import f3f.dev1.domain.post.dto.PostDTO.PostSaveRequest;
import f3f.dev1.domain.trade.api.TradeController;
import f3f.dev1.domain.trade.application.TradeService;
import f3f.dev1.domain.member.application.MemberService;
import f3f.dev1.domain.trade.dao.TradeRepository;
import f3f.dev1.domain.trade.dto.TradeDTO;
import f3f.dev1.domain.trade.exception.DuplicateTradeException;
import f3f.dev1.domain.trade.exception.InvalidSellerIdException;
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

import java.util.Optional;

import static f3f.dev1.domain.member.model.UserLoginType.EMAIL;
import static f3f.dev1.domain.model.TradeStatus.TRADABLE;
import static f3f.dev1.domain.model.TradeStatus.TRADING;
import static f3f.dev1.domain.trade.dto.TradeDTO.*;
import static f3f.dev1.domain.trade.dto.TradeDTO.CreateTradeDto;
import static f3f.dev1.domain.trade.dto.TradeDTO.UpdateTradeDto;
import static f3f.dev1.domain.member.dto.MemberDTO.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@WebMvcTest(TradeController.class)
@MockBean(JpaMetamodelMappingContext.class)
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class TradeControllerTest {
    @MockBean
    private MemberService memberService;

    @MockBean
    private TradeService tradeService;

    @MockBean
    private AuthService authService;

    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private PostRepository postRepository;

    @MockBean
    private TradeRepository tradeRepository;


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

    // 판매자 DTO 생성 메소드
    public SignUpRequest createSellerRequest() {
        return SignUpRequest.builder()
                .userName("username")
                .nickname("nickname")
                .phoneNumber("01012345678")
                .email("userEmail@email.com")
                .birthDate("990128")
                .password("password")
                .userLoginType(EMAIL)
                .build();
    }

    // 구매자 DTO 생성 메소드
    public SignUpRequest createBuyerRequest() {
        return SignUpRequest.builder()
                .userName("buyerName")
                .nickname("buyerNickname")
                .phoneNumber("01043218765")
                .email("buyer@email.com")
                .birthDate("990130")
                .password("12345678")
                .userLoginType(EMAIL)
                .build();

    }

    // 게시글 생성 DTO 메소드
    public PostSaveRequest createPostSaveRequest(Long authorId) {

        return PostSaveRequest.builder()
                .productCategoryName(null)
                .wishCategoryName(null)
                .tradeEachOther(false)
                .title("title")
                .content("content")
                .authorId(authorId).build();


    }

    // 로그인 DTO 생성 메소드
    public LoginRequest createLoginRequest() {
        return LoginRequest.builder()
                .email("userEmail@email.com")
                .password("password").build();
    }

    public CreateTradeDto createTradeDto(Long sellerId, Long buyerId, Long postId) {
        return CreateTradeDto.builder()
                .sellerId(sellerId)
                .buyerId(buyerId)
                .postId(postId)
                .build();
    }

    public UpdateTradeDto createUpdateTradeDto(Long postId) {
        return UpdateTradeDto.builder()
                .postId(postId)
                .tradeStatus(TRADING).build();
    }

    @Test
    @DisplayName("트레이드 생성 성공 테스트")
    @WithMockCustomUser
    public void createTradeTestSuccess() throws Exception{
        //given

        doReturn(1L).when(tradeService).createTrade(any(), any());
        // then
        mockMvc.perform(post("/trade")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTradeDto(1L, 2L, 1L))))
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document("trade/create/successful",
                        requestFields(
                        fieldWithPath("sellerId").description("Id value of seller"),
                        fieldWithPath("buyerId").description("Id value of buyer"),
                        fieldWithPath("postId").description("Id value of post"))));
    }

    @Test
    @DisplayName("이미 존재하는 거래로 인한 트레이드 생성 실패 테스트")
    @WithMockCustomUser
    public void createTradeTestFailByDuplicateTrade() throws Exception{
        //given

        doThrow(DuplicateTradeException.class).when(tradeService).createTrade(any(), any());

        // then
        mockMvc.perform(post("/trade")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTradeDto(1L, 2L, 1L))))
                .andDo(print())
                .andExpect(status().isConflict())
                .andDo(document("trade/create/fail/duplicate-trade", requestFields(
                        fieldWithPath("sellerId").description("Id value of seller"),
                        fieldWithPath("buyerId").description("Id value of buyer"),
                        fieldWithPath("postId").description("Id value of post")), responseFields(
                        fieldWithPath("status").description("HttpStatus of response"),
                        fieldWithPath("message").description("message of response")
                )));
    }

    @Test
    @DisplayName("잘못된 판매자로 인한 트레이드 생성 실패 테스트")
    @WithMockCustomUser
    public void createTradeTestFailByInvalidSeller() throws Exception{
        //given

        doThrow(InvalidSellerIdException.class).when(tradeService).createTrade(any(), any());


        // then
        mockMvc.perform(post("/trade")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTradeDto(1L, 2L, 1L))))
                .andDo(print())
                .andExpect(status().isConflict())
                .andDo(document("trade/create/fail/invalid-seller", requestFields(
                        fieldWithPath("sellerId").description("Id value of seller"),
                        fieldWithPath("buyerId").description("Id value of buyer"),
                        fieldWithPath("postId").description("Id value of post")), responseFields(
                        fieldWithPath("status").description("HttpStatus of response"),
                        fieldWithPath("message").description("message of response")
                )));
    }

    @Test
    @DisplayName("로그인 안된 상태에서 트레이드 생성 실패 테스트")
    public void createTradeTestFailByNonLogin() throws Exception{

        // then
        mockMvc.perform(post("/trade")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createTradeDto(1L, 2L, 1L))))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andDo(document("trade/create/fail/non-login", requestFields(
                        fieldWithPath("sellerId").description("Id value of seller"),
                        fieldWithPath("buyerId").description("Id value of buyer"),
                        fieldWithPath("postId").description("Id value of post")), responseFields(
                        fieldWithPath("status").description("HttpStatus of response"),
                        fieldWithPath("message").description("message of response")
                )));
    }

    @Test
    @DisplayName("거래 상태 변경 성공 테스트")
    @WithMockCustomUser
    public void updateTradeStatusTestSuccess() throws Exception{
        //given
        doReturn(TradeInfoDto.builder()
                .sellerNickname("sellerNickname")
                .buyerNickname("buyerNickname")
                .tradeStatus(TRADABLE).build()).when(tradeService).updateTradeStatus(any(), any());

        // then
        mockMvc.perform(patch("/trade")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUpdateTradeDto(1L))))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("trade/update/successful", requestFields(
                        fieldWithPath("postId").description("Id value of trade"),
                        fieldWithPath("userId").description("Id value of user"),
                        fieldWithPath("tradeStatus").description("TradeStatus to update")
                ), responseFields(
                        fieldWithPath("sellerNickname").description("Nickname of seller"),
                        fieldWithPath("buyerNickname").description("Nickname of buyer"),
                        fieldWithPath("tradeStatus").description("TradeStatus of trade")
                )));
    }

    @Test
    @DisplayName("잘못된 판매자 요청으로 거래 상태 업데이트 실패 테스트")
    @WithMockCustomUser
    public void updateTradeStatusTestFailByInvalidSeller() throws Exception{
        //given
        doThrow(InvalidSellerIdException.class).when(tradeService).updateTradeStatus(any(), any());

        // then
        mockMvc.perform(patch("/trade")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUpdateTradeDto(1L))))
                .andDo(print())
                .andExpect(status().isConflict())
                .andDo(document("trade/update/fail/invalid-seller", requestFields(
                        fieldWithPath("postId").description("Id value of trade"),
                        fieldWithPath("userId").description("Id value of user"),
                        fieldWithPath("tradeStatus").description("TradeStatus to update")
                ), responseFields(
                        fieldWithPath("status").description("HttpStatus of response"),
                        fieldWithPath("message").description("description of error")
                )));

    }

    @Test
    @DisplayName("로그인하지 않은 상태에서 거래 상태 업데이트 실패 테스트")
    public void updateTradeStatusTestFailByNonLogin() throws Exception{
        // then
        mockMvc.perform(patch("/trade")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUpdateTradeDto(1L))))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andDo(document("trade/update/fail/invalid-seller", requestFields(
                        fieldWithPath("postId").description("Id value of trade"),
                        fieldWithPath("userId").description("Id value of user"),
                        fieldWithPath("tradeStatus").description("TradeStatus to update")
                ), responseFields(
                        fieldWithPath("status").description("HttpStatus of response"),
                        fieldWithPath("message").description("description of error")
                )));
    }
    @Test
    @DisplayName("거래 조회 성공 테스트")
    @WithMockCustomUser
    public void getTradeInfoTestSuccess() throws Exception{
        //given
        doReturn(TradeInfoDto.builder()
                .sellerNickname("userNickname")
                .buyerNickname("none")
                .tradeStatus(TradeStatus.TRADABLE).build()).when(tradeService).getTradeInfo(any());
        // then
        mockMvc.perform(get("/trade/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString("")))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("trade/get-trade/success",responseFields(
                        fieldWithPath("sellerNickname").description("Nickname of seller"),
                        fieldWithPath("buyerNickname").description("Nickname of buyer"),
                        fieldWithPath("tradeStatus").description("TradeStatus of trade")
                )));

    }
    @Test
    @DisplayName("거래 삭제 성공 테스트")
    @WithMockCustomUser
    public void deleteTradeTestSuccess() throws Exception{
        //given
        DeleteTradeDto deleteTradeDto = DeleteTradeDto.builder()
                .userId(1L)
                .postId(1L)
                .build();

        // when
        doReturn(TradeInfoDto.builder()
                .sellerNickname("userNickname")
                .buyerNickname("none")
                .tradeStatus(TradeStatus.TRADABLE).build()).when(tradeService).deleteTrade(any(), any());

        // then
        mockMvc.perform(delete("/trade")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deleteTradeDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("trade/delete/success",requestFields(
                        fieldWithPath("postId").description("Id of post"),
                        fieldWithPath("userId").description("Id of user")
                ), responseFields(
                        fieldWithPath("sellerNickname").description("Nickname of seller"),
                        fieldWithPath("buyerNickname").description("Nickname of buyer"),
                        fieldWithPath("tradeStatus").description("TradeStatus of trade")
                )));
    }
    @Test
    @DisplayName("로그인하지 않은 상태에서 거래 삭제 실패 테스트")
    public void deleteTradeTestFailByNonLogin() throws Exception{
        //given
        DeleteTradeDto deleteTradeDto = DeleteTradeDto.builder()
                .userId(1L)
                .postId(1L)
                .build();



        // then
        mockMvc.perform(delete("/trade")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deleteTradeDto)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andDo(document("trade/delete/fail/non-login",requestFields(
                        fieldWithPath("postId").description("Id of post"),
                        fieldWithPath("userId").description("Id of user")
                ), responseFields(
                        fieldWithPath("status").description("HttpStatus of response"),
                        fieldWithPath("message").description("description of error")
                )));
    }

    @Test
    @DisplayName("잘못된 판매자로 인한 거래 삭제 실패 테스트")
    @WithMockCustomUser
    public void deleteTradeTestFailByInvalidSeller() throws Exception{
        //given
        DeleteTradeDto deleteTradeDto = DeleteTradeDto.builder()
                .userId(1L)
                .postId(1L)
                .build();

        // when
        doThrow(InvalidSellerIdException.class).when(tradeService).deleteTrade(any(), any());

        // then
        mockMvc.perform(delete("/trade")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deleteTradeDto)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andDo(document("trade/delete/fail/invalid-seller",requestFields(
                        fieldWithPath("postId").description("Id of post"),
                        fieldWithPath("userId").description("Id of user")
                ), responseFields(
                        fieldWithPath("status").description("HttpStatus of response"),
                        fieldWithPath("message").description("description of error")
                )));
    }
}
