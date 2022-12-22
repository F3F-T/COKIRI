package f3f.dev1.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import f3f.dev1.domain.member.application.AuthService;
import f3f.dev1.domain.member.application.MemberService;
import f3f.dev1.domain.member.dao.MemberRepository;
import f3f.dev1.domain.member.dto.MemberDTO;
import f3f.dev1.domain.member.model.Member;
import f3f.dev1.domain.model.Address;
import f3f.dev1.domain.post.api.PostController;
import f3f.dev1.domain.post.application.PostService;
import f3f.dev1.domain.post.dto.PostDTO;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static f3f.dev1.domain.member.dto.MemberDTO.*;
import static f3f.dev1.domain.member.model.UserLoginType.EMAIL;
import static f3f.dev1.domain.post.dto.PostDTO.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

@WebMvcTest(PostController.class)
@MockBean(JpaMetamodelMappingContext.class)
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class PostControllerTest {

    @MockBean
    private PostService postService;

    @MockBean
    private MemberService memberService;

    @MockBean
    private AuthService authService;

    @MockBean
    private MemberRepository memberRepository;

    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentationContextProvider) {
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

    // 회원가입 DTO
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

    public Member createMember() {
        SignUpRequest signUpRequest = createSignUpRequest();
        Member member = Member.builder()
                .phoneNumber(signUpRequest.getPhoneNumber())
                .birthDate(signUpRequest.getBirthDate())
                .nickname(signUpRequest.getNickname())
                .password(signUpRequest.getPassword())
                .address(signUpRequest.getAddress())
                .email(signUpRequest.getEmail())
                .id(1L)
                .build();
        return member;
    }

    public PostSaveRequest createPostSaveRequest(Member author, boolean tradeEachOther) {
        return PostSaveRequest.builder()
                .content("냄새가 조금 나긴 하는데 뭐 그럭저럭 괜찮아요")
                .title("3년 신은 양말 거래 희망합니다")
                .tradeEachOther(tradeEachOther)
                .productCategory(null)
                .wishCategory(null)
                .author(author)
                .build();
    }

    @Test
    @DisplayName("게시글 생성 테스트")
    @WithMockCustomUser
    public void createPostSuccessTest() throws Exception{
        //given
        // 어떤 값으로 postService를 호출하던 리턴값은 1L로 줘라.
        doReturn(1L).when(postService).savePost(any());
        Member member = createMember();

        //when
        PostSaveRequest postSaveRequest = createPostSaveRequest(member, false);
        Long postId = postService.savePost(postSaveRequest);

        //then
        mockMvc.perform(post("/post")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(postSaveRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document("post/create/successful",requestFields(
                        fieldWithPath("productCategory").description("productCategory value of post"),
                        fieldWithPath("tradeEachOther").description("tradeEachOther value of post"),
                        fieldWithPath("wishCategory").description("wishCategory value of post"),
                        fieldWithPath("content").description("content value of post"),
                        fieldWithPath("author").description("author value of post"),
                        fieldWithPath("author.createDate").description("author's create date value of post"),
                        fieldWithPath("author.modifiedDate").description("author's modified date value of post"),
                        fieldWithPath("author.id").description("author's id value of post"),
                        fieldWithPath("author.email").description("author's email value of post"),
                        fieldWithPath("author.password").description("author's password value of post"),
                        fieldWithPath("author.authority").description("author's authority value of post"),
                        fieldWithPath("author.userLoginType").description("author's userLoginType value of post"),
                        fieldWithPath("author.address").description("author's address value of post"),
                        fieldWithPath("author.address.addressName").description("user address's addressName value"),
                        fieldWithPath("author.address.postalAddress").description("user address's postalAddress value"),
                        fieldWithPath("author.address.latitude").description("user address's latitude value"),
                        fieldWithPath("author.address.longitude").description("user address's longitude value"),
                        fieldWithPath("author.userName").description("author's name value of post"),
                        fieldWithPath("author.birthDate").description("author's birthDate value of post"),
                        fieldWithPath("author.phoneNumber").description("author's phoneNumber value of post"),
                        fieldWithPath("author.nickname").description("author's nickname value of post"),
                        fieldWithPath("author.posts").description("author's post list value of post"),
                        fieldWithPath("author.comments").description("author's comment list value of post"),
                        fieldWithPath("author.scrap").description("author's scrap list value of post"),
                        fieldWithPath("author.sellingRooms").description("author's sellingRoom list value of post"),
                        fieldWithPath("author.buyingRooms").description("author's buyingRoom list value of post"),
                        fieldWithPath("author.sendMessages").description("author's sendMessages value of post"),
                        fieldWithPath("author.receivedMessages").description("author's receivedMessages value of post"),
                        fieldWithPath("author.buyingTrades").description("author's buyingTrade list value of post"),
                        fieldWithPath("author.sellingTrades").description("author's sellingTrade list value of post"),
                        fieldWithPath("title").description("title value of post")
                )));
    }

//    @Test
//    @DisplayName("게시글 생성 실패 - ")
//    public void () throws Exception {
//        //given
//
//        //when
//
//        //then
//    }
}
