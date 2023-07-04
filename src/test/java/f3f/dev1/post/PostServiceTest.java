package f3f.dev1.post;

import f3f.dev1.domain.category.application.CategoryService;
import f3f.dev1.domain.category.dao.CategoryRepository;
import f3f.dev1.domain.category.model.Category;
import f3f.dev1.domain.comment.dao.CommentRepository;
import f3f.dev1.domain.member.application.AuthService;
import f3f.dev1.domain.member.dao.MemberRepository;
import f3f.dev1.domain.member.model.Member;
import f3f.dev1.domain.address.model.Address;
import f3f.dev1.domain.model.TradeStatus;
import f3f.dev1.domain.post.application.PostService;
import f3f.dev1.domain.post.dao.PostRepository;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.scrap.application.ScrapService;
import f3f.dev1.domain.tag.application.PostTagService;
import f3f.dev1.domain.tag.application.TagService;
import f3f.dev1.domain.tag.dao.PostTagRepository;
import f3f.dev1.domain.tag.dao.TagRepository;
import f3f.dev1.domain.tag.model.PostTag;
import f3f.dev1.domain.tag.model.Tag;
import f3f.dev1.global.common.annotation.WithMockCustomUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static f3f.dev1.domain.category.dto.CategoryDTO.*;
import static f3f.dev1.domain.member.dto.MemberDTO.*;
import static f3f.dev1.domain.member.model.UserLoginType.EMAIL;
import static f3f.dev1.domain.post.dto.PostDTO.*;
import static f3f.dev1.domain.scrap.dto.ScrapDTO.*;
import static f3f.dev1.domain.tag.dto.TagDTO.*;
import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
public class PostServiceTest {

    @Autowired
    PostService postService;

    @Autowired
    AuthService authService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    TagService tagService;

    @Autowired
    PostTagService postTagService;

    @Autowired
    ScrapService scrapService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    PostTagRepository postTagRepository;

    public CreateTagRequest createTagRequest(String tagName, Long authorId) {
        return new CreateTagRequest(tagName, authorId);
    }

    public AddTagToPostRequest createAddTagToPostRequest(Long tagId, Long postId) {
        return new AddTagToPostRequest(tagId, postId);
    }


    public CategorySaveRequest createCategorySaveRequest(String name, Long depth, Long parentId, Member author) {
        return CategorySaveRequest.builder()
                .name(name)
                .depth(depth)
                .parentId(parentId)
                .memberId(author.getId())
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
                .email("oldstyle4@email.com")
                .birthDate("990128")
                .password("password")
                .userLoginType(EMAIL)
                .build();
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

    public PostSaveRequest createPostSaveRequestWithTag(Member author, boolean tradeEachOther, String productName, String wishName, List<String> tagNames) {
        return PostSaveRequest.builder()
                .tradeEachOther(tradeEachOther)
                .productCategory(productName)
                .content("태그 게시글 content")
                .authorId(author.getId())
                .title("태그 게시글 title")
                .wishCategory(wishName)
                .tagNames(tagNames)
                .thumbnail("")
                .price(10000L)
                .build();
    }

    public PostSaveRequest createPostSaveRequestWithTagAndTitle(Member author, String title, boolean tradeEachOther, String productName, String wishName, List<String> tagNames) {
        return PostSaveRequest.builder()
                .tradeEachOther(tradeEachOther)
                .productCategory(productName)
                .content("태그 게시글 content")
                .authorId(author.getId())
                .wishCategory(wishName)
                .tagNames(tagNames)
                .thumbnail("")
                .title(title)
                .build();
    }

    public PostSaveRequest createPostSaveRequestWithDynamicTitle(Member author, String title, boolean tradeEachOther, String productName, String wishName) {
        return PostSaveRequest.builder()
                .tradeEachOther(tradeEachOther)
                .productCategory(productName)
                .tagNames(new ArrayList<>())
                .authorId(author.getId())
                .content("테스트 게시글 본문")
                .wishCategory(wishName)
                .price(10000L)
                .title(title)
                .build();
    }

    public PostSaveRequest createCompletedPostSaveRequest(Member author, String title, String content, boolean tradeEachOther,
                                                          String productName, String wishName, List<String> tagNames, Long price) {
        // 최종 테스트에서 사용될 완성형 포스트 생성 요청 메소드
        return PostSaveRequest.builder()
                .tradeEachOther(tradeEachOther)
                .productCategory(productName)
                .authorId(author.getId())
                .wishCategory(wishName)
                .tagNames(tagNames)
                .content(content)
                .title(title)
                .price(price)
                .build();
    }

    public SearchPostRequest createPostSearchRequest(String productName, String wishName, List<String> tagNames, String minPrice, String maxPrice) {
        return SearchPostRequest.builder()
                .productCategory(productName)
                .wishCategory(wishName)
                .tagNames(tagNames)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .build();
    }

    public SearchPostRequestExcludeTag createSearchPostRequestExcludeTagWithTradable(String productName, String wishName,String minPrice, String maxPrice) {
        return SearchPostRequestExcludeTag.builder()
                .tradeStatus(TradeStatus.TRADABLE)
                .productCategory(productName)
                .wishCategory(wishName)
                .maxPrice(maxPrice)
                .minPrice(minPrice)
                .build();
    }

    public SearchPostRequestExcludeTag createSearchPostRequestExcludeTagWithNotTradable(String productName, String wishName,String minPrice, String maxPrice) {
        return SearchPostRequestExcludeTag.builder()
                .tradeStatus(TradeStatus.TRADED)
                .productCategory(productName)
                .wishCategory(wishName)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .build();
    }


    public DeletePostRequest createDeletePostRequest(Long postId, Long authorId) {
        return new DeletePostRequest(postId, authorId);
    }

    // 업데이트 요청
    public UpdatePostRequest createUpdatePostRequest(Long postId, Long authorId, String title, String content, String productCategoryName, String wishCategoryName, List<String> tagNames) {
        return UpdatePostRequest.builder()
                .productCategory(productCategoryName)
                .wishCategory(wishCategoryName)
                .images(new ArrayList<>())
                .tradeEachOther(false)
                .authorId(authorId)
                .tagNames(tagNames)
                .content(content)
                .price(15000L)
                .thumbnail("")
                .title(title)
                .build();
    }


    // 회원가입 테스트
    // 뒤에서 활용될 유저 생성 관련 테스트 선행
    @Test
    @DisplayName("유저 생성 성공 테스트")
    public void signUpTestSuccess() throws Exception {
        //given
        SignUpRequest signUpRequest = createSignUpRequest();

        // when
        authService.signUp(signUpRequest);
        Optional<Member> byId = memberRepository.findByEmail(signUpRequest.getEmail());
        // then
        assertThat(byId.get().getEmail()).isEqualTo(signUpRequest.getEmail());
    }

    @Test
    @DisplayName("게시글 생성 테스트")
    @WithMockCustomUser
    public void savePostSuccess() throws Exception {
        //given
        SignUpRequest signUpRequest = createSignUpRequest();
        authService.signUp(signUpRequest);
        Member member = memberRepository.findByEmail(signUpRequest.getEmail()).get();

        // 루트 생성
        CategorySaveRequest rootRequest = createCategorySaveRequest("테스트용 루트", 0L, null, member);
        Long rootId = categoryService.createCategory(rootRequest);
        Category root = categoryRepository.findById(rootId).get();
        // product, wish 생성

        CategorySaveRequest productRequest = createCategorySaveRequest("테스트용 카테고리1", 1L, rootId, member);
        CategorySaveRequest wishRequest = createCategorySaveRequest("테스트용 카테고리2", 1L, rootId, member);
        Long productCategoryId = categoryService.createCategory(productRequest);
        Long wishCategoryId = categoryService.createCategory(wishRequest);

        //when
        PostSaveRequest postSaveRequest = createPostSaveRequest(member, false, productRequest.getName(), wishRequest.getName());
        Long postId = postService.savePost(postSaveRequest, member.getId());
        Post post = postRepository.findById(postId).get();

        //then
        assertThat(post.getContent()).isEqualTo(postSaveRequest.getContent());
        assertThat(post.getTitle()).isEqualTo(postSaveRequest.getTitle());
    }

    @Test
    @DisplayName("게시글 생성 - 태그 생성 테스트")
    public void postSaveTestWithTagForSuccess() throws Exception {
        SignUpRequest signUpRequest = createSignUpRequest();
        authService.signUp(signUpRequest);
        Member member = memberRepository.findByEmail(signUpRequest.getEmail()).get();

        // 루트 생성
        CategorySaveRequest rootRequest = createCategorySaveRequest("root", 0L, null, member);
        Long rootId = categoryService.createCategory(rootRequest);
        Category root = categoryRepository.findById(rootId).get();
        // product, wish 생성

        CategorySaveRequest productRequest = createCategorySaveRequest("도서", 1L, rootId, member);
        CategorySaveRequest wishRequest = createCategorySaveRequest("전자기기", 1L, rootId, member);
        Long productCategoryId = categoryService.createCategory(productRequest);
        Long wishCategoryId = categoryService.createCategory(wishRequest);

        CreateTagRequest tagRequest = createTagRequest("해시태그1", member.getId());
        CreateTagRequest secondTagRequest = createTagRequest("해시태그2", member.getId());
        CreateTagRequest thirdTagRequest = createTagRequest("해시태그3", member.getId());
        Long tagId = tagService.createTag(tagRequest);
        Long secondTagId = tagService.createTag(secondTagRequest);
        Long thirdTagId = tagService.createTag(thirdTagRequest);

        //when
        List<String> tagNames = new ArrayList<>();
        tagNames.add(tagRequest.getName());
        tagNames.add(secondTagRequest.getName());
        tagNames.add(thirdTagRequest.getName());
        PostSaveRequest postSaveRequestWithTag = createPostSaveRequestWithTag(member, false, productRequest.getName(), wishRequest.getName(), tagNames);
        Long postId = postService.savePost(postSaveRequestWithTag, member.getId());
        tagService.addTagsToPost(postId, tagNames);

        //then
        Post post = postRepository.findById(postId).get();
        List<PostTag> postTagList = postTagRepository.findByPost(post);
        assertThat(postTagList.size()).isEqualTo(6);
        assertThat(postTagList.get(0).getTag().getName()).isEqualTo(tagRequest.getName());
        assertThat(postTagList.get(1).getTag().getName()).isEqualTo(secondTagRequest.getName());
        assertThat(postTagList.get(2).getTag().getName()).isEqualTo(thirdTagRequest.getName());
    }




    @Test
    @DisplayName("프론트 요구사항 변경 테스트 - PostInfoDtoWithTags return type : 포스트 스크랩 여부 조회 시 표시")
    public void postInfoDtoWithTagsTestForSuccess() throws Exception {
        //given
        SignUpRequest signUpRequest = createSignUpRequest();
        authService.signUp(signUpRequest);
        Member member = memberRepository.findByEmail(signUpRequest.getEmail()).get();

        // 루트 생성
        CategorySaveRequest rootRequest = createCategorySaveRequest("root", 0L, null, member);
        Long rootId = categoryService.createCategory(rootRequest);
        Category root = categoryRepository.findById(rootId).get();
        // product, wish 생성

        CategorySaveRequest productRequest = createCategorySaveRequest("product", 1L, rootId, member);
        CategorySaveRequest wishRequest = createCategorySaveRequest("wish", 1L, rootId, member);
        Long productCategoryId = categoryService.createCategory(productRequest);
        Long wishCategoryId = categoryService.createCategory(wishRequest);

        // 태그 생성
        CreateTagRequest tagRequest1 = createTagRequest("해시태그1", member.getId());
        CreateTagRequest tagRequest2 = createTagRequest("해시태그2", member.getId());
        CreateTagRequest tagRequest3 = createTagRequest("해시태그3", member.getId());
        tagService.createTag(tagRequest1);
        tagService.createTag(tagRequest2);
        tagService.createTag(tagRequest3);

        //when
        List<String> tagNamesToBeAdded = new ArrayList<>();
        tagNamesToBeAdded.add(tagRequest1.getName());
        tagNamesToBeAdded.add(tagRequest2.getName());
        tagNamesToBeAdded.add(tagRequest3.getName());
        PostSaveRequest postSaveRequest = createPostSaveRequestWithTagAndTitle(member, "제목", false, productRequest.getName(), wishRequest.getName(), tagNamesToBeAdded);
        Long postId = postService.savePost(postSaveRequest, member.getId());
        tagService.addTagsToPost(postId, tagNamesToBeAdded);

        //then
        // 컨트롤러에서 사용하는 포스트 서비스 로직을 그대로 사용하여 테스트해보겠음.
        SinglePostInfoDto postInfoDtoWithTag = postService.findPostById(postId, member.getId());
        assertThat(postInfoDtoWithTag.getTitle()).isEqualTo("제목");
        assertThat(postInfoDtoWithTag.getTagNames().size()).isEqualTo(6);
        assertThat(postInfoDtoWithTag.getTagNames().containsAll(tagNamesToBeAdded)).isTrue();
    }



    @Test
    @DisplayName("After QueryDSL - 태그 제외한 검색")
    public void searchPostWithCustomRepository_noTags() throws Exception {
        //given
        SignUpRequest signUpRequest = createSignUpRequest();
        authService.signUp(signUpRequest);
        Member member = memberRepository.findByEmail(signUpRequest.getEmail()).get();
        CreateTagRequest tagRequest = createTagRequest("해시태그1", member.getId());
        CreateTagRequest secondTagRequest = createTagRequest("해시태그2", member.getId());
        CreateTagRequest thirdTagRequest = createTagRequest("해시태그3", member.getId());
        Long tagId = tagService.createTag(tagRequest);
        Long secondTagId = tagService.createTag(secondTagRequest);
        Long thirdTagId = tagService.createTag(thirdTagRequest);

        CategorySaveRequest rootRequest = createCategorySaveRequest("root", 0L, null, member);
        Long rootId = categoryService.createCategory(rootRequest);
        Category root = categoryRepository.findById(rootId).get();
        // product, wish 생성

        CategorySaveRequest productRequest = createCategorySaveRequest("product", 1L, rootId, member);
        CategorySaveRequest wishRequest = createCategorySaveRequest("wish", 1L, rootId, member);
        Long productCategoryId = categoryService.createCategory(productRequest);
        Long wishCategoryId = categoryService.createCategory(wishRequest);

        CategorySaveRequest secondProductRequest = createCategorySaveRequest("product2", 1L, rootId, member);
        CategorySaveRequest secondWishRequest = createCategorySaveRequest("wish2", 1L, rootId, member);
        Long secondProductCategoryId = categoryService.createCategory(secondProductRequest);
        Long secondWishCategoryId = categoryService.createCategory(secondWishRequest);

        //when
        PostSaveRequest firstRequest = createCompletedPostSaveRequest(member, "첫번째 게시글", "첫번째 내용", false, productRequest.getName(), wishRequest.getName(), new ArrayList<>(), 7500L);
        PostSaveRequest secondRequest = createCompletedPostSaveRequest(member, "두번째 게시글", "두번째 내용", false, productRequest.getName(), secondWishRequest.getName(), new ArrayList<>(), 12000L);
        PostSaveRequest thirdRequest = createCompletedPostSaveRequest(member, "세번째 게시글", "세번째 내용", false, secondProductRequest.getName(), secondWishRequest.getName(), new ArrayList<>(), 9000L);
        postService.savePost(firstRequest, member.getId());
        postService.savePost(secondRequest, member.getId());
        postService.savePost(thirdRequest, member.getId());

        // 첫번째, 두번째 게시글 조회돼야함
        SearchPostRequestExcludeTag searchPostRequestExcludeTag = createSearchPostRequestExcludeTagWithTradable(productRequest.getName(), null, null, null);
        // 첫번째 게시글 조회돼야함
        SearchPostRequestExcludeTag searchPostRequestExcludeTag1 = createSearchPostRequestExcludeTagWithTradable(null, wishRequest.getName(), null, null);
        // 두번째 게시글 조회돼야함
        SearchPostRequestExcludeTag searchPostRequestExcludeTag2 = createSearchPostRequestExcludeTagWithTradable(productRequest.getName(), secondWishRequest.getName(), null, null);
        // 두번째 게시글 조회돼야함
        SearchPostRequestExcludeTag searchPostRequestExcludeTag3 = createSearchPostRequestExcludeTagWithTradable(productRequest.getName(), null, "8000", null);
        // 조회되면 안됨
        SearchPostRequestExcludeTag searchPostRequestExcludeTag4 = createSearchPostRequestExcludeTagWithTradable(null, secondWishRequest.getName(), null, "8000");
        // 세번째 게시글 조회돼야함
        SearchPostRequestExcludeTag searchPostRequestExcludeTag5 = createSearchPostRequestExcludeTagWithTradable(null, secondWishRequest.getName(), "5000", "10000");
        // 전체 게시글 조회돼야함
        SearchPostRequestExcludeTag searchPostRequestExcludeTag6 = createSearchPostRequestExcludeTagWithTradable(null, null, null, null);

        //then
        PageRequest pageRequest = PageRequest.of(0, 20);
        Page<PostSearchResponseDto> postsByCategoryAndPriceRange = postService.findPostsByCategoryAndPriceRange(searchPostRequestExcludeTag, member.getId(), pageRequest);
        assertThat(postsByCategoryAndPriceRange).extracting("title").hasSize(2).contains("첫번째 게시글", "두번째 게시글");

        Page<PostSearchResponseDto> postsByCategoryAndPriceRange1 = postService.findPostsByCategoryAndPriceRange(searchPostRequestExcludeTag1, member.getId(), pageRequest);
        assertThat(postsByCategoryAndPriceRange1).extracting("title").hasSize(1).contains("첫번째 게시글");

        Page<PostSearchResponseDto> postsByCategoryAndPriceRange2 = postService.findPostsByCategoryAndPriceRange(searchPostRequestExcludeTag2, member.getId(), pageRequest);
        assertThat(postsByCategoryAndPriceRange2).extracting("title").hasSize(1).contains("두번째 게시글");

        Page<PostSearchResponseDto> postsByCategoryAndPriceRange3 = postService.findPostsByCategoryAndPriceRange(searchPostRequestExcludeTag3, member.getId(), pageRequest);
        assertThat(postsByCategoryAndPriceRange3).extracting("title").hasSize(1).contains("두번째 게시글");

        Page<PostSearchResponseDto> postsByCategoryAndPriceRange4 = postService.findPostsByCategoryAndPriceRange(searchPostRequestExcludeTag4, member.getId(), pageRequest);
        assertThat(postsByCategoryAndPriceRange4).isEmpty();
        Page<PostSearchResponseDto> postsByCategoryAndPriceRange5 = postService.findPostsByCategoryAndPriceRange(searchPostRequestExcludeTag5, member.getId(), pageRequest);
        assertThat(postsByCategoryAndPriceRange5).extracting("title").hasSize(1).contains("세번째 게시글");

        Page<PostSearchResponseDto> postsByCategoryAndPriceRange6 = postService.findPostsByCategoryAndPriceRange(searchPostRequestExcludeTag6, member.getId(), pageRequest);
        assertThat(postsByCategoryAndPriceRange6).extracting("title").hasSize(3).contains("첫번째 게시글", "두번째 게시글", "세번째 게시글");
    }

    @Test
    @DisplayName("After QueryDSL - 조인 후 DTO로 조회")
    public void getPostInfoDtoForGET_PreProcessorTestForSuccess() throws Exception {
        //given
        SignUpRequest signUpRequest = createSignUpRequest();
        authService.signUp(signUpRequest);
        Member member = memberRepository.findByEmail(signUpRequest.getEmail()).get();
        CreateTagRequest tagRequest = createTagRequest("해시태그1", member.getId());
        CreateTagRequest secondTagRequest = createTagRequest("해시태그2", member.getId());
        CreateTagRequest thirdTagRequest = createTagRequest("해시태그3", member.getId());
        Long tagId = tagService.createTag(tagRequest);
        Long secondTagId = tagService.createTag(secondTagRequest);
        Long thirdTagId = tagService.createTag(thirdTagRequest);

        CategorySaveRequest rootRequest = createCategorySaveRequest("root", 0L, null, member);
        Long rootId = categoryService.createCategory(rootRequest);
        Category root = categoryRepository.findById(rootId).get();
        // product, wish 생성

        CategorySaveRequest productRequest = createCategorySaveRequest("product", 1L, rootId, member);
        CategorySaveRequest wishRequest = createCategorySaveRequest("wish", 1L, rootId, member);
        Long productCategoryId = categoryService.createCategory(productRequest);
        Long wishCategoryId = categoryService.createCategory(wishRequest);

        CategorySaveRequest secondProductRequest = createCategorySaveRequest("product2", 1L, rootId, member);
        CategorySaveRequest secondWishRequest = createCategorySaveRequest("wish2", 1L, rootId, member);
        Long secondProductCategoryId = categoryService.createCategory(secondProductRequest);
        Long secondWishCategoryId = categoryService.createCategory(secondWishRequest);

        //when
        PostSaveRequest firstRequest = createCompletedPostSaveRequest(member, "첫번째 게시글", "첫번째 내용", false, productRequest.getName(), wishRequest.getName(), new ArrayList<>(), 7500L);
        PostSaveRequest secondRequest = createCompletedPostSaveRequest(member, "두번째 게시글", "두번째 내용", false, productRequest.getName(), secondWishRequest.getName(), new ArrayList<>(), 12000L);
        PostSaveRequest thirdRequest = createCompletedPostSaveRequest(member, "세번째 게시글", "세번째 내용", false, secondProductRequest.getName(), secondWishRequest.getName(), new ArrayList<>(), 9000L);
        Long firstPostId = postService.savePost(firstRequest, member.getId());
        postService.savePost(secondRequest, member.getId());
        postService.savePost(thirdRequest, member.getId());

        AddScrapPostDTO addScrapPostDTO = AddScrapPostDTO.builder().userId(member.getId()).postId(firstPostId).build();
        scrapService.addScrapPost(addScrapPostDTO, member.getId());

        // 첫번째, 두번째 게시글 조회돼야함
        SearchPostRequestExcludeTag searchPostRequestExcludeTag = createSearchPostRequestExcludeTagWithTradable(productRequest.getName(), null, null, null);

        //then
        PageRequest pageRequest = PageRequest.of(0, 20);
        Page<PostSearchResponseDto> list1 = postService.findPostsByCategoryAndPriceRange(searchPostRequestExcludeTag, member.getId(), pageRequest);
        assertThat(list1).extracting("title").hasSize(2).contains("첫번째 게시글", "두번째 게시글");
        // 첫번째 게시글은 스크랩을 했기 때문에 스크랩 갯수가 1이 나와야 한다.
        // assertThat(list1.getContent().get(0).getScrapCount()).isEqualTo(1L);
    }

    @Test
    @DisplayName("게시글 업데이트 테스트")
    public void updatePostTestForSuccess() throws Exception {
        SignUpRequest signUpRequest = createSignUpRequest();
        authService.signUp(signUpRequest);
        Member member = memberRepository.findByEmail(signUpRequest.getEmail()).get();

        // 루트 생성
        CategorySaveRequest rootRequest = createCategorySaveRequest("root", 0L, null, member);
        Long rootId = categoryService.createCategory(rootRequest);
        Category root = categoryRepository.findById(rootId).get();
        // product, wish 생성

        CategorySaveRequest productRequest = createCategorySaveRequest("product", 1L, rootId, member);
        CategorySaveRequest wishRequest = createCategorySaveRequest("wish", 1L, rootId, member);
        Long productCategoryId = categoryService.createCategory(productRequest);
        Long wishCategoryId = categoryService.createCategory(wishRequest);

        //when
        PostSaveRequest postSaveRequest = createPostSaveRequest(member, false,productRequest.getName(), wishRequest.getName());
        Long postId = postService.savePost(postSaveRequest, member.getId());
        Post post = postRepository.findById(postId).get();
        UpdatePostRequest updatePostRequest = createUpdatePostRequest(postId, member.getId(), "변경한 제목", "변경한 내용", post.getProductCategory().getName(), post.getWishCategory().getName(), new ArrayList<>());
        postService.updatePostWithPatch(updatePostRequest, postId);

        // then
        Post updatedPost = postRepository.findById(postId).get();
        assertThat(updatedPost.getTitle()).isEqualTo(updatedPost.getTitle());
        assertThat(updatedPost.getContent()).isEqualTo(updatedPost.getContent());

        assertThat(updatedPost.getTitle()).isEqualTo(updatePostRequest.getTitle());
        assertThat(updatedPost.getContent()).isEqualTo(updatePostRequest.getContent());
    }

    @Test
    @DisplayName("게시글 업데이트 테스트 - 태그 변경")
    public void updatePostWithDifferentTagsTestForSuccess() throws Exception {
        SignUpRequest signUpRequest = createSignUpRequest();
        authService.signUp(signUpRequest);
        Member member = memberRepository.findByEmail(signUpRequest.getEmail()).get();

        // 루트 생성
        CategorySaveRequest rootRequest = createCategorySaveRequest("root", 0L, null, member);
        Long rootId = categoryService.createCategory(rootRequest);
        Category root = categoryRepository.findById(rootId).get();
        // product, wish 생성

        CategorySaveRequest productRequest = createCategorySaveRequest("product", 1L, rootId, member);
        CategorySaveRequest wishRequest = createCategorySaveRequest("wish", 1L, rootId, member);
        Long productCategoryId = categoryService.createCategory(productRequest);
        Long wishCategoryId = categoryService.createCategory(wishRequest);

        // 태그 생성
        CreateTagRequest tagRequest1 = createTagRequest("해시태그1", member.getId());
        CreateTagRequest tagRequest2 = createTagRequest("해시태그2", member.getId());
        Long tag1 = tagService.createTag(tagRequest1);
        Long tag2 = tagService.createTag(tagRequest2);

        //when
        PostSaveRequest postSaveRequest = createPostSaveRequest(member, false, productRequest.getName(), wishRequest.getName());
        Long postId = postService.savePost(postSaveRequest, member.getId());
        Post post = postRepository.findById(postId).get();
        // 게시글1에 태그1을 추가하겠다. 이 시점에서 게시글1은 태그2에 대한 어떠한 정보(PostTag)도 없다.
        AddTagToPostRequest addTagToPostRequest = createAddTagToPostRequest(tag1, postId);
        tagService.addTagToPost(addTagToPostRequest);
        // 여기서 업데이트 요청에 태그2를 넘기겠다.
        // 이 요청에 실행되면 게시글1은 기존 태그1이 사라지고 (postTag까지 사라져야함) 아무것도 없던 태그2가 PostTag까지 함께 생성되어 가지고 있어야 한다.
        // PostTag에 추가됐는지 확인하는 것이 핵심이다.
        List<String> secondTagNameList = new ArrayList<>();
        secondTagNameList.add("해시태그2");
        UpdatePostRequest updatePostRequest = createUpdatePostRequest(postId, member.getId(),"변경한 제목", "변경한 내용", post.getProductCategory().getName(), post.getWishCategory().getName(), secondTagNameList);
        // 컨트롤러에서는 update 하기 전에 postTagService에서 레포지토리를 삭제한다. 똑같은 환경으로 테스트 하기 위해 여기서도 그렇게 하겠다.
        postTagService.deletePostTagFromPost(postId);
        postService.updatePostWithPatch(updatePostRequest, postId);

        //then
        Post updatedPost = postRepository.findById(postId).get();
        // 이 postTags는 해시태그1이 아닌 해시태그2만을 담고 있어야 한다.
        List<PostTag> postTags = postTagRepository.findByPost(updatedPost);
        assertThat(postTags.size()).isEqualTo(1);
        // 아래의 검증에서 updatedPost는 단 하나의 해시태그만을 가지고 있으며, 그 해시태그의 이름이 "해시태그2"라는 것이 증명되었다.
        List<PostTag> tag2PostTags = postTagRepository.findByTagName("해시태그2");
        assertThat(tag2PostTags.size()).isEqualTo(1);
        assertThat(tag2PostTags.get(0).getId().equals(postTags.get(0).getId()));

        List<PostTag> tag1PostTags = postTagRepository.findByTagName("해시태그1");
        assertThat(tag1PostTags).isEmpty();
    }

    @Test
    @DisplayName("게시글 업데이트 테스트 - 태그 변경 심화")
    public void updatePostWithDifferentTagsDetailedTestForSuccess() throws Exception {
        //given
        SignUpRequest signUpRequest = createSignUpRequest();
        authService.signUp(signUpRequest);
        Member member = memberRepository.findByEmail(signUpRequest.getEmail()).get();

        //when
        // 루트 생성
        CategorySaveRequest rootRequest = createCategorySaveRequest("root", 0L, null, member);
        Long rootId = categoryService.createCategory(rootRequest);
        Category root = categoryRepository.findById(rootId).get();
        // product, wish 생성

        CategorySaveRequest productRequest = createCategorySaveRequest("product", 1L, rootId, member);
        CategorySaveRequest wishRequest = createCategorySaveRequest("wish", 1L, rootId, member);
        Long productCategoryId = categoryService.createCategory(productRequest);
        Long wishCategoryId = categoryService.createCategory(wishRequest);
        // 태그 생성
        CreateTagRequest tagRequest1 = createTagRequest("해시태그1", member.getId());
        CreateTagRequest tagRequest2 = createTagRequest("해시태그2", member.getId());
        CreateTagRequest tagRequest3 = createTagRequest("해시태그3", member.getId());
        tagService.createTag(tagRequest1);
        tagService.createTag(tagRequest2);
        tagService.createTag(tagRequest3);

        List<String> tagNames = new ArrayList<>();
        tagNames.add(tagRequest1.getName());
        tagNames.add(tagRequest2.getName());

        PostSaveRequest postSaveRequest = createPostSaveRequestWithTag(member, false, productRequest.getName(), wishRequest.getName(), tagNames);
        Long postId = postService.savePost(postSaveRequest, member.getId());
        tagService.addTagsToPost(postId, tagNames);
        Post beforeUpdatedPost = postRepository.findById(postId).get();

        // 게시글을 하나 더 만들겠다.
        PostSaveRequest secondPostSaveRequest = createPostSaveRequestWithTagAndTitle(member, "두번째 게시글", false, productRequest.getName(), wishRequest.getName(), tagNames);
        Long secondPostId = postService.savePost(secondPostSaveRequest, member.getId());
        tagService.addTagsToPost(secondPostId, tagNames);

        List<String> updatedTagNames = new ArrayList<>();
        updatedTagNames.add(tagRequest1.getName());
        updatedTagNames.add(tagRequest3.getName());
        UpdatePostRequest updatePostRequest = createUpdatePostRequest(postId, member.getId(),"변경한 제목", "변경한 내용", productRequest.getName(), wishRequest.getName(), updatedTagNames);
        postTagService.deletePostTagFromPost(postId);
        postService.updatePostWithPatch(updatePostRequest, postId);

        //then

        Post updatedPost = postRepository.findById(postId).get();
        List<PostTag> updatedPostTags = postTagRepository.findByPost(updatedPost);
        assertThat(updatedPostTags.size()).isEqualTo(2);

        PostTag postTag1 = postTagRepository.findByTagName("해시태그1").get(0);
        PostTag postTag2 = postTagRepository.findByTagName("해시태그2").get(0);
        PostTag postTag3 = postTagRepository.findByTagName("해시태그3").get(0);

        assertThat(updatedPostTags.get(0).getTag().getName()).isEqualTo("해시태그1");
        assertThat(updatedPostTags.get(1).getTag().getName()).isEqualTo("해시태그3");
        assertThat(updatedPostTags.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("deletePostTagFromPost 테스트")
    public void deletePostTagFromPostTest() throws Exception {
        //given
        SignUpRequest signUpRequest = createSignUpRequest();
        authService.signUp(signUpRequest);
        Member member = memberRepository.findByEmail(signUpRequest.getEmail()).get();

        //when
        // 루트 생성
        CategorySaveRequest rootRequest = createCategorySaveRequest("root", 0L, null, member);
        Long rootId = categoryService.createCategory(rootRequest);
        Category root = categoryRepository.findById(rootId).get();
        // product, wish 생성

        CategorySaveRequest productRequest = createCategorySaveRequest("product", 1L, rootId, member);
        CategorySaveRequest wishRequest = createCategorySaveRequest("wish", 1L, rootId, member);
        Long productCategoryId = categoryService.createCategory(productRequest);
        Long wishCategoryId = categoryService.createCategory(wishRequest);
        // 태그 생성
        CreateTagRequest tagRequest1 = createTagRequest("해시태그1", member.getId());
        CreateTagRequest tagRequest2 = createTagRequest("해시태그2", member.getId());
        CreateTagRequest tagRequest3 = createTagRequest("해시태그3", member.getId());
        tagService.createTag(tagRequest1);
        tagService.createTag(tagRequest2);
        tagService.createTag(tagRequest3);

        List<String> tagNames = new ArrayList<>();
        tagNames.add(tagRequest1.getName());
        tagNames.add(tagRequest2.getName());
        // 처음에는 해시태그1, 해시태그2를 적용해서 게시글을 만들고, 업데이트 할때 태그를 1,3으로 수정하겠다.
        PostSaveRequest postSaveRequest = createPostSaveRequestWithTag(member, false, productRequest.getName(), wishRequest.getName(), tagNames);
        Long postId = postService.savePost(postSaveRequest, member.getId());
        // 컨트롤러에서는 게시글 생성 직후 아래와 같이 tagService를 호출하여 생성된 게시글에 태그를 추가해준다.
        // 서비스 테스트에서도 이를 반영하겠다.
        tagService.addTagsToPost(postId, tagNames);
        postTagService.deletePostTagFromPost(postId);

        //then
        Post post = postRepository.findById(postId).get();
        List<PostTag> postTags = postTagRepository.findByPost(post);
        assertThat(postTags).isEmpty();

    }

//    @Test
//    @DisplayName("게시글 삭제 테스트")
//    @WithMockCustomUser
//    public void deletePostTestForSuccess() throws Exception {
//        SignUpRequest signUpRequest = createSignUpRequest();
//        authService.signUp(signUpRequest);
//        Member member = memberRepository.findByEmail(signUpRequest.getEmail()).get();
//
//        // 루트 생성
//        CategorySaveRequest rootRequest = createCategorySaveRequest("root", 0L, null, member);
//        Long rootId = categoryService.createCategory(rootRequest);
//        Category root = categoryRepository.findById(rootId).get();
//        // product, wish 생성
//
//        CategorySaveRequest productRequest = createCategorySaveRequest("product", 1L, rootId, member);
//        CategorySaveRequest wishRequest = createCategorySaveRequest("wish", 1L, rootId, member);
//        categoryService.createCategory(productRequest);
//        categoryService.createCategory(wishRequest);
//
//        //when
//
//        // 첫번째 게시글
//        PostSaveRequest postSaveRequest = createPostSaveRequest(member, false, productRequest.getName(), wishRequest.getName());
//        Long postId = postService.savePost(postSaveRequest, member.getId());
//        Post post = postRepository.findById(postId).get();
//
//        // 두번째 게시글
//        PostSaveRequest postSaveRequest2 = createPostSaveRequestWithDynamicTitle(member, "동적 제목",false, productRequest.getName(), wishRequest.getName());
//        Long postId2 = postService.savePost(postSaveRequest2, member.getId());
//        Post post2 = postRepository.findById(postId2).get();
//
//        // 첫번째 게시글 삭제
//        DeletePostRequest deletePostRequest = createDeletePostRequest(postId, member.getId());
//        postTagService.deletePostTagFromPost(postId);
//        String result = postService.deletePost(deletePostRequest, member.getId());
//
//        // 모든 게시글 조회
//        List<Post> allPosts = postRepository.findAll();
//
//        //then
//        // 게시글이 삭제된 뒤 하나만 조회됨
//        assertThat(allPosts).extracting("title")
//                .hasSize(1)
//                .contains("동적 제목");
//    }
}
