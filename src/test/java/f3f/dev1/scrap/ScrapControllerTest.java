package f3f.dev1.scrap;

import com.fasterxml.jackson.databind.ObjectMapper;
import f3f.dev1.domain.member.application.MemberService;
import f3f.dev1.domain.member.dto.MemberDTO;
import f3f.dev1.domain.member.exception.NotAuthorizedException;
import f3f.dev1.domain.member.model.Member;
import f3f.dev1.domain.model.Address;
import f3f.dev1.domain.model.TradeStatus;
import f3f.dev1.domain.post.dto.PostDTO;
import f3f.dev1.domain.scrap.api.ScrapController;
import f3f.dev1.domain.scrap.application.ScrapService;
import f3f.dev1.domain.scrap.dao.ScrapRepository;
import f3f.dev1.domain.scrap.dto.ScrapDTO;
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
import java.util.List;

import static f3f.dev1.domain.scrap.dto.ScrapDTO.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

import static f3f.dev1.domain.member.dto.MemberDTO.*;
import static f3f.dev1.domain.member.model.UserLoginType.EMAIL;
import static f3f.dev1.domain.post.dto.PostDTO.*;

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
    public SignUpRequest createSignUpRequest() {
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

    // 로그인 DTO 생성 메소드
    public LoginRequest createLoginRequest() {
        return LoginRequest.builder()
                .email("userEmail@email.com")
                .password("password")
                .build();
    }

    // 포스트 생성 DTO 메소드
    public PostSaveRequest createPostSaveRequest(Long authorId) {
        return PostSaveRequest.builder()
                .authorId(authorId)
                .title("이건 테스트 게시글 제목이야")
                .content("이건 테스트 게시글 내용이지 하하")
                .tradeEachOther(false)
                .wishCategoryName(null)
                .productCategoryName(null)
                .build();
    }

    @Test
    @DisplayName("스크랩 포스트 조회 성공 테스트")
    @WithMockCustomUser
    public void getScrapPostTestSuccess() throws Exception{
        //given
        PostInfoDto postInfoDto = PostInfoDto.builder()
                .content("content")
                .id(1L)
                .productCategory(null)
                .wishCategory(null)
                .authorNickname("sellerNickname")
                .title("testPost")
                .tradeEachOther(true)
                .tradeStatus(TradeStatus.TRADABLE)
                .build();
        List<PostInfoDto> postList = new ArrayList<>();
        postList.add(postInfoDto);
        // when
        GetScrapPostDTO getScrapPostDTO = GetScrapPostDTO.builder().scrapPosts(postList).build();
        doReturn(getScrapPostDTO).when(scrapService).getUserScrapPosts(any());

        // then
        mockMvc.perform(get("/user/scrap"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("scrap/get-user-scrapPosts/success", responseFields(
                        fieldWithPath("scrapPosts").description("posts that user has scrapped"),
                        fieldWithPath("scrapPosts[0].id").description("id of post"),
                        fieldWithPath("scrapPosts[0].title").description("title of post"),
                        fieldWithPath("scrapPosts[0].content").description("content of post"),
                        fieldWithPath("scrapPosts[0].tradeEachOther").description("if post is trade-each-other"),
                        fieldWithPath("scrapPosts[0].authorNickname").description("author nickname"),
                        fieldWithPath("scrapPosts[0].wishCategory").description("wish product category"),
                        fieldWithPath("scrapPosts[0].productCategory").description("product category"),
                        fieldWithPath("scrapPosts[0].tradeStatus").description("trade status of product")
                )));
    }

    @Test
    @DisplayName("로그인 하지 않은 상태에서 스크랩 포스트 조회 실패 테스트")
    public void getScrapPostTestFailByNonLogin() throws Exception{

        // then
        mockMvc.perform(get("/user/scrap"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andDo(document("scrap/get-user-scrapPosts/fail/non-login", responseFields(
                        fieldWithPath("status").description("httpStatus of response"),
                        fieldWithPath("message").description("message of error response")
                )));
    }

    @Test
    @DisplayName("스크랩에 포스트 추가 성공 테스트")
    @WithMockCustomUser
    public void addScrapPostTestSuccess() throws Exception{
        //given
        AddScrapPostDTO addScrapPostDTO = AddScrapPostDTO.builder()
                .postId(1L)
                .userId(1L).build();
        // when
        doReturn(CreateScrapPostDTO.builder()
                .scrapPostId(1L)
                .postTitle("postTitle")
                .build()).when(scrapService).addScrapPost(any(), any());


        // then
        mockMvc.perform(post("/user/scrap")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addScrapPostDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("scrap/add-post/success", requestFields(
                        fieldWithPath("postId").description("Id of post"),
                        fieldWithPath("userId").description("Id of user")
                ), responseFields(
                        fieldWithPath("scrapPostId").description("Id of scrapPost"),
                        fieldWithPath("postTitle").description("Title of post")
                )));
    }

    @Test
    @DisplayName("로그인하지 않아서 스크랩에 포스트 추가 실패 테스트")
    public void addScrapPostTestFailByNonLogin() throws Exception{
        //given
        AddScrapPostDTO addScrapPostDTO = AddScrapPostDTO.builder()
                .postId(1L)
                .userId(1L).build();
        // then
        mockMvc.perform(post("/user/scrap")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addScrapPostDTO)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andDo(document("scrap/add-post/success", requestFields(
                        fieldWithPath("postId").description("Id of post"),
                        fieldWithPath("userId").description("Id of user")
                ), responseFields(
                        fieldWithPath("status").description("status of response"),
                        fieldWithPath("message").description("error message")
                )));
    }

    @Test
    @DisplayName("로그인된 사용자가 아닌 다른 사용자의 요청으로 인한 스크랩 포스트 추가 실패 테스트")
    public void addScrapPostTestFailByWrongUser() throws Exception{
        //given
        AddScrapPostDTO addScrapPostDTO = AddScrapPostDTO.builder()
                .postId(1L)
                .userId(1L).build();

        // when
        doThrow(NotAuthorizedException.class).when(scrapService).addScrapPost(any(), any());

        // then
        mockMvc.perform(post("/user/scrap")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addScrapPostDTO)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andDo(document("scrap/add-post/success", requestFields(
                        fieldWithPath("postId").description("Id of post"),
                        fieldWithPath("userId").description("Id of user")
                ), responseFields(
                        fieldWithPath("status").description("status of response"),
                        fieldWithPath("message").description("error message")
                )));
    }
    
    @Test
    @DisplayName("스크랩 포스트 제거 성공")
    @WithMockCustomUser
    public void deleteScrapPostTestSuccess() throws Exception{
        //given
        DeleteScrapPostDTO deleteScrapPostDTO = DeleteScrapPostDTO.builder().userId(1L).postId(1L).build();


        // when
        doReturn("DELETE").when(scrapService).deleteScrapPost(any(), any());
        // then
        mockMvc.perform(delete("/user/scrap")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deleteScrapPostDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("scrap/delete-post/success", requestFields(
                        fieldWithPath("userId").description("Id of user"),
                        fieldWithPath("postId").description("Id of post")
                )));
    }

    @Test
    @DisplayName("로그인하지 않은 상태에서 스크랩 포스트 삭제 실패 테스트")
    public void deleteScrapPostTestFailByNonLogin() throws Exception{
        //given
        DeleteScrapPostDTO deleteScrapPostDTO = DeleteScrapPostDTO.builder().userId(1L).postId(1L).build();

        // then
        mockMvc.perform(delete("/user/scrap")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deleteScrapPostDTO)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andDo(document("scrap/delete-post/success", requestFields(
                        fieldWithPath("userId").description("Id of user"),
                        fieldWithPath("postId").description("Id of post")
                ), responseFields(
                        fieldWithPath("status").description("status of response"),
                        fieldWithPath("message").description("error message")
                )));
    }

    @Test
    @DisplayName("잘못된 유저 아이디로 스크랩 포스트 삭제 실패 테스트")
    @WithMockCustomUser
    public void deleteScrapPostTestFailByWrongUser() throws Exception{
        //given
        DeleteScrapPostDTO deleteScrapPostDTO = DeleteScrapPostDTO.builder().userId(1L).postId(1L).build();


        // when
        doThrow(NotAuthorizedException.class).when(scrapService).deleteScrapPost(any(), any());

        // then
        mockMvc.perform(delete("/user/scrap")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deleteScrapPostDTO)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andDo(document("scrap/delete-post/success", requestFields(
                        fieldWithPath("userId").description("Id of user"),
                        fieldWithPath("postId").description("Id of post")
                ), responseFields(
                        fieldWithPath("status").description("status of response"),
                        fieldWithPath("message").description("error message")
                )));
    }
}
