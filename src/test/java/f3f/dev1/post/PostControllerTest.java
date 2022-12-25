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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

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

    public UpdatePostRequest createUpdatePostRequest() {
        return UpdatePostRequest.builder()
                .postId(1L)
                .title("제목 맘에 안들어서 바꿈")
                .content("내용도 바꿀래요")
                .productCategoryId(null)
                .wishCategoryId(null)
                .postTags(null)
                .build();
    }

    public PostSaveRequest createPostSaveRequest(Member author, boolean tradeEachOther) {
        return PostSaveRequest.builder()
                .content("냄새가 조금 나긴 하는데 뭐 그럭저럭 괜찮아요")
                .title("3년 신은 양말 거래 희망합니다")
                .tradeEachOther(tradeEachOther)
                .authorId(author.getId())
                .productCategory(null)
                .wishCategory(null)
                .build();
    }

    public PostSaveRequest createPostSaveRequestWithDynamicAuthorId(Long authorId, boolean tradeEachOther) {
        return PostSaveRequest.builder()
                .content("냄새가 조금 나긴 하는데 뭐 그럭저럭 괜찮아요")
                .title("3년 신은 양말 거래 희망합니다")
                .tradeEachOther(tradeEachOther)
                .authorId(authorId)
                .productCategory(null)
                .wishCategory(null)
                .build();
    }

    public DeletePostRequest createDeletePostRequest(Long postId, Long authorId) {
        return DeletePostRequest.builder()
                .requesterId(authorId)
                .postId(postId)
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
        // TODO rest docs 매개변수 수정하기
        mockMvc.perform(post("/post")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(postSaveRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document("post/create/successful",requestFields(
                        fieldWithPath("productCategory").description("productCategory value of post"),
                        fieldWithPath("tradeEachOther").description("tradeEachOther value of post"),
                        fieldWithPath("wishCategory").description("wishCategory value of post"),
                        fieldWithPath("authorId").description("author id value of post"),
                        fieldWithPath("content").description("content value of post"),
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

        //when
        Long postId = postService.savePost(postSaveRequest);
        Long postId2 = postService.savePost(postSaveRequest2);
        Long postId3 = postService.savePost(postSaveRequest3);

        //then
        mockMvc.perform(get("/post")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString("")))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    @DisplayName("게시글 수정 테스트")
    @WithMockCustomUser
    public void updatePostSuccessTest() throws Exception {
        //given
        PostInfoDto postInfoDto = PostInfoDto.builder().title("제목 맘에 안들어서 바꿈").build();
        doReturn(postInfoDto).when(postService).updatePost(any());
        Member member = createMember();

        //when
        PostSaveRequest postSaveRequest = createPostSaveRequest(member, false);
        UpdatePostRequest updatePostRequest = createUpdatePostRequest();
        Long postId = postService.savePost(postSaveRequest);

        //then
        mockMvc.perform(patch("/post" + "/{postId}", 1L)
        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatePostRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(updatePostRequest.getTitle()))
                .andDo(document("post/update/successful", requestFields(
                        fieldWithPath("postId").description("Id value of post"),
                        fieldWithPath("title").description("title value of post"),
                        fieldWithPath("content").description("content value of post"),
                        fieldWithPath("productCategoryId").description("product category Id value of post"),
                        fieldWithPath("wishCategoryId").description("wish category Id value of post"),
                        fieldWithPath("postTags").description("postTag values of post")
                ), responseFields(
                        fieldWithPath("id").description("Id value of post"),
                        fieldWithPath("content").description("content value of post"),
                        fieldWithPath("tradeEachOther").description("tradeEachOther value of post"),
                        fieldWithPath("authorNickname").description("authorNickname value of post"),
                        fieldWithPath("wishCategory").description("wishCategory name value of post"),
                        fieldWithPath("productCategory").description("productCategory name value of post"),
                        fieldWithPath("title").description("title name value of post"),
                        fieldWithPath("tradeStatus").description("tradeStatus value of post")
                )));
    }

    @Test
    @DisplayName("게시글 아이디로 특정 게시글 조회 테스트")
    public void getPostInfoByPostIdTestSuccess() throws Exception {
        //given
        Member member = new Member();
        PostSaveRequest postSaveRequest = createPostSaveRequest(member, false);
        PostInfoDto postInfoDto = PostInfoDto.builder().title(postSaveRequest.getTitle()).build();
        postService.savePost(postSaveRequest);
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
                        fieldWithPath("tradeEachOther").description("tradeEachOther value of post"),
                        fieldWithPath("authorNickname").description("authorNickname value of post"),
                        fieldWithPath("wishCategory").description("wishCategory name value of post"),
                        fieldWithPath("productCategory").description("productCategory name value of post"),
                        fieldWithPath("title").description("title name value of post"),
                        fieldWithPath("tradeStatus").description("tradeStatus value of post")
                )));
    }

    @Test
    @DisplayName("게시글 삭제 테스트")
    @WithMockCustomUser
    public void deletePostSuccessTest() throws Exception {
        //given
        DeletePostRequest deletePostRequest = createDeletePostRequest(1L, 1L);
        doReturn("DELETE").when(postService).deletePost(any());
        doReturn(1L).when(postService).savePost(any());
        Member member = createMember();

        //when
        PostSaveRequest postSaveRequest = createPostSaveRequest(member, false);
        Long postId = postService.savePost(postSaveRequest);


        //then
        mockMvc.perform(delete("/post/{postId}", 1L)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(deletePostRequest)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andDo(document("post/delete/succssful", requestFields(
                        fieldWithPath("postId").description("postId value of post"),
                        fieldWithPath("requesterId").description("delete requesterId")
                )));
        // TODO responseFields 완성해야함
    }

}
