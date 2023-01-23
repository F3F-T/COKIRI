package f3f.dev1.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import f3f.dev1.domain.member.application.AuthService;
import f3f.dev1.domain.member.application.MemberService;
import f3f.dev1.domain.member.dao.MemberRepository;
import f3f.dev1.domain.member.model.Member;
import f3f.dev1.domain.address.model.Address;
import f3f.dev1.domain.post.api.PostController;
import f3f.dev1.domain.post.application.PostService;
import f3f.dev1.domain.tag.application.PostTagService;
import f3f.dev1.domain.tag.application.TagService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static f3f.dev1.domain.member.dto.MemberDTO.*;
import static f3f.dev1.domain.member.model.UserLoginType.EMAIL;
import static f3f.dev1.domain.post.dto.PostDTO.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
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
    private TagService tagService;

    @MockBean
    private PostTagService postTagService;

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
                .email(signUpRequest.getEmail())
                .id(1L)
                .build();
        return member;
    }

    public UpdatePostRequest createUpdatePostRequest() {
        return UpdatePostRequest.builder()
                .authorId(1L)
                .title("제목 맘에 안들어서 바꿈")
                .content("내용도 바꿀래요")
                .productCategory(null)
                .wishCategory(null)
                .build();
    }

    public PostSaveRequest createPostSaveRequest(Member author, boolean tradeEachOther) {
        return PostSaveRequest.builder()
                .content("냄새가 조금 나긴 하는데 뭐 그럭저럭 괜찮아요")
                .title("3년 신은 양말 거래 희망합니다")
                .tradeEachOther(tradeEachOther)
                .authorId(author.getId())
                .tagNames(new ArrayList<>())
                .productCategory(null)
                .wishCategory(null)
                .build();
    }

    public PostSaveRequest createPostSaveRequestWithDynamicAuthorId(Long authorId, boolean tradeEachOther) {
        return PostSaveRequest.builder()
                .content("냄새가 조금 나긴 하는데 뭐 그럭저럭 괜찮아요")
                .title("3년 신은 양말 거래 희망합니다")
                .tradeEachOther(tradeEachOther)
                .tagNames(new ArrayList<>())
                .productCategory(null)
                .wishCategory(null)
                .authorId(authorId)
                .build();
    }

    public DeletePostRequest createDeletePostRequest(Long postId, Long authorId) {
        return new DeletePostRequest(postId, authorId);
    }

    @Test
    @DisplayName("게시글 생성 테스트")
    @WithMockCustomUser
    public void createPostSuccessTest() throws Exception{
        //given
        // 어떤 값으로 postService를 호출하던 리턴값은 1L로 줘라.
        doReturn(1L).when(postService).savePost(any(), any());
        Member member = createMember();

        //when
        PostSaveRequest postSaveRequest = createPostSaveRequest(member, false);
        Long postId = postService.savePost(postSaveRequest, member.getId());

        //then
        // TODO rest docs 매개변수 수정하기
        mockMvc.perform(post("/post")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(postSaveRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document("post/create/successful",requestFields(
                        fieldWithPath("productCategory").description("productCategory name value of post"),
                        fieldWithPath("tradeEachOther").description("tradeEachOther value of post"),
                        fieldWithPath("wishCategory").description("wishCategory name value of post"),
                        fieldWithPath("tagNames").description("tag names list value of post"),
                        fieldWithPath("authorId").description("author id value of post"),
                        fieldWithPath("content").description("content value of post"),
                        fieldWithPath("price").description("price value of post"),
                        fieldWithPath("title").description("title value of post")
                )));
    }


    @Test
    @DisplayName("게시글 전체 조회 테스트")
    public void getAllPostInfoSuccessTest() throws Exception {
        //given
        PostSaveRequest postSaveRequest = createPostSaveRequestWithDynamicAuthorId(1L, false);
        PostSaveRequest postSaveRequest2 = createPostSaveRequestWithDynamicAuthorId(2L, false);
        PostSaveRequest postSaveRequest3 = createPostSaveRequestWithDynamicAuthorId(3L, false);
        Member member = createMember();
        //when
        Long postId = postService.savePost(postSaveRequest, member.getId());
        Long postId2 = postService.savePost(postSaveRequest2, member.getId());
        Long postId3 = postService.savePost(postSaveRequest3, member.getId());

        //then
        mockMvc.perform(get("/post")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString("")))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("태그 정보와 함께 게시글 조회 테스트")
    public void getPostsWithTagsTestForSuccess() throws Exception {
        //given

        //when

        //then
    }


    @Test
    @DisplayName("게시글 수정 테스트")
    @WithMockCustomUser
    public void updatePostSuccessTest() throws Exception {
        //given
        PostInfoDtoWithTag postInfoDto = PostInfoDtoWithTag.builder().title("제목 맘에 안들어서 바꿈").build();
        doReturn(postInfoDto).when(postService).updatePost(any(), any(), any());
        Member member = createMember();

        //when
        PostSaveRequest postSaveRequest = createPostSaveRequest(member, false);
        UpdatePostRequest updatePostRequest = createUpdatePostRequest();
        Long postId = postService.savePost(postSaveRequest, member.getId());

        //then
        mockMvc.perform(patch("/post" + "/{postId}", 1L)
        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatePostRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(updatePostRequest.getTitle()))
                .andDo(document("post/update/successful", requestFields(
                        fieldWithPath("id").description("Id value of post"),
                        fieldWithPath("authorId").description("Id value of auhor (requester)"),
                        fieldWithPath("title").description("title value of post"),
                        fieldWithPath("content").description("content value of post"),
                        fieldWithPath("price").description("price value of post"),
                        fieldWithPath("productCategory").description("product category name value of post"),
                        fieldWithPath("wishCategory").description("wish category name value of post"),
                        fieldWithPath("tagNames").description("list values of tag names")
                ), responseFields(
                        fieldWithPath("id").description("Id value of post"),
                        fieldWithPath("content").description("content value of post"),
                        fieldWithPath("tradeEachOther").description("tradeEachOther value of post"),
                        fieldWithPath("authorNickname").description("authorNickname value of post"),
                        fieldWithPath("wishCategory").description("wishCategory name value of post"),
                        fieldWithPath("productCategory").description("productCategory name value of post"),
                        fieldWithPath("scrapCount").description("scrap count value of post"),
                        fieldWithPath("messageRoomCount").description("message room count value of post"),
                        fieldWithPath("createdTime").description("created time value of post"),
                        fieldWithPath("price").description("price value of post"),
                        fieldWithPath("title").description("title name value of post"),
                        fieldWithPath("tagNames").description("list values of tag names"),
                        fieldWithPath("tradeStatus").description("tradeStatus value of post")
                )));
    }

    @Test
    @DisplayName("게시글 아이디로 특정 게시글 조회 테스트")
    public void getPostInfoByPostIdTestSuccess() throws Exception {
        //given
        Member member = new Member();
        PostSaveRequest postSaveRequest = createPostSaveRequest(member, false);
        SinglePostInfoDto postInfoDto = SinglePostInfoDto.builder().title(postSaveRequest.getTitle()).build();
        postService.savePost(postSaveRequest, member.getId());
        doReturn(postInfoDto).when(postService).findPostById(any());

        //when & then
        mockMvc.perform(get("/post/{postId}", 1L)
        .contentType(MediaType.APPLICATION_JSON)
        .content(""))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(postInfoDto.getTitle()))
                .andDo(document("post/readPostById/successful", responseFields(
                        fieldWithPath("id").description("Id value of post"),
                        fieldWithPath("content").description("content value of post"),
                        fieldWithPath("scrapCount").description("scrap count value of post"),
                        fieldWithPath("messageRoomCount").description("message room count value of post"),
                        fieldWithPath("createdTime").description("created time value of post"),
                        fieldWithPath("tradeEachOther").description("tradeEachOther value of post"),
                        fieldWithPath("wishCategory").description("wishCategory name value of post"),
                        fieldWithPath("productCategory").description("productCategory name value of post"),
                        fieldWithPath("price").description("price value of post"),
                        fieldWithPath("userInfo").description("userInfo DTO value"),
                        fieldWithPath("commentInfoDtoList").description("commentInfo DTO list value of post"),
                        fieldWithPath("title").description("title name value of post"),
                        fieldWithPath("tradeStatus").description("tradeStatus value of post"),
                        fieldWithPath("tagNames").description("tag name list value of post")
                )));
    }

    @Test
    @DisplayName("게시글 삭제 테스트")
    @WithMockCustomUser
    public void deletePostSuccessTest() throws Exception {
        //given
        DeletePostRequest deletePostRequest = createDeletePostRequest(1L, 1L);
        doReturn("DELETE").when(postService).deletePost(any(), any());
        doReturn(1L).when(postService).savePost(any(), any());
        Member member = createMember();

        //when
        PostSaveRequest postSaveRequest = createPostSaveRequest(member, false);
        Long postId = postService.savePost(postSaveRequest, member.getId());


        //then
        mockMvc.perform(delete("/post/{postId}", 1L)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(deletePostRequest)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andDo(document("post/delete/succssful", requestFields(
                        fieldWithPath("id").description("postId value of post"),
                        fieldWithPath("authorId").description("delete requester Id value")
                )));
        // TODO responseFields 완성해야함
    }

}
