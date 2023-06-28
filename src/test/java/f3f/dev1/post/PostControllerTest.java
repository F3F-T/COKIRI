package f3f.dev1.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import f3f.dev1.domain.member.application.AuthService;
import f3f.dev1.domain.member.application.MemberService;
import f3f.dev1.domain.member.dao.MemberRepository;
import f3f.dev1.domain.member.model.Member;
import f3f.dev1.domain.address.model.Address;
import f3f.dev1.domain.model.TradeStatus;
import f3f.dev1.domain.post.api.PostController;
import f3f.dev1.domain.post.application.PostService;
import f3f.dev1.domain.tag.application.PostTagService;
import f3f.dev1.domain.tag.application.TagService;
import f3f.dev1.global.common.annotation.WithMockCustomUser;
import f3f.dev1.global.util.SecurityUtil;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
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
                .title("게시글 수정 테스트 제목")
                .content("게시글 수정 테스트 본문")
                .productCategory(null)
                .wishCategory(null)
                .build();
    }

    public PostSaveRequest createPostSaveRequest(Member author, boolean tradeEachOther) {
        return PostSaveRequest.builder()
                .content("게시글 생성 api 테스트 본문")
                .title("게시글 생성 api 테스트 제목")
                .tradeEachOther(tradeEachOther)
                .authorId(author.getId())
                .price(3000L)
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
                        fieldWithPath("thumbnail").description("thumbnail image of post"),
                        fieldWithPath("authorId").description("author id value of post"),
                        fieldWithPath("images").description("image list value of post"),
                        fieldWithPath("content").description("content value of post"),
                        fieldWithPath("price").description("price value of post"),
                        fieldWithPath("title").description("title value of post")
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
                .authorNickname("Owen")
                .createdTime(String.valueOf(LocalDateTime.now()))
                .isScrap(false)
                .messageRoomCount(0L)
                .thumbnail("")
                .scrapCount(0L)
                .title("테스트용 게시글 검색 결과 제목")
                .content("테스트용 게시글 검색 결과 본문")
                .id(1L)
                .price(3000L)
                .productCategory("테스트용 프로덕트 카테고리")
                .wishCategory("테스트용 위시 카테고리")
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
                .andDo(document("post/tagSearch/successful", requestParameters(
                        parameterWithName("tags").description("tag name list for post search"),
                        parameterWithName("trade").description("trade value for search, 1 - tradable, 2 - trading, 3 - traded"),
                        parameterWithName("size").description("size value required by pageable"),
                        parameterWithName("page").description("page value required by pageable")
                ), responseFields(
                        fieldWithPath("content[].id").description("id value of searched post"),
                        fieldWithPath("content[].title").description("title value of searched post"),
                        fieldWithPath("content[].content").description("content value of searched post"),
                        fieldWithPath("content[].thumbnail").description("thumbnail value of searched post"),
                        fieldWithPath("content[].authorNickname").description("author nick name of searched post"),
                        fieldWithPath("content[].productCategory").description("product category value of searched post"),
                        fieldWithPath("content[].createdTime").description("created time value of searched post"),
                        fieldWithPath("content[].messageRoomCount").description("the number of message rooms of searched post"),
                        fieldWithPath("content[].wishCategory").description("wish category of searched post"),
                        fieldWithPath("content[].scrapCount").description("the number of scrap of searched post"),
                        fieldWithPath("content[].price").description("price value of searched post"),
                        fieldWithPath("content[].scrap").description("is user scrap this post?"),
                        fieldWithPath("pageable.pageNumber").description("current page number"),
                        fieldWithPath("pageable.offset").description("page offset"),
                        fieldWithPath("pageable.paged").description("is pagination applied?"),
                        fieldWithPath("pageable.unpaged").description("is pagination not applied?"),
                        fieldWithPath("pageable.pageSize").description("the size of page"),
                        fieldWithPath("totalElements").description("the number of total elements"),
                        fieldWithPath("numberOfElements").description("number of elements in current page"),
                        fieldWithPath("sort.sorted").description("is sorting applied?"),
                        fieldWithPath("sort.unsorted").description("is sorting not applied?"),
                        fieldWithPath("totalPages").description("the number of total pages"),
                        fieldWithPath("first").description("is this page is first?"),
                        fieldWithPath("last").description("is this page is last?"),
                        fieldWithPath("empty").description("is this page is empty?"),
                        fieldWithPath("pageable.sort.sorted").description("is this page is sorted?"),
                        fieldWithPath("pageable.sort.unsorted").description("is this page is not sorted?"),
                        fieldWithPath("pageable.sort.empty").description("is there isn't any sorting information in this page?"),
                        fieldWithPath("sort.empty").description("is there isn't any sorting information?"),
                        fieldWithPath("number").description("current page number"),
                        fieldWithPath("size").description("the number of elements that shown in 1 page")
                )));

        //then
    }
    public ResponseEntity<Page<PostSearchResponseDto>> getPostsWithTagNames(
            @RequestParam(value = "tags", required = false, defaultValue = "") List<String> tagNames,
            @RequestParam(value="trade", required = true, defaultValue = "1") long trade,
            Pageable pageable) {
        Page<PostSearchResponseDto> resultList;
        TradeStatus tradeStatus = TradeStatus.findById(trade);
        Long currentMemberId = SecurityUtil.getCurrentNullableMemberId();
        if(!tagNames.isEmpty()) {
            resultList = postService.findPostsWithTagNameList(tagNames, currentMemberId, tradeStatus, pageable);
        } else {
            // TODO 태그 검색은 동적 쿼리로 작성을 못해서 분기를 나눴다. findAll에서 tradeStatus를 고려해주게 코드를 바꿔주면 될 것 같다.
//            resultList = postService.findAll(currentMemberId, pageable);
            resultList = postService.findOnlyWithTradeStatus(currentMemberId, tradeStatus, pageable);
        }
        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }


    @Test
    @DisplayName("게시글 수정 테스트")
    @WithMockCustomUser
    public void updatePostSuccessTest() throws Exception {
        //given
        PostInfoDtoWithTag postInfoDto = PostInfoDtoWithTag.builder().title("테스트용 게시글").build();
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
//                .andExpect(jsonPath("$.title").value(updatePostRequest.getTitle()))
                .andDo(document("post/update/successful", requestFields(
                        fieldWithPath("authorId").description("Id value of author (requester)"),
                        fieldWithPath("title").description("title value of post"),
                        fieldWithPath("content").description("content value of post"),
                        fieldWithPath("price").description("price value of post"),
                        fieldWithPath("productCategory").description("product category name value of post"),
                        fieldWithPath("wishCategory").description("wish category name value of post"),
                        fieldWithPath("tagNames").description("list values of tag names"),
                        fieldWithPath("tradeEachOther").description("the value that indicates the trade is inter-tradable or not"),
                        fieldWithPath("images").description("image values that wants to be changed"),
                        fieldWithPath("thumbnail").description("the thumbnail value of changed post")
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
        doReturn(postInfoDto).when(postService).findPostById(any(), any());

        //when & then
        mockMvc.perform(get("/post/{postId}", 1L)
        .contentType(MediaType.APPLICATION_JSON)
        .content(""))
                .andDo(print())
                .andExpect(status().isOk())
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
                        fieldWithPath("commentInfoDtoList").description("commentInfo DTO list value of post"),
                        fieldWithPath("title").description("title name value of post"),
                        fieldWithPath("tradeStatus").description("tradeStatus value of post"),
                        fieldWithPath("tagNames").description("tag name list value of post"),
                        fieldWithPath("userInfoWithAddress").description("DTO class that contains UserDetails and Address List"),
                        fieldWithPath("images").description("image values of searched post"),
                        fieldWithPath("scrap").description("boolean value of whether post is scraped or not")
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
                        fieldWithPath("id").description("postId value of post"),
                        fieldWithPath("authorId").description("delete requester Id value")
                )));
    }

}
