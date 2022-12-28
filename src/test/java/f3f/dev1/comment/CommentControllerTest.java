package f3f.dev1.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import f3f.dev1.domain.comment.api.CommentController;
import f3f.dev1.domain.comment.application.CommentService;
import f3f.dev1.domain.comment.dto.CommentDTO;
import f3f.dev1.domain.member.dto.MemberDTO;
import f3f.dev1.domain.member.model.Member;
import f3f.dev1.domain.model.Address;
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

import java.util.ArrayList;
import java.util.List;

import static f3f.dev1.domain.comment.dto.CommentDTO.*;
import static f3f.dev1.domain.member.model.UserLoginType.EMAIL;
import static f3f.dev1.domain.post.dto.PostDTO.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
                .address(createAddress())
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
                .address(signUpRequest.getAddress())
                .email(signUpRequest.getEmail())
                .id(1L)
                .build();
        return member;
    }

    public PostSaveRequest createPostSaveRequest(Member author, boolean tradeEachOther) {
        return PostSaveRequest.builder()
                .content("새 제품이랑 교환 희망하시는 분 계시면 좋겠습니다.")
                .title("12번 사용한 마스크 교환해요~")
                .tradeEachOther(tradeEachOther)
                .authorId(author.getId())
                .productCategory(null)
                .wishCategory(null)
                .build();
    }

    public PostSaveRequest createPostSaveRequestWithDynamicAuthorId(Long authorId, boolean tradeEachOther) {
        return PostSaveRequest.builder()
                .content("새 제품이랑 교환 희망하시는 분 계시면 좋겠습니다.")
                .title("12번 사용한 마스크 교환해요~")
                .tradeEachOther(tradeEachOther)
                .authorId(authorId)
                .productCategory(null)
                .wishCategory(null)
                .build();
    }

    public CreateCommentRequest createCommentRequest(Member author) {
        return CreateCommentRequest.builder()
                .authorId(author.getId())
                .postId(1L)
                .depth(1L)
                .content("12번 쓴 마스크면 냄새 안나요..??")
                .parentCommentId(null)
                .build();
    }

    public CommentInfoDto createCommentInfoDto(Long commentId, Long postId, Long memberId, String content) {
        return CommentInfoDto.builder()
                .id(commentId)
                .postId(postId)
                .memberId(memberId)
                .depth(1L)
                .content(content)
                .parentCommentId(1L)
                .build();
    }

    @Test
    @DisplayName("댓글 생성 테스트")
    @WithMockCustomUser
    public void createCommentTestForSuccess() throws Exception {
        //given
        doReturn(1L).when(postService).savePost(any());
        Member member = createMember();
        PostSaveRequest postSaveRequest = createPostSaveRequest(member, false);
        Long postId = postService.savePost(postSaveRequest);

        CreateCommentRequest commentRequest = createCommentRequest(member);
        doReturn(null).when(commentService).createComment(any());
        postService.savePost(postSaveRequest);

        //when & then
        mockMvc.perform(post("/post/{postId}/comments", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentRequest)))
                .andDo(print())
                .andExpect(status().isCreated());

    }

    @Test
    @DisplayName("댓글 조회 테스트 성공")
    public void findCommentsByPostIdTestForSuccess() throws Exception {
        //given
        doReturn(1L).when(postService).savePost(any());
        Member member = createMember();
        PostSaveRequest postSaveRequest = createPostSaveRequest(member, false);
        Long postId = postService.savePost(postSaveRequest);

        // when
        CreateCommentRequest commentRequest = createCommentRequest(member);
        List<CommentInfoDto> commentInfoDtoList = new ArrayList<>();
        CommentInfoDto commentInfoDto = createCommentInfoDto(1L, postId, member.getId(), "댓글1");
        commentInfoDtoList.add(commentInfoDto);
        doReturn(commentInfoDtoList).when(commentService).findCommentsByPostId(any());
        postService.savePost(postSaveRequest);

        //then
        mockMvc.perform(get("/post/{postId}/comments", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].content").value("댓글1"));
    }

    @Test
    @DisplayName("댓글 조회 테스트 실패 - 없는 포스트에 대한 댓글 조회 요청")
    public void findCommentsByPostIdTestForFail() throws Exception {
        //given
        doReturn(1L).when(postService).savePost(any());
        Member member = createMember();
        PostSaveRequest postSaveRequest = createPostSaveRequest(member, false);
        Long postId = postService.savePost(postSaveRequest);

        // when
        CreateCommentRequest commentRequest = createCommentRequest(member);
        List<CommentInfoDto> commentInfoDtoList = new ArrayList<>();
        CommentInfoDto commentInfoDto = createCommentInfoDto(1L, postId, member.getId(), "댓글1");
        commentInfoDtoList.add(commentInfoDto);
        doReturn(commentInfoDtoList).when(commentService).findCommentsByPostId(any());
        postService.savePost(postSaveRequest);

        //then
        mockMvc.perform(get("/post/{postId}/comments", 30L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0]").isEmpty());
//                .andExpect(jsonPath());
    }

}
