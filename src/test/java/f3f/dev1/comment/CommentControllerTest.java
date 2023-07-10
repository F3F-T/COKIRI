package f3f.dev1.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import f3f.dev1.domain.comment.api.CommentController;
import f3f.dev1.domain.comment.application.CommentService;
import f3f.dev1.domain.member.dto.MemberDTO;
import f3f.dev1.domain.member.model.Member;
import f3f.dev1.domain.address.model.Address;
import f3f.dev1.domain.post.application.PostService;
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
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static f3f.dev1.domain.comment.dto.CommentDTO.*;
import static f3f.dev1.domain.member.model.UserLoginType.EMAIL;
import static f3f.dev1.domain.post.dto.PostDTO.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

@WebMvcTest(CommentController.class)
@MockBean(JpaMetamodelMappingContext.class)
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class CommentControllerTest {
    @MockBean
    private PostService postService;

    @MockBean
    private CommentService commentService;

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

    public MemberDTO.SignUpRequest createSignUpRequest() {
        return MemberDTO.SignUpRequest.builder()
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
        MemberDTO.SignUpRequest signUpRequest = createSignUpRequest();
        Member member = Member.builder()
                .phoneNumber(signUpRequest.getPhoneNumber())
                .birthDate(signUpRequest.getBirthDate())
                .nickname(signUpRequest.getNickname())
                .password(signUpRequest.getPassword())
//                .address(signUpRequest.getAddress())
                .email(signUpRequest.getEmail())
                .id(1L)
                .build();
        return member;
    }

    public PostSaveRequest createPostSaveRequest(Member author, boolean tradeEachOther, String productName, String wishName) {
        return PostSaveRequest.builder()
                .tradeEachOther(tradeEachOther)
                .productCategory(productName)
                .tagNames(new ArrayList<>())
                .content("테스트용 게시글 본문")
                .authorId(author.getId())
                .title("테스트용 게시글 제목")
                .wishCategory(wishName)
                .price(10000L)
                .thumbnail("")
                .build();
    }

    public CreateCommentRequest createCommentRequest(Member author) {
        return CreateCommentRequest.builder()
                .authorId(author.getId())
                .postId(1L)
                .depth(1L)
                .content("테스트용 댓글")
                .parentCommentId(null)
                .build();
    }

    public CommentInfoDto createCommentInfoDto(Long commentId, Long postId, Long memberId, String content) {
        return CommentInfoDto.builder()
                .createdTime(LocalDateTime.now())
                .memberNickname("Owen")
                .parentCommentId(1L)
                .memberId(memberId)
                .content(content)
                .postId(postId)
                .id(commentId)
                .imageUrl("")
                .depth(1L)
                .build();
    }

    public UpdateCommentRequest createUpdateCommentRequest(Long commentId, Long parentId, Long authorId, Long postId, String content) {
        return new UpdateCommentRequest(commentId, parentId, authorId, postId, content);
    }

    public DeleteCommentRequest createDeleteCommentRequest(Long commentId, Long authorId, Long postId) {
        return new DeleteCommentRequest(commentId, authorId, postId);
    }

    @Test
    @DisplayName("댓글 생성 테스트")
    @WithMockCustomUser
    public void createCommentTestForSuccess() throws Exception {
        //given
        doReturn(1L).when(postService).savePost(any(), any());
        Member member = createMember();
        PostSaveRequest postSaveRequest = createPostSaveRequest(member, false, "product", "wish");
        Long postId = postService.savePost(postSaveRequest, member.getId());

        CreateCommentRequest commentRequest = createCommentRequest(member);
        CommentInfoDto commentInfoDto = createCommentInfoDto(1L, postId, member.getId(), "새로 만든 댓글");
        doReturn(commentInfoDto).when(commentService).saveComment(any(), any());
        postService.savePost(postSaveRequest, member.getId());

        //when & then
        mockMvc.perform(post("/post/{postId}/comments", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentRequest)))
                .andDo(print())
                .andExpect(jsonPath("$.content").value("새로 만든 댓글"))
                .andExpect(status().isCreated())
                .andDo(document("comment/create/success", requestFields(
                        fieldWithPath("parentCommentId").description("Id value of parent comment").type(JsonFieldType.NUMBER).optional(),
                        fieldWithPath("content").description("content value of post").type(JsonFieldType.STRING),
                        fieldWithPath("authorId").description("Id value of author").type(JsonFieldType.NUMBER),
                        fieldWithPath("depth").description("depth value of post").type(JsonFieldType.NUMBER),
                        fieldWithPath("postId").description("Id value of post").type(JsonFieldType.NUMBER)
                ), responseFields(
                        fieldWithPath("createdTime").description("created time value of comment").type(JsonFieldType.STRING),
                        fieldWithPath("parentCommentId").description("Id value of parent comment").type(JsonFieldType.NUMBER),
                        fieldWithPath("memberNickname").description("nickname value of member").type(JsonFieldType.STRING),
                        fieldWithPath("imageUrl").description("image url value of member").type(JsonFieldType.STRING),
                        fieldWithPath("depth").description("depth value of comment").type(JsonFieldType.NUMBER),
                        fieldWithPath("memberId").description("Id value of member").type(JsonFieldType.NUMBER),
                        fieldWithPath("content").description("content of comment").type(JsonFieldType.STRING),
                        fieldWithPath("postId").description("Id value of post").type(JsonFieldType.NUMBER),
                        fieldWithPath("id").description("Id value of comment").type(JsonFieldType.NUMBER)
                )));

    }

    @Test
    @DisplayName("댓글 조회 테스트 성공")
    public void findCommentsByPostIdTestForSuccess() throws Exception {
        //given
        doReturn(1L).when(postService).savePost(any(), any());
        Member member = createMember();
        PostSaveRequest postSaveRequest = createPostSaveRequest(member, false, "product", "wish");
        Long postId = postService.savePost(postSaveRequest, member.getId());

        // when
        List<CommentInfoDto> commentInfoDtoList = new ArrayList<>();
        CommentInfoDto commentInfoDto = createCommentInfoDto(1L, postId, member.getId(), "댓글1");
        commentInfoDtoList.add(commentInfoDto);
        doReturn(commentInfoDtoList).when(commentService).findCommentDtosByPostId(any());
        postService.savePost(postSaveRequest, member.getId());

        //then
        mockMvc.perform(get("/post/{postId}/comments", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].content").value("댓글1"))
                .andExpect(jsonPath("$.[0].memberId").value(member.getId()))
                .andDo(document("comment/readCommentsByPostId/success", responseFields(
                        fieldWithPath("[].parentCommentId").description("Id value of parent comment").type(JsonFieldType.NUMBER),
                        fieldWithPath("[].createdTime").description("created time value of comment").type(JsonFieldType.STRING),
                        fieldWithPath("[].memberNickname").description("nickname value of member").type(JsonFieldType.STRING),
                        fieldWithPath("[].imageUrl").description("image url value of member").type(JsonFieldType.STRING),
                        fieldWithPath("[].depth").description("depth value of comment").type(JsonFieldType.NUMBER),
                        fieldWithPath("[].memberId").description("Id value of member").type(JsonFieldType.NUMBER),
                        fieldWithPath("[].content").description("content of comment").type(JsonFieldType.STRING),
                        fieldWithPath("[].postId").description("Id value of post").type(JsonFieldType.NUMBER),
                        fieldWithPath("[].id").description("Id value of comment").type(JsonFieldType.NUMBER)
                )));
    }

    @Test
    @DisplayName("댓글 조회 테스트 실패 - 없는 포스트에 대한 댓글 조회 요청")
    public void findCommentsByPostIdTestForFail() throws Exception {
        //given
        doReturn(1L).when(postService).savePost(any(), any());
        Member member = createMember();
        PostSaveRequest postSaveRequest = createPostSaveRequest(member, false, "product", "wish");
        Long postId = postService.savePost(postSaveRequest, member.getId());

        // when
        List<CommentInfoDto> commentInfoDtoList = new ArrayList<>();
        CommentInfoDto commentInfoDto = createCommentInfoDto(1L, postId, member.getId(), "댓글1");
        commentInfoDtoList.add(commentInfoDto);
        // 존재하지 않는 포스트 조회
        doReturn(null).when(commentService).findCommentDtosByPostId(-1L);
        postService.savePost(postSaveRequest, member.getId());

        //then
        MvcResult mvcResult = mockMvc.perform(get("/post/{postId}/comments", 20L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertThat(content).isEqualTo("[]");
    }


    @Test
    @DisplayName("댓글 수정 테스트")
    @WithMockCustomUser
    public void updateCommentTestForSuccess() throws Exception {
        //given
        doReturn(1L).when(postService).savePost(any(), any());
        Member member = createMember();
        PostSaveRequest postSaveRequest = createPostSaveRequest(member, false, "product", "wish");
        Long postId = postService.savePost(postSaveRequest, member.getId());

        //when
        CommentInfoDto commentInfoDto = createCommentInfoDto(1L, postId, member.getId(), "새로 만든 댓글");
        doReturn(commentInfoDto).when(commentService).saveComment(any(), any());
        UpdateCommentRequest updateCommentRequest = createUpdateCommentRequest(1L, null, postId, member.getId(), "수정한 댓글");
        CommentInfoDto updatedCommentInfoDto = createCommentInfoDto(1L, postId, member.getId(), updateCommentRequest.getContent());
        doReturn(updatedCommentInfoDto).when(commentService).updateComment(any(), any());

        //then
        mockMvc.perform(patch("/post/{postId}/comments/{commentId}", 1L, 1L)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updateCommentRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(updatedCommentInfoDto.getContent()))
                .andDo(document("comment/update/success", requestFields(
                        fieldWithPath("parentId").description("id value of parent comment").type(JsonFieldType.NUMBER).optional(),
                        fieldWithPath("content").description("updated content value of comment").type(JsonFieldType.STRING),
                        fieldWithPath("authorId").description("id value of author").type(JsonFieldType.NUMBER),
                        fieldWithPath("postId").description("id value of post").type(JsonFieldType.NUMBER),
                        fieldWithPath("id").description("id value of comment").type(JsonFieldType.NUMBER)
                ), responseFields(
                        fieldWithPath("parentCommentId").description("Id value of parent comment").type(JsonFieldType.NUMBER),
                        fieldWithPath("createdTime").description("created time value of comment").type(JsonFieldType.STRING),
                        fieldWithPath("memberNickname").description("nickname value of member").type(JsonFieldType.STRING),
                        fieldWithPath("imageUrl").description("image url value of member").type(JsonFieldType.STRING),
                        fieldWithPath("depth").description("depth value of comment").type(JsonFieldType.NUMBER),
                        fieldWithPath("memberId").description("Id value of member").type(JsonFieldType.NUMBER),
                        fieldWithPath("content").description("content of comment").type(JsonFieldType.STRING),
                        fieldWithPath("postId").description("Id value of post").type(JsonFieldType.NUMBER),
                        fieldWithPath("id").description("Id value of comment").type(JsonFieldType.NUMBER)
                )));
    }

    @Test
    @DisplayName("댓글 삭제 테스트")
    @WithMockCustomUser
    public void deleteCommentTestForSuccess() throws Exception {
        //given
        doReturn(1L).when(postService).savePost(any(), any());
        Member member = createMember();
        PostSaveRequest postSaveRequest = createPostSaveRequest(member, false, "product", "wish");
        Long postId = postService.savePost(postSaveRequest, member.getId());

        //when
        CommentInfoDto commentInfoDto = createCommentInfoDto(1L, postId, member.getId(), "새로 만든 댓글");
        doReturn(commentInfoDto).when(commentService).saveComment(any(), any());
        DeleteCommentRequest deleteCommentRequest = createDeleteCommentRequest(1L, member.getId(), postId);
        doReturn("DELETE").when(commentService).deleteComment(any(), any());

        //then
        mockMvc.perform(delete("/post/{postId}/comments/{commentId}", 1L, 1L)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(deleteCommentRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("comment/delete/success", requestFields(
                        fieldWithPath("authorId").description("Id value of author").type(JsonFieldType.NUMBER),
                        fieldWithPath("postId").description("Id value of post").type(JsonFieldType.NUMBER),
                        fieldWithPath("id").description("Id value of comment").type(JsonFieldType.NUMBER)
                )));
    }
}
