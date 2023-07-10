package f3f.dev1.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import f3f.dev1.domain.address.model.Address;
import f3f.dev1.domain.comment.dto.CommentDTO;
import f3f.dev1.domain.member.dto.MemberDTO;
import f3f.dev1.domain.member.model.Member;
import f3f.dev1.domain.model.TradeStatus;
import f3f.dev1.domain.post.api.PostController;
import f3f.dev1.domain.post.application.PostService;
import f3f.dev1.global.common.annotation.WithMockCustomUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static f3f.dev1.domain.member.dto.MemberDTO.GetUserPost;
import static f3f.dev1.domain.member.dto.MemberDTO.SignUpRequest;
import static f3f.dev1.domain.member.model.UserLoginType.EMAIL;
import static f3f.dev1.domain.post.dto.PostDTO.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

@WebMvcTest(PostController.class)
@MockBean(JpaMetamodelMappingContext.class)
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class PostControllerTest {

    @MockBean
    private PostService postService;

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
                .longitude("127.12170")
                .latitude("37.49455")
                .build();
    }

    // 회원가입 DTO
    public SignUpRequest createSignUpRequest() {
        return SignUpRequest.builder()
                .email("userEmail@email.com")
                .phoneNumber("01012345678")
                .userName("username")
                .nickname("nickname")
                .password("password")
                .userLoginType(EMAIL)
                .birthDate("990128")
                .build();
    }

    public Member createMember() {
        SignUpRequest signUpRequest = createSignUpRequest();
        Member member = Member.builder()
                .phoneNumber(signUpRequest.getPhoneNumber())
                .birthDate(signUpRequest.getBirthDate())
                .password(signUpRequest.getPassword())
                .nickname(signUpRequest.getNickname())
                .email(signUpRequest.getEmail())
                .id(1L)
                .build();
        return member;
    }

    public UpdatePostRequest createUpdatePostRequest() {
        return UpdatePostRequest.builder()
                .content("게시글 수정 테스트 본문")
                .tagNames(new ArrayList<>())
                .title("게시글 수정 테스트 제목")
                .images(new ArrayList<>())
                .tradeEachOther(false)
                .productCategory("")
                .wishCategory("")
                .thumbnail(null)
                .price(30000L)
                .authorId(1L)
                .build();
    }

    public PostSaveRequest createPostSaveRequest(Member author, boolean tradeEachOther) {
        return PostSaveRequest.builder()
                .content("게시글 생성 api 테스트 본문")
                .title("게시글 생성 api 테스트 제목")
                .tradeEachOther(tradeEachOther)
                .tagNames(new ArrayList<>())
                .images(new ArrayList<>())
                .authorId(author.getId())
                .productCategory("")
                .wishCategory("")
                .price(3000L)
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
        mockMvc.perform(post("/post")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(postSaveRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document("post/create/successful",requestFields(
                        fieldWithPath("productCategory").description("productCategory name value of post").type(JsonFieldType.STRING),
                        fieldWithPath("thumbnail").description("thumbnail image of post").type(JsonFieldType.STRING).optional(),
                        fieldWithPath("tradeEachOther").description("tradeEachOther value of post").type(JsonFieldType.BOOLEAN),
                        fieldWithPath("wishCategory").description("wishCategory name value of post").type(JsonFieldType.STRING),
                        fieldWithPath("tagNames").description("tag names list value of post").type(JsonFieldType.ARRAY),
                        fieldWithPath("authorId").description("author id value of post").type(JsonFieldType.NUMBER),
                        fieldWithPath("images").description("image list value of post").type(JsonFieldType.ARRAY),
                        fieldWithPath("content").description("content value of post").type(JsonFieldType.STRING),
                        fieldWithPath("price").description("price value of post").type(JsonFieldType.NUMBER),
                        fieldWithPath("title").description("title value of post").type(JsonFieldType.STRING)
                )));
    }


    @Test
    @DisplayName("게시글 전체 조회 테스트")
    public void getAllPostInfoSuccessTest() throws Exception {
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
        List<String> tagNames = new ArrayList<>();
        tagNames.add("스팸");
        tagNames.add("키보드");

        Pageable pageable = Pageable.ofSize(10).withPage(0);
        List<PostSearchResponseDto> list = new ArrayList<>();
        PostSearchResponseDto dto = PostSearchResponseDto.builder()
                .createdTime(String.valueOf(LocalDateTime.now()))
                .productCategory("테스트용 프로덕트 카테고리")
                .content("테스트용 게시글 검색 결과 본문")
                .title("테스트용 게시글 검색 결과 제목")
                .wishCategory("테스트용 위시 카테고리")
                .authorNickname("Owen")
                .messageRoomCount(0L)
                .isScrap(false)
                .scrapCount(0L)
                .thumbnail("")
                .price(3000L)
                .id(1L)
                .build();
        list.add(dto);
        Page<PostSearchResponseDto> page = new PageImpl<>(list, pageable, list.size());

        doReturn(page).when(postService).findPostsWithTagNameList(any(), any(), any(), any());

        //when
        mockMvc.perform(get("/post/tagSearch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(page))
                .param("tags", "스팸", "키보드")
                .param("trade", "2")
                .param("page", "0")
                .param("size", "5"))
                .andExpect(status().isOk())
                .andDo(document("post/search/tag/successful", requestParameters(
                        parameterWithName("tags").description("tag name list for post search"),
                        parameterWithName("trade").description("trade value for search, 1 - tradable, 2 - trading, 3 - traded"),
                        parameterWithName("size").description("size value required by pageable"),
                        parameterWithName("page").description("page value required by pageable")
                ), responseFields(
                        fieldWithPath("pageable.sort.empty").description("is there isn't any sorting information in this page?").type(JsonFieldType.BOOLEAN),
                        fieldWithPath("content[].messageRoomCount").description("the number of message rooms of searched post").type(JsonFieldType.NUMBER),
                        fieldWithPath("content[].productCategory").description("product category value of searched post").type(JsonFieldType.STRING),
                        fieldWithPath("content[].thumbnail").description("thumbnail value of searched post").type(JsonFieldType.STRING).optional(),
                        fieldWithPath("content[].authorNickname").description("author nick name of searched post").type(JsonFieldType.STRING),
                        fieldWithPath("content[].scrapCount").description("the number of scrap of searched post").type(JsonFieldType.NUMBER),
                        fieldWithPath("content[].createdTime").description("created time value of searched post").type(JsonFieldType.STRING),
                        fieldWithPath("content[].wishCategory").description("wish category of searched post").type(JsonFieldType.STRING),
                        fieldWithPath("pageable.sort.unsorted").description("is this page is not sorted?").type(JsonFieldType.BOOLEAN),
                        fieldWithPath("sort.empty").description("is there isn't any sorting information?").type(JsonFieldType.BOOLEAN),
                        fieldWithPath("numberOfElements").description("number of elements in current page").type(JsonFieldType.NUMBER),
                        fieldWithPath("content[].content").description("content value of searched post").type(JsonFieldType.STRING),
                        fieldWithPath("size").description("the number of elements that shown in 1 page").type(JsonFieldType.NUMBER),
                        fieldWithPath("pageable.sort.sorted").description("is this page is sorted?").type(JsonFieldType.BOOLEAN),
                        fieldWithPath("content[].title").description("title value of searched post").type(JsonFieldType.STRING),
                        fieldWithPath("content[].price").description("price value of searched post").type(JsonFieldType.NUMBER),
                        fieldWithPath("pageable.unpaged").description("is pagination not applied?").type(JsonFieldType.BOOLEAN),
                        fieldWithPath("totalElements").description("the number of total elements").type(JsonFieldType.NUMBER),
                        fieldWithPath("content[].scrap").description("is user scrap this post?").type(JsonFieldType.BOOLEAN),
                        fieldWithPath("pageable.pageNumber").description("current page number").type(JsonFieldType.NUMBER),
                        fieldWithPath("content[].id").description("id value of searched post").type(JsonFieldType.NUMBER),
                        fieldWithPath("pageable.paged").description("is pagination applied?").type(JsonFieldType.BOOLEAN),
                        fieldWithPath("sort.unsorted").description("is sorting not applied?").type(JsonFieldType.BOOLEAN),
                        fieldWithPath("totalPages").description("the number of total pages").type(JsonFieldType.NUMBER),
                        fieldWithPath("pageable.pageSize").description("the size of page").type(JsonFieldType.NUMBER),
                        fieldWithPath("sort.sorted").description("is sorting applied?").type(JsonFieldType.BOOLEAN),
                        fieldWithPath("empty").description("is this page is empty?").type(JsonFieldType.BOOLEAN),
                        fieldWithPath("first").description("is this page is first?").type(JsonFieldType.BOOLEAN),
                        fieldWithPath("pageable.offset").description("page offset").type(JsonFieldType.NUMBER),
                        fieldWithPath("last").description("is this page is last?").type(JsonFieldType.BOOLEAN),
                        fieldWithPath("number").description("current page number").type(JsonFieldType.NUMBER)
                )));
    }

    @Test
    @DisplayName("사용자 아이디로 게시글 찾기(프로필 조회에서 사용)")
    @WithMockCustomUser
    public void getPostsWithMemberIdTestForSuccess() throws Exception {
        //given
        Pageable pageable = Pageable.ofSize(10).withPage(0);
        List<GetUserPost> list = new ArrayList<>();
        GetUserPost dto = GetUserPost.builder()
                .likeCount(3L)
                .postId(1L)
                .thumbNail("")
                .tradeStatus(String.valueOf(TradeStatus.TRADABLE))
                .wishCategory("테스트용 위시 카테고리")
                .title("테스트용 게시글 제목")
                .build();
        list.add(dto);
        Page<GetUserPost> page = new PageImpl<>(list, pageable, list.size());
        doReturn(page).when(postService).findPostByAuthorId(any(), any());

        //when & then
        mockMvc.perform(get("/post/user/{memberId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(page))
                .param("tags", "스팸", "키보드")
                .param("trade", "1")
                .param("page", "0")
                .param("size", "5"))
                .andExpect(status().isOk())
                .andDo(document("post/search/memberId/successful", requestParameters(
                        parameterWithName("tags").description("tag name list for post search"),
                        parameterWithName("trade").description("trade value for search, 1 - tradable, 2 - trading, 3 - traded"),
                        parameterWithName("size").description("size value required by pageable"),
                        parameterWithName("page").description("page value required by pageable")
                ), responseFields(
                        fieldWithPath("pageable.sort.empty").description("is there isn't any sorting information in this page?").type(JsonFieldType.BOOLEAN),
                        fieldWithPath("content[].tradeStatus").description("the trade status value of searched post").type(JsonFieldType.STRING),
                        fieldWithPath("content[].likeCount").description("the number of like of searched post").type(JsonFieldType.NUMBER),
                        fieldWithPath("content[].wishCategory").description("wish category of searched post").type(JsonFieldType.STRING),
                        fieldWithPath("content[].thumbNail").description("thumbnail value of searched post").type(JsonFieldType.STRING),
                        fieldWithPath("pageable.sort.unsorted").description("is this page is not sorted?").type(JsonFieldType.BOOLEAN),
                        fieldWithPath("sort.empty").description("is there isn't any sorting information?").type(JsonFieldType.BOOLEAN),
                        fieldWithPath("numberOfElements").description("number of elements in current page").type(JsonFieldType.NUMBER),
                        fieldWithPath("size").description("the number of elements that shown in 1 page").type(JsonFieldType.NUMBER),
                        fieldWithPath("pageable.sort.sorted").description("is this page is sorted?").type(JsonFieldType.BOOLEAN),
                        fieldWithPath("pageable.unpaged").description("is pagination not applied?").type(JsonFieldType.BOOLEAN),
                        fieldWithPath("content[].title").description("title value of searched post").type(JsonFieldType.STRING),
                        fieldWithPath("content[].postId").description("id value of searched post").type(JsonFieldType.NUMBER),
                        fieldWithPath("totalElements").description("the number of total elements").type(JsonFieldType.NUMBER),
                        fieldWithPath("pageable.pageNumber").description("current page number").type(JsonFieldType.NUMBER),
                        fieldWithPath("sort.unsorted").description("is sorting not applied?").type(JsonFieldType.BOOLEAN),
                        fieldWithPath("pageable.paged").description("is pagination applied?").type(JsonFieldType.BOOLEAN),
                        fieldWithPath("totalPages").description("the number of total pages").type(JsonFieldType.NUMBER),
                        fieldWithPath("pageable.pageSize").description("the size of page").type(JsonFieldType.NUMBER),
                        fieldWithPath("sort.sorted").description("is sorting applied?").type(JsonFieldType.BOOLEAN),
                        fieldWithPath("empty").description("is this page is empty?").type(JsonFieldType.BOOLEAN),
                        fieldWithPath("first").description("is this page is first?").type(JsonFieldType.BOOLEAN),
                        fieldWithPath("pageable.offset").description("page offset").type(JsonFieldType.NUMBER),
                        fieldWithPath("last").description("is this page is last?").type(JsonFieldType.BOOLEAN),
                        fieldWithPath("number").description("current page number").type(JsonFieldType.NUMBER)
                )));

    }


    @Test
    @DisplayName("패치 매핑으로 게시글 수정하기")
    @WithMockCustomUser
    public void updatePostWithPatchTest() throws Exception {

        // given
        // updatePostWithPatch는 반환 타입이 없기 때문에 doReturn을 지정해줄 수 없다.
        UpdatePostRequest updatePostRequest = createUpdatePostRequest();

        // when & then
        mockMvc.perform(patch("/post" + "/{postId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatePostRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("post/update/successful", requestFields(
                        fieldWithPath("tradeEachOther").description("the value that indicates the trade is inter-tradable or not").type(JsonFieldType.BOOLEAN),
                        fieldWithPath("thumbnail").description("the thumbnail value of changed post").type(JsonFieldType.STRING).optional(),
                        fieldWithPath("productCategory").description("product category name value of post").type(JsonFieldType.STRING),
                        fieldWithPath("wishCategory").description("wish category name value of post").type(JsonFieldType.STRING),
                        fieldWithPath("images").description("image values that wants to be changed").type(JsonFieldType.ARRAY),
                        fieldWithPath("authorId").description("Id value of author (requester)").type(JsonFieldType.NUMBER),
                        fieldWithPath("tagNames").description("list values of tag names").type(JsonFieldType.ARRAY),
                        fieldWithPath("content").description("content value of post").type(JsonFieldType.STRING),
                        fieldWithPath("title").description("title value of post").type(JsonFieldType.STRING),
                        fieldWithPath("price").description("price value of post").type(JsonFieldType.NUMBER)
                )));
    }


    @Test
    @DisplayName("게시글 아이디로 특정 게시글 조회 테스트")
    public void getPostInfoByPostIdTestSuccess() throws Exception {
        //given
        Member member = new Member();
        PostSaveRequest postSaveRequest = createPostSaveRequest(member, false);
        SinglePostInfoDto postInfoDto = SinglePostInfoDto
                .builder()
                .id(100L)
                .title(postSaveRequest.getTitle())
                .commentInfoDtoList(new ArrayList<>())
                .content("test")
                .tradeStatus(TradeStatus.TRADABLE)
                .createdTime(LocalDateTime.now())
                .userInfoWithAddress(new MemberDTO.UserInfoWithAddress())
                .images(new ArrayList<>())
                .isScrap(false)
                .messageRoomCount(3L)
                .scrapCount(3L)
                .tradeEachOther(false)
                .tagNames(new ArrayList<>())
                .price(30000L)
                .productCategory("test product")
                .wishCategory("wish product")
                .build();
        postService.savePost(postSaveRequest, member.getId());
        doReturn(postInfoDto).when(postService).findPostById(any(), any());

        //when & then
        mockMvc.perform(get("/post/{postId}", 1L)
        .contentType(MediaType.APPLICATION_JSON)
        .content(""))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("post/search/id/successful", responseFields(
                        fieldWithPath("scrap").description("boolean value of whether post is scraped or not").type(JsonFieldType.BOOLEAN),
                        fieldWithPath("commentInfoDtoList").description("commentInfo DTO list value of post").type(JsonFieldType.ARRAY),
                        fieldWithPath("productCategory").description("productCategory name value of post").type(JsonFieldType.STRING),
                        fieldWithPath("messageRoomCount").description("message room count value of post").type(JsonFieldType.NUMBER),
                        fieldWithPath("wishCategory").description("wishCategory name value of post").type(JsonFieldType.STRING),
                        fieldWithPath("tradeEachOther").description("tradeEachOther value of post").type(JsonFieldType.BOOLEAN),
                        fieldWithPath("createdTime").description("created time value of post").type(JsonFieldType.STRING),
                        fieldWithPath("tradeStatus").description("tradeStatus value of post").type(JsonFieldType.STRING),
                        fieldWithPath("scrapCount").description("scrap count value of post").type(JsonFieldType.NUMBER),
                        fieldWithPath("images").description("image values of searched post").type(JsonFieldType.ARRAY),
                        fieldWithPath("tagNames").description("tag name list value of post").type(JsonFieldType.ARRAY),
                        fieldWithPath("title").description("title name value of post").type(JsonFieldType.STRING),
                        fieldWithPath("content").description("content value of post").type(JsonFieldType.STRING),
                        subsectionWithPath("userInfoWithAddress").description("userInfoWithAddress details"),
                        fieldWithPath("price").description("price value of post").type(JsonFieldType.NUMBER),
                        fieldWithPath("id").description("Id value of post").type(JsonFieldType.NUMBER)
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
                .andExpect(status().isOk())
                .andDo(document("post/delete/succssful", requestFields(
                        fieldWithPath("authorId").description("delete requester Id value").type(JsonFieldType.NUMBER),
                        fieldWithPath("id").description("postId value of post").type(JsonFieldType.NUMBER)
                )));
    }

}
