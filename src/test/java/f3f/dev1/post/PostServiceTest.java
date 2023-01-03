package f3f.dev1.post;

import f3f.dev1.domain.category.application.CategoryService;
import f3f.dev1.domain.category.dao.CategoryRepository;
import f3f.dev1.domain.category.dto.CategoryDTO;
import f3f.dev1.domain.category.model.Category;
import f3f.dev1.domain.comment.dao.CommentRepository;
import f3f.dev1.domain.member.application.AuthService;
import f3f.dev1.domain.member.dao.MemberRepository;
import f3f.dev1.domain.member.dto.MemberDTO;
import f3f.dev1.domain.member.model.Member;
import f3f.dev1.domain.model.Address;
import f3f.dev1.domain.post.application.PostService;
import f3f.dev1.domain.post.dao.PostRepository;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.tag.application.PostTagService;
import f3f.dev1.domain.tag.application.TagService;
import f3f.dev1.domain.tag.dao.PostTagRepository;
import f3f.dev1.domain.tag.dao.TagRepository;
import f3f.dev1.domain.tag.dto.TagDTO;
import f3f.dev1.domain.tag.model.PostTag;
import f3f.dev1.domain.tag.model.Tag;
import f3f.dev1.global.common.annotation.WithMockCustomUser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static f3f.dev1.domain.category.dto.CategoryDTO.*;
import static f3f.dev1.domain.member.dto.MemberDTO.*;
import static f3f.dev1.domain.member.model.UserLoginType.EMAIL;
import static f3f.dev1.domain.post.dto.PostDTO.*;
import static f3f.dev1.domain.tag.dto.TagDTO.*;
import static org.assertj.core.api.Assertions.*;

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

    @BeforeEach
    public void deleteAll() {
        memberRepository.deleteAll();
        postRepository.deleteAll();
        commentRepository.deleteAll();
        categoryRepository.deleteAll();
        tagRepository.deleteAll();
        postTagRepository.deleteAll();
    }

    public CreateTagRequest createTagRequest(String tagName, Long authorId) {
        return new CreateTagRequest(tagName, authorId);
    }

    public AddTagToPostRequest createAddTagToPostRequest(Long tagId, Long postId) {
        return new AddTagToPostRequest(tagId, postId);
    }


    public CategorySaveRequest createCategorySaveRequest(String name, Long depth, Category parent, Member author) {
        return CategorySaveRequest.builder()
                .name(name)
                .depth(depth)
                .parent(parent)
                .member(author)
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
                .postId(1L)
                .title("제목 맘에 안들어서 바꿈")
                .content("내용도 바꿀래요")
                .productCategoryId(null)
                .wishCategoryId(null)
                .build();
    }

    public PostSaveRequest createPostSaveRequest(Member author, boolean tradeEachOther, Long productId, Long wishId) {
        return PostSaveRequest.builder()
                .content("냄새가 조금 나긴 하는데 뭐 그럭저럭 괜찮아요")
                .title("3년 신은 양말 거래 희망합니다")
                .tradeEachOther(tradeEachOther)
                .authorId(author.getId())
                .productCategoryId(productId)
                .tagNames(new ArrayList<>())
                .wishCategoryId(wishId)
                .build();
    }

    public PostSaveRequest createPostSaveRequestWithTag(Member author, boolean tradeEachOther, Long productId, Long wishId, List<String> tagNames) {
        return PostSaveRequest.builder()
                .content("태그 게시글 content")
                .title("태그 게시글 title")
                .tradeEachOther(tradeEachOther)
                .authorId(author.getId())
                .productCategoryId(productId)
                .wishCategoryId(wishId)
                .tagNames(tagNames)
                .build();
    }

    public PostSaveRequest createPostSaveRequestWithTagAndTitle(Member author, String title, boolean tradeEachOther, Long productId, Long wishId, List<String> tagNames) {
        return PostSaveRequest.builder()
                .content("태그 게시글 content")
                .title(title)
                .tradeEachOther(tradeEachOther)
                .authorId(author.getId())
                .productCategoryId(productId)
                .wishCategoryId(wishId)
                .tagNames(tagNames)
                .build();
    }

    public PostSaveRequest createPostSaveRequestWithDynamicTitle(Member author, String title, boolean tradeEachOther, Long productId, Long wishId) {
        return PostSaveRequest.builder()
                .content("냄새가 조금 나긴 하는데 뭐 그럭저럭 괜찮아요")
                .title(title)
                .tradeEachOther(tradeEachOther)
                .authorId(author.getId())
                .productCategoryId(productId)
                .tagNames(new ArrayList<>())
                .wishCategoryId(wishId)
                .build();
    }

    public PostSaveRequest createPostSaveRequestWithDynamicAuthorId(Long authorId, boolean tradeEachOther) {
        return PostSaveRequest.builder()
                .content("냄새가 조금 나긴 하는데 뭐 그럭저럭 괜찮아요")
                .title("3년 신은 양말 거래 희망합니다")
                .tradeEachOther(tradeEachOther)
                .authorId(authorId)
                .productCategoryId(null)
                .wishCategoryId(null)
                .build();
    }

    public DeletePostRequest createDeletePostRequest(Long postId, Long authorId) {
        return new DeletePostRequest(postId, authorId);
    }

    // 업데이트 요청
    public UpdatePostRequest createUpdatePostRequest(Long postId, String title, String content, Long productCategoryId, Long wishCategoryId, List<String> tagNames) {
        return UpdatePostRequest.builder()
                .postId(postId)
                .title(title)
                .content(content)
                .productCategoryId(productCategoryId)
                .wishCategoryId(wishCategoryId)
                .tagNames(tagNames)
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
        CategorySaveRequest rootRequest = createCategorySaveRequest("root", 0L, null, member);
        Long rootId = categoryService.createCategory(rootRequest);
        Category root = categoryRepository.findById(rootId).get();
        // product, wish 생성

        CategorySaveRequest productRequest = createCategorySaveRequest("도서", 1L, root, member);
        CategorySaveRequest wishRequest = createCategorySaveRequest("전자기기", 1L, root, member);
        Long productCategoryId = categoryService.createCategory(productRequest);
        Long wishCategoryId = categoryService.createCategory(wishRequest);

        //when
        PostSaveRequest postSaveRequest = createPostSaveRequest(member, false, productCategoryId, wishCategoryId);
        Long postId = postService.savePost(postSaveRequest, member.getId());
        Post post = postRepository.findById(postId).get();

        //then
        assertThat(post.getContent()).isEqualTo(postSaveRequest.getContent());
        assertThat(post.getTitle()).isEqualTo(postSaveRequest.getTitle());
    }

    @Test
    @DisplayName("작성자로 게시글 조회 테스트")
    public void findPostByAuthorSuccess() throws Exception {
        //given
        SignUpRequest signUpRequest = createSignUpRequest();
        authService.signUp(signUpRequest);
        Member member = memberRepository.findByEmail(signUpRequest.getEmail()).get();

        // 루트 생성
        CategorySaveRequest rootRequest = createCategorySaveRequest("root", 0L, null, member);
        Long rootId = categoryService.createCategory(rootRequest);
        Category root = categoryRepository.findById(rootId).get();
        // product, wish 생성

        CategorySaveRequest productRequest = createCategorySaveRequest("product", 1L, root, member);
        CategorySaveRequest wishRequest = createCategorySaveRequest("wish", 1L, root, member);
        Long productCategoryId = categoryService.createCategory(productRequest);
        Long wishCategoryId = categoryService.createCategory(wishRequest);

        //when
        // 첫번째 게시글
        PostSaveRequest postSaveRequest = createPostSaveRequest(member, false, productCategoryId, wishCategoryId);
        Long postId = postService.savePost(postSaveRequest, member.getId());
        Post post = postRepository.findById(postId).get();

        //두번째 게시글
        PostSaveRequest postSaveRequest2 = createPostSaveRequestWithDynamicTitle(member, "2년 쓴 이불 바꿔요",false, productCategoryId, wishCategoryId);
        Long postId2 = postService.savePost(postSaveRequest2, member.getId());
        Post post2 = postRepository.findById(postId2).get();

        //then
        List<PostInfoDto> postsByAuthor = postService.findPostByAuthor(member.getId());
        assertThat(postsByAuthor).extracting("title")
                .hasSize(2)
                .contains("2년 쓴 이불 바꿔요", "3년 신은 양말 거래 희망합니다");

        assertThat(postsByAuthor).extracting("content")
                .hasSize(2);
    }
    
    @Test
    @DisplayName("게시글 전체 조회 테스트")
    public void findAllPostTestForSuccess() throws Exception {
        //given
        SignUpRequest signUpRequest = createSignUpRequest();
        authService.signUp(signUpRequest);
        Member member = memberRepository.findByEmail(signUpRequest.getEmail()).get();

        // 루트 생성
        CategorySaveRequest rootRequest = createCategorySaveRequest("root", 0L, null, member);
        Long rootId = categoryService.createCategory(rootRequest);
        Category root = categoryRepository.findById(rootId).get();
        // product, wish 생성

        CategorySaveRequest productRequest = createCategorySaveRequest("product", 1L, root, member);
        CategorySaveRequest wishRequest = createCategorySaveRequest("wish", 1L, root, member);
        Long productCategoryId = categoryService.createCategory(productRequest);
        Long wishCategoryId = categoryService.createCategory(wishRequest);

        //when
        // 첫번째 게시글
        PostSaveRequest postSaveRequest = createPostSaveRequest(member, false, productCategoryId, wishCategoryId);
        Long postId = postService.savePost(postSaveRequest, member.getId());
        Post post = postRepository.findById(postId).get();

        //두번째 게시글
        PostSaveRequest postSaveRequest2 = createPostSaveRequestWithDynamicTitle(member, "2년 쓴 이불 바꿔요",false, productCategoryId, wishCategoryId);
        Long postId2 = postService.savePost(postSaveRequest2, member.getId());
        Post post2 = postRepository.findById(postId2).get();
        
        //then
        List<PostInfoDto> allPosts = postService.findAllPosts();
        assertThat(allPosts).extracting("title")
                .hasSize(2)
                .contains("2년 쓴 이불 바꿔요", "3년 신은 양말 거래 희망합니다");

        assertThat(allPosts).extracting("content")
                .hasSize(2);
    }

    @Test
    @DisplayName("조건과 함께 게시글 검색 테스트 - 태그로만 검색")
    public void findPostsWithTagNamesConditionTestForSuccess() throws Exception {
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

        // 루트 생성
        CategorySaveRequest rootRequest = createCategorySaveRequest("root", 0L, null, member);
        Long rootId = categoryService.createCategory(rootRequest);
        Category root = categoryRepository.findById(rootId).get();
        // product, wish 생성
        CategorySaveRequest productRequest = createCategorySaveRequest("도서", 1L, root, member);
        CategorySaveRequest wishRequest = createCategorySaveRequest("전자기기", 1L, root, member);
        Long productCategoryId = categoryService.createCategory(productRequest);
        Long wishCategoryId = categoryService.createCategory(wishRequest);

        //when
        // 게시글 추가 + 게시글에 태그 추가
        // 첫번째 게시글에 해시태그 1, 2, 3이 모두 추가되어있다.
        PostSaveRequest postSaveRequest = createPostSaveRequest(member, false, productCategoryId, wishCategoryId);
        Long postId = postService.savePost(postSaveRequest, member.getId());
        Post post = postRepository.findById(postId).get();
        AddTagToPostRequest addTagToPostRequest = createAddTagToPostRequest(tagId, postId);
        AddTagToPostRequest addTagToPostSecondRequest = createAddTagToPostRequest(secondTagId, postId);
        AddTagToPostRequest addTagToPostThirdRequest = createAddTagToPostRequest(thirdTagId, postId);
        tagService.addTagToPost(addTagToPostRequest);
        tagService.addTagToPost(addTagToPostSecondRequest);
        tagService.addTagToPost(addTagToPostThirdRequest);

        // 두번째 게시글에 해시태그 1, 2가 추가됐다.
        PostSaveRequest secondPostSaveRequest = createPostSaveRequestWithDynamicTitle(member, "두번째 게시글", false, productCategoryId, wishCategoryId);
        Long secondPostId = postService.savePost(secondPostSaveRequest, member.getId());
        Post secondPost = postRepository.findById(secondPostId).get();
        AddTagToPostRequest addTagToSecondPostRequest = createAddTagToPostRequest(tagId, secondPostId);
        AddTagToPostRequest addSecondTagToSecondPostRequest = createAddTagToPostRequest(secondTagId, secondPostId);
        tagService.addTagToPost(addTagToSecondPostRequest);
        tagService.addTagToPost(addSecondTagToSecondPostRequest);

        // 세번째 게시글에 해시태그1이 추가되었다.
        PostSaveRequest thirdPostSaveRequest = createPostSaveRequestWithDynamicTitle(member, "세번째 게시글", false, productCategoryId, wishCategoryId);
        Long thirdPostId = postService.savePost(thirdPostSaveRequest, member.getId());
        Post thirdPost = postRepository.findById(thirdPostId).get();
        AddTagToPostRequest addTagToThirdPostRequest = createAddTagToPostRequest(tagId, thirdPostId);
        tagService.addTagToPost(addTagToThirdPostRequest);

        // 네번째 게시글에는 해시태그를 추가하지 않겠다.
        PostSaveRequest fourthPostSaveRequest = createPostSaveRequestWithDynamicTitle(member, "네번째 게시글", false, productCategoryId, wishCategoryId);
        Long fourthPostId = postService.savePost(fourthPostSaveRequest, member.getId());
        Post fourthPost = postRepository.findById(fourthPostId).get();

        //then
        List<String> names = new ArrayList<>();
        names.add("해시태그1");
        names.add("해시태그2");
        names.add("해시태그3");

        List<PostInfoDto> postInfoDtoList = postService.findPostsWithConditions("", "", names);
        assertThat(postInfoDtoList).extracting("title")
                .hasSize(1)
                .contains("3년 신은 양말 거래 희망합니다");

        assertThat(postInfoDtoList).extracting("content")
                .hasSize(1)
                .contains("냄새가 조금 나긴 하는데 뭐 그럭저럭 괜찮아요");

        List<String> secondNames = new ArrayList<>();
        secondNames.add("해시태그1");
        secondNames.add("해시태그2");

        List<PostInfoDto> secondPostInfoDtoList = postService.findPostsWithConditions("", "", secondNames);
        assertThat(secondPostInfoDtoList).extracting("title")
                .hasSize(2)
                .contains("두번째 게시글", "3년 신은 양말 거래 희망합니다");

        List<String> thirdNames = new ArrayList<>();
        thirdNames.add("해시태그1");

        List<PostInfoDto> thirdPostInfoDtoList = postService.findPostsWithConditions("", "", thirdNames);
        assertThat(thirdPostInfoDtoList).extracting("title")
                .hasSize(3)
                .contains("세번째 게시글", "두번째 게시글", "3년 신은 양말 거래 희망합니다");

        // 비어있는 태그 이름 리스트를 넘기면 게시글 전체 조회
        List<String> emptyNamesList = new ArrayList<>();
        List<PostInfoDto> fourthPostInfoDtoList = postService.findPostsWithConditions("", "", emptyNamesList);
        assertThat(fourthPostInfoDtoList).extracting("title")
                .hasSize(4)
                .contains("네번째 게시글", "세번째 게시글", "두번째 게시글", "3년 신은 양말 거래 희망합니다");
    }

    @Test
    @DisplayName("조건과 함께 테스트 - 상품 카테고리로만 검색")
    public void findPostsWithProductCategoryNameConditionTestForSuccess() throws Exception {
        //given
        SignUpRequest signUpRequest = createSignUpRequest();
        authService.signUp(signUpRequest);
        Member member = memberRepository.findByEmail(signUpRequest.getEmail()).get();

        // 루트 생성
        CategorySaveRequest rootRequest = createCategorySaveRequest("root", 0L, null, member);
        Long rootId = categoryService.createCategory(rootRequest);
        Category root = categoryRepository.findById(rootId).get();
        // product, wish 생성

        CategorySaveRequest productRequest = createCategorySaveRequest("product", 1L, root, member);
        CategorySaveRequest wishRequest = createCategorySaveRequest("wish", 1L, root, member);
        Long productCategoryId = categoryService.createCategory(productRequest);
        Long wishCategoryId = categoryService.createCategory(wishRequest);

        // 구분을 두기 위해 다른 product 하나 더 생성
        CategorySaveRequest secondProductRequest = createCategorySaveRequest("product2", 1L, root, member);
        Long secondProductCategoryId = categoryService.createCategory(secondProductRequest);

        //when
        PostSaveRequest postSaveRequest = createPostSaveRequest(member, false, productCategoryId, wishCategoryId);
        Long postId = postService.savePost(postSaveRequest, member.getId());

        // 구분을 두기 위해 다른 productCategory를 가지는 게시글을 하나 더 만들겠음
        PostSaveRequest secondPostSaveRequest = createPostSaveRequestWithDynamicTitle(member, "두번째 게시글", false, secondProductCategoryId, wishCategoryId);
        Long secondPostId = postService.savePost(secondPostSaveRequest, member.getId());

        List<PostInfoDto> postsWithConditions = postService.findPostsWithConditions(productRequest.getName(), "", new ArrayList<>());
        List<PostInfoDto> secondPostsWithConditions = postService.findPostsWithConditions(secondProductRequest.getName(), "", new ArrayList<>());

        //then
        assertThat(postsWithConditions).extracting("title")
                .hasSize(1)
                .contains("3년 신은 양말 거래 희망합니다");

        assertThat(postsWithConditions).extracting("productCategory")
                .hasSize(1)
                .contains("product");

        assertThat(secondPostsWithConditions).extracting("title")
                .hasSize(1)
                .contains("두번째 게시글");

        assertThat(secondPostsWithConditions).extracting("productCategory")
                .hasSize(1)
                .contains("product2");
    }

    @Test
    @DisplayName("조건과 함께 테스트 - 희망 카테고리로만 검색")
    public void findPostsWithWishCategoryNameConditionTestForSuccess() throws Exception {
        SignUpRequest signUpRequest = createSignUpRequest();
        authService.signUp(signUpRequest);
        Member member = memberRepository.findByEmail(signUpRequest.getEmail()).get();

        // 루트 생성
        CategorySaveRequest rootRequest = createCategorySaveRequest("root", 0L, null, member);
        Long rootId = categoryService.createCategory(rootRequest);
        Category root = categoryRepository.findById(rootId).get();
        // product, wish 생성

        CategorySaveRequest productRequest = createCategorySaveRequest("product", 1L, root, member);
        CategorySaveRequest wishRequest = createCategorySaveRequest("wish", 1L, root, member);
        Long productCategoryId = categoryService.createCategory(productRequest);
        Long wishCategoryId = categoryService.createCategory(wishRequest);

        CategorySaveRequest secondWishRequest = createCategorySaveRequest("wish2", 1L, root, member);
        Long secondWishCategoryId = categoryService.createCategory(secondWishRequest);

        //when
        PostSaveRequest postSaveRequest = createPostSaveRequest(member, false, productCategoryId, wishCategoryId);
        Long postId = postService.savePost(postSaveRequest, member.getId());

        // 구분을 두기 위해 다른 productCategory를 가지는 게시글을 하나 더 만들겠음
        PostSaveRequest secondPostSaveRequest = createPostSaveRequestWithDynamicTitle(member, "두번째 게시글", false, productCategoryId, secondWishCategoryId);
        Long secondPostId = postService.savePost(secondPostSaveRequest, member.getId());

        // 하나만 더 만들겠음
        PostSaveRequest thirdPostSaveRequest = createPostSaveRequestWithDynamicTitle(member, "세번째 게시글", false, productCategoryId, secondWishCategoryId);
        Long thirdPostId = postService.savePost(thirdPostSaveRequest, member.getId());

        List<PostInfoDto> postsWithConditions = postService.findPostsWithConditions("", wishRequest.getName(), new ArrayList<>());
        List<PostInfoDto> secondPostsWithConditions = postService.findPostsWithConditions("", secondWishRequest.getName(), new ArrayList<>());

        //then
        assertThat(postsWithConditions).extracting("title")
                .hasSize(1)
                .contains("3년 신은 양말 거래 희망합니다");

        assertThat(postsWithConditions).extracting("wishCategory")
                .hasSize(1)
                .contains("wish");

        assertThat(secondPostsWithConditions).extracting("title")
                .hasSize(2)
                .contains("두번째 게시글", "세번째 게시글");

        assertThat(secondPostsWithConditions).extracting("wishCategory")
                .hasSize(2)
                .contains("wish2");

    }

    @Test
    @DisplayName("조건과 함께 테스트 - 상품 카테고리와 희망 카테고리 모두 고려하여 검색")
    public void findPostsWithBothProductCategoryNameAndWishCategoryNameConditionTestForSuccess() throws Exception {
        SignUpRequest signUpRequest = createSignUpRequest();
        authService.signUp(signUpRequest);
        Member member = memberRepository.findByEmail(signUpRequest.getEmail()).get();

        // 루트 생성
        CategorySaveRequest rootRequest = createCategorySaveRequest("root", 0L, null, member);
        Long rootId = categoryService.createCategory(rootRequest);
        Category root = categoryRepository.findById(rootId).get();
        // product, wish 생성

        CategorySaveRequest productRequest = createCategorySaveRequest("product", 1L, root, member);
        CategorySaveRequest wishRequest = createCategorySaveRequest("wish", 1L, root, member);
        Long productCategoryId = categoryService.createCategory(productRequest);
        Long wishCategoryId = categoryService.createCategory(wishRequest);

        CategorySaveRequest secondProductRequest = createCategorySaveRequest("product2", 1L, root, member);
        CategorySaveRequest secondWishRequest = createCategorySaveRequest("wish2", 1L, root, member);
        Long secondProductCategoryId = categoryService.createCategory(secondProductRequest);
        Long secondWishCategoryId = categoryService.createCategory(secondWishRequest);

        //when

        // 첫번째 게시글은 product, wish로 생성하겠음
        PostSaveRequest postSaveRequest = createPostSaveRequestWithDynamicTitle(member, "첫번째 게시글", false, productCategoryId, wishCategoryId);
        Long postId = postService.savePost(postSaveRequest, member.getId());

        // 두번째 게시글은 product2, wish로 생성하겠음
        PostSaveRequest secondPostSaveRequest = createPostSaveRequestWithDynamicTitle(member, "두번째 게시글", false, secondProductCategoryId, wishCategoryId);
        Long secondPostId = postService.savePost(secondPostSaveRequest, member.getId());

        // 세번째 게시글은 product, wish2로 생성하겠음
        PostSaveRequest thirdPostSaveRequest = createPostSaveRequestWithDynamicTitle(member, "세번째 게시글", false, productCategoryId, secondWishCategoryId);
        Long thirdPostId = postService.savePost(thirdPostSaveRequest, member.getId());

        // 네번째 게시글은 product2, wish2로 생성하겠음
        PostSaveRequest fourthPostSaveRequest = createPostSaveRequestWithDynamicTitle(member, "네번째 게시글", false, secondProductCategoryId, secondWishCategoryId);
        Long fourthPostId = postService.savePost(fourthPostSaveRequest, member.getId());

        // 첫번째 게시글만 조회되어야 한다.
        List<PostInfoDto> firstPostsWithConditions = postService.findPostsWithConditions(productRequest.getName(), wishRequest.getName(), new ArrayList<>());
        // 두번째 게시글만 조회되어야 한다.
        List<PostInfoDto> secondPostsWithConditions = postService.findPostsWithConditions(secondProductRequest.getName(), wishRequest.getName(), new ArrayList<>());
        // 세번째 게시글만 조회되어야 한다.
        List<PostInfoDto> thirdPostsWithConditions = postService.findPostsWithConditions(productRequest.getName(), secondWishRequest.getName(), new ArrayList<>());
        // 네번째 게시글만 조회되어야 한다.
        List<PostInfoDto> fourthPostsWithConditions = postService.findPostsWithConditions(secondProductRequest.getName(), secondWishRequest.getName(), new ArrayList<>());
        // 첫번째, 세번째 게시글만 조회되어야 한다.
        List<PostInfoDto> firstAndThirdPostsWithConditions = postService.findPostsWithConditions(productRequest.getName(), "", new ArrayList<>());
        // 두번째, 네번째 게시글만 조회되어야 한다.
        List<PostInfoDto> secondAndFourthPostsWithConditions = postService.findPostsWithConditions(secondProductRequest.getName(), "", new ArrayList<>());
        // 첫번째, 두번째 게시글만 조회되어야 한다.
        List<PostInfoDto> firstAndSecondPostsWithConditions = postService.findPostsWithConditions("", wishRequest.getName(), new ArrayList<>());
        // 세번째, 네번째 게시글만 조회되어야 한다.
        List<PostInfoDto> thirdAndFourthPostsWithConditions = postService.findPostsWithConditions("", secondWishRequest.getName(), new ArrayList<>());
        // 조건이 전달되지 않았으므로 모든 게시글이 조회되어야 한다.
        List<PostInfoDto> allPostsWithNoConditions = postService.findPostsWithConditions("", "", new ArrayList<>());
        //then
        assertThat(firstPostsWithConditions).extracting("title").hasSize(1).contains("첫번째 게시글");
        assertThat(firstPostsWithConditions).extracting("productCategory").hasSize(1).contains("product");
        assertThat(firstPostsWithConditions).extracting("wishCategory").hasSize(1).contains("wish");

        assertThat(secondPostsWithConditions).extracting("title").hasSize(1).contains("두번째 게시글");
        assertThat(secondPostsWithConditions).extracting("productCategory").hasSize(1).contains("product2");
        assertThat(secondPostsWithConditions).extracting("wishCategory").hasSize(1).contains("wish");

        assertThat(thirdPostsWithConditions).extracting("title").hasSize(1).contains("세번째 게시글");
        assertThat(thirdPostsWithConditions).extracting("productCategory").hasSize(1).contains("product");
        assertThat(thirdPostsWithConditions).extracting("wishCategory").hasSize(1).contains("wish2");

        assertThat(fourthPostsWithConditions).extracting("title").hasSize(1).contains("네번째 게시글");
        assertThat(fourthPostsWithConditions).extracting("productCategory").hasSize(1).contains("product2");
        assertThat(fourthPostsWithConditions).extracting("wishCategory").hasSize(1).contains("wish2");

        assertThat(firstAndThirdPostsWithConditions).extracting("title").hasSize(2).contains("첫번째 게시글", "세번째 게시글");
        assertThat(firstAndThirdPostsWithConditions).extracting("productCategory").hasSize(2).contains("product");

        assertThat(secondAndFourthPostsWithConditions).extracting("title").hasSize(2).contains("두번째 게시글", "네번째 게시글");
        assertThat(secondAndFourthPostsWithConditions).extracting("productCategory").hasSize(2).contains("product2");

        assertThat(firstAndSecondPostsWithConditions).extracting("title").hasSize(2).contains("첫번째 게시글", "두번째 게시글");
        assertThat(firstAndSecondPostsWithConditions).extracting("wishCategory").hasSize(2).contains("wish");

        assertThat(thirdAndFourthPostsWithConditions).extracting("title").hasSize(2).contains("세번째 게시글", "네번째 게시글");
        assertThat(thirdAndFourthPostsWithConditions).extracting("wishCategory").hasSize(2).contains("wish2");

        assertThat(allPostsWithNoConditions).extracting("title").hasSize(4).contains("첫번째 게시글", "두번째 게시글", "세번째 게시글", "네번째 게시글");
    }

    @Test
    @DisplayName("조건과 함께 테스트 - 모든 조건들 고려한 최종 조회 테스트")
    public void findPostsWithProductCategoryNameAndTagsConditionTestForSuccess() throws Exception {
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

        // 루트 생성
        CategorySaveRequest rootRequest = createCategorySaveRequest("root", 0L, null, member);
        Long rootId = categoryService.createCategory(rootRequest);
        Category root = categoryRepository.findById(rootId).get();
        // product, wish 생성

        CategorySaveRequest productRequest = createCategorySaveRequest("product", 1L, root, member);
        CategorySaveRequest wishRequest = createCategorySaveRequest("wish", 1L, root, member);
        Long productCategoryId = categoryService.createCategory(productRequest);
        Long wishCategoryId = categoryService.createCategory(wishRequest);

        CategorySaveRequest secondProductRequest = createCategorySaveRequest("product2", 1L, root, member);
        CategorySaveRequest secondWishRequest = createCategorySaveRequest("wish2", 1L, root, member);
        Long secondProductCategoryId = categoryService.createCategory(secondProductRequest);
        Long secondWishCategoryId = categoryService.createCategory(secondWishRequest);

        //when
        List<String> firstTagNames = new ArrayList<>();
        firstTagNames.add(tagRequest.getName());
        firstTagNames.add(secondTagRequest.getName());

        List<String> secondTagNames = new ArrayList<>();
        secondTagNames.add(secondTagRequest.getName());
        secondTagNames.add(thirdTagRequest.getName());

        List<String> thirdTagNames = new ArrayList<>();
        thirdTagNames.add(tagRequest.getName());
        thirdTagNames.add(thirdTagRequest.getName());

        // 첫번째 게시글은 상품카테고리1, 희망카테고리1과 해시태그1, 해시태그2
        PostSaveRequest firstPostSaveRequest = createPostSaveRequestWithTagAndTitle(member, "첫번째 게시글",false, productCategoryId, wishCategoryId, firstTagNames);
        // 두번째 게시글은 상품카테고리2, 희망카테고리1과 해시태그2, 해시태그3
        PostSaveRequest secondPostSaveRequest = createPostSaveRequestWithTagAndTitle(member, "두번째 게시글",false, secondProductCategoryId, wishCategoryId, secondTagNames);
        // 세번째 게시글은 상품카테고리2, 희망카테고리2와 해시태그1, 해시태그3
        PostSaveRequest thirdPostSaveRequest = createPostSaveRequestWithTagAndTitle(member, "세번째 게시글",false, secondProductCategoryId, secondWishCategoryId, thirdTagNames);

        Long firstPostId = postService.savePost(firstPostSaveRequest, member.getId());
        Long secondPostId = postService.savePost(secondPostSaveRequest, member.getId());
        Long thirdPostId = postService.savePost(thirdPostSaveRequest, member.getId());

        // 태그 추가 - 이 부분은 컨트롤러에서 자동으로 시행해주지만 서비스로직 테스트라 수동으로 추가하겠다.
        AddTagToPostRequest addTagToPostRequest = createAddTagToPostRequest(tagId, firstPostId);
        AddTagToPostRequest addTagToPostSecondRequest = createAddTagToPostRequest(secondTagId, firstPostId);
        tagService.addTagToPost(addTagToPostRequest);
        tagService.addTagToPost(addTagToPostSecondRequest);

        AddTagToPostRequest addTagToSecondPostRequest = createAddTagToPostRequest(secondTagId, secondPostId);
        AddTagToPostRequest addTagToSecondPostSecondRequest = createAddTagToPostRequest(thirdTagId, secondPostId);
        tagService.addTagToPost(addTagToSecondPostRequest);
        tagService.addTagToPost(addTagToSecondPostSecondRequest);

        AddTagToPostRequest addTagToThirdPostRequest = createAddTagToPostRequest(tagId, thirdPostId);
        AddTagToPostRequest addTagToThirdPostSecondRequest = createAddTagToPostRequest(thirdTagId, thirdPostId);
        tagService.addTagToPost(addTagToThirdPostRequest);
        tagService.addTagToPost(addTagToThirdPostSecondRequest);

        // then
        // 첫번째 검증 : 태그와 희망 카테고리 없이 상품 카테고리 만으로 조회 : 두번째, 세번째 게시글이 조회되어야 한다.
        List<PostInfoDto> firstResult = postService.findPostsWithConditions(secondProductRequest.getName(), "", new ArrayList<>());
        // 두번째 검증 : 태그와 상품 카테고리 없이 희망 카테고리 만으로 조회 : 첫번째, 두번째 게시글이 조회되어야 한다.
        List<PostInfoDto> secondResult = postService.findPostsWithConditions("", wishRequest.getName(), new ArrayList<>());
        // 세번째 검증 : 태그1 만으로 조회 : 첫번째, 세번째 게시글이 조회되어야 한다.
        List<String> firstTagName = new ArrayList<>();
        firstTagName.add("해시태그1");
        List<PostInfoDto> thirdResult = postService.findPostsWithConditions("", "", firstTagName);
        // 네번째 검증 - 1: 모든 정보를 활용해 조회 - 상품 카테고리1, 희망 카테고리1, 해시태그1 - 첫번째 게시글이 조회되어야 한다.
        List<PostInfoDto> fourth_Result1 = postService.findPostsWithConditions(productRequest.getName(), wishRequest.getName(), firstTagName);
        // 네번째 검증 - 2: 모든 정보를 활용해 조회 - 상품 카테고리1, 희망 카테고리1, 해시태그1, 2 - 첫번째 게시글이 조회되어야 한다.
        List<PostInfoDto> fourth_Result2 = postService.findPostsWithConditions(productRequest.getName(), wishRequest.getName(), firstTagNames);
        // 네번째 검증 - 3: 모든 정보를 활용해 조회 - 상품 카테고리2, 희망 카테고리1, 해시태그2, 3 - 두번째 게시글이 조회되어야 한다.
        List<PostInfoDto> fourth_Result3 = postService.findPostsWithConditions(secondProductRequest.getName(), wishRequest.getName(), secondTagNames);
        // 네번째 검증 - 4: 모든 정보를 활용해 조회 - 상품 카테고리2, 희망 카테고리2, 해시태그1, 3 - 세번째 게시글이 조회되어야 한다.
        List<PostInfoDto> fourth_Result4 = postService.findPostsWithConditions(secondProductRequest.getName(), secondWishRequest.getName(), thirdTagNames);

        // 마지막 검증 : 조건 없이 조회 - 모든 게시글이 다 조회되어야 한다.
        List<PostInfoDto> lastResult = postService.findPostsWithConditions("", "", new ArrayList<>());

        assertThat(firstResult).extracting("title").hasSize(2).contains("두번째 게시글", "세번째 게시글");
        assertThat(firstResult).extracting("productCategory").hasSize(2).contains("product2");

        assertThat(secondResult).extracting("title").hasSize(2).contains("첫번째 게시글", "두번째 게시글");
        assertThat(secondResult).extracting("wishCategory").hasSize(2).contains("wish");

        assertThat(thirdResult).extracting("title").hasSize(2).contains("첫번째 게시글", "세번째 게시글");

        assertThat(fourth_Result1).extracting("title").hasSize(1).contains("첫번째 게시글");
        assertThat(fourth_Result1).extracting("productCategory").hasSize(1).contains("product");
        assertThat(fourth_Result1).extracting("wishCategory").hasSize(1).contains("wish");

        assertThat(fourth_Result2).extracting("title").hasSize(1).contains("첫번째 게시글");
        assertThat(fourth_Result2).extracting("productCategory").hasSize(1).contains("product");
        assertThat(fourth_Result2).extracting("wishCategory").hasSize(1).contains("wish");

        assertThat(fourth_Result3).extracting("title").hasSize(1).contains("두번째 게시글");
        assertThat(fourth_Result3).extracting("productCategory").hasSize(1).contains("product2");
        assertThat(fourth_Result3).extracting("wishCategory").hasSize(1).contains("wish");

        assertThat(fourth_Result4).extracting("title").hasSize(1).contains("세번째 게시글");
        assertThat(fourth_Result4).extracting("productCategory").hasSize(1).contains("product2");
        assertThat(fourth_Result4).extracting("wishCategory").hasSize(1).contains("wish2");

        assertThat(lastResult).extracting("title").hasSize(3).contains("첫번째 게시글", "두번째 게시글", "세번째 게시글");
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

        CategorySaveRequest productRequest = createCategorySaveRequest("product", 1L, root, member);
        CategorySaveRequest wishRequest = createCategorySaveRequest("wish", 1L, root, member);
        Long productCategoryId = categoryService.createCategory(productRequest);
        Long wishCategoryId = categoryService.createCategory(wishRequest);

        //when
        PostSaveRequest postSaveRequest = createPostSaveRequest(member, false, productCategoryId, wishCategoryId);
        Long postId = postService.savePost(postSaveRequest, member.getId());
        Post post = postRepository.findById(postId).get();
        UpdatePostRequest updatePostRequest = createUpdatePostRequest(postId, "변경한 제목", "변경한 내용", post.getProductCategory().getId(), post.getWishCategory().getId(), new ArrayList<>());
        PostInfoDto postInfoDto = postService.updatePost(updatePostRequest, member.getId());

        //then
        assertThat(postInfoDto.getTitle()).isEqualTo(updatePostRequest.getTitle());
        assertThat(postInfoDto.getContent()).isEqualTo(updatePostRequest.getContent());
        assertThat(postInfoDto.getId()).isEqualTo(updatePostRequest.getPostId());

        // then +
        Post updatedPost = postRepository.findById(postInfoDto.getId()).get();
        assertThat(updatedPost.getTitle()).isEqualTo(updatedPost.getTitle());
        assertThat(updatedPost.getContent()).isEqualTo(updatedPost.getContent());
    }

    @Test
    @DisplayName("게시글 수정 - 태그 변경")
    public void updatePostWithDifferentTagsTestForSuccess() throws Exception {
        SignUpRequest signUpRequest = createSignUpRequest();
        authService.signUp(signUpRequest);
        Member member = memberRepository.findByEmail(signUpRequest.getEmail()).get();

        // 루트 생성
        CategorySaveRequest rootRequest = createCategorySaveRequest("root", 0L, null, member);
        Long rootId = categoryService.createCategory(rootRequest);
        Category root = categoryRepository.findById(rootId).get();
        // product, wish 생성

        CategorySaveRequest productRequest = createCategorySaveRequest("product", 1L, root, member);
        CategorySaveRequest wishRequest = createCategorySaveRequest("wish", 1L, root, member);
        Long productCategoryId = categoryService.createCategory(productRequest);
        Long wishCategoryId = categoryService.createCategory(wishRequest);

        // 태그 생성
        CreateTagRequest tagRequest1 = createTagRequest("해시태그1", member.getId());
        CreateTagRequest tagRequest2 = createTagRequest("해시태그2", member.getId());
        Long tag1 = tagService.createTag(tagRequest1);
        Long tag2 = tagService.createTag(tagRequest2);

        //when
        PostSaveRequest postSaveRequest = createPostSaveRequest(member, false, productCategoryId, wishCategoryId);
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
        UpdatePostRequest updatePostRequest = createUpdatePostRequest(postId, "변경한 제목", "변경한 내용", post.getProductCategory().getId(), post.getWishCategory().getId(), secondTagNameList);
        // 컨트롤러에서는 update 하기 전에 postTagService에서 레포지토리를 삭제한다. 똑같은 환경으로 테스트 하기 위해 여기서도 그렇게 하겠다.
        postTagService.deletePostTagFromPost(postId);
        PostInfoDto postInfoDto = postService.updatePost(updatePostRequest, member.getId());

        //then
        Post updatedPost = postRepository.findById(postInfoDto.getId()).get();
//        List<PostTag> postTags = updatedPost.getPostTags();
        // 이 postTags는 해시태그1이 아닌 해시태그2만을 담고 있어야 한다.
        List<PostTag> postTags = postTagRepository.findByPost(updatedPost);
        assertThat(postTags.size()).isEqualTo(1);
        // 아래의 검증에서 updatedPost는 단 하나의 해시태그만을 가지고 있으며, 그 해시태그의 이름이 "해시태그2"라는 것이 증명되었다.
        List<PostTag> tag2PostTags = postTagRepository.findByTagName("해시태그2");
        assertThat(tag2PostTags.size()).isEqualTo(1);
        assertThat(tag2PostTags.get(0).getId().equals(postTags.get(0).getId()));

        List<PostTag> tag1PostTags = postTagRepository.findByTagName("해시태그1");
        assertThat(tag1PostTags).isEmpty();

        // 아니 근데 태그 쪽에서 삭제됐는지를 알고싶은데

    }

    @Test
    @DisplayName("게시글 삭제 테스트")
    @WithMockCustomUser
    public void deletePostTestForSuccess() throws Exception {
        SignUpRequest signUpRequest = createSignUpRequest();
        authService.signUp(signUpRequest);
        Member member = memberRepository.findByEmail(signUpRequest.getEmail()).get();

        // 루트 생성
        CategorySaveRequest rootRequest = createCategorySaveRequest("root", 0L, null, member);
        Long rootId = categoryService.createCategory(rootRequest);
        Category root = categoryRepository.findById(rootId).get();
        // product, wish 생성

        CategorySaveRequest productRequest = createCategorySaveRequest("product", 1L, root, member);
        CategorySaveRequest wishRequest = createCategorySaveRequest("wish", 1L, root, member);
        Long productCategoryId = categoryService.createCategory(productRequest);
        Long wishCategoryId = categoryService.createCategory(wishRequest);

        //when

        // 첫번째 게시글
        PostSaveRequest postSaveRequest = createPostSaveRequest(member, false, productCategoryId, wishCategoryId);
        Long postId = postService.savePost(postSaveRequest, member.getId());
        Post post = postRepository.findById(postId).get();

        // 두번째 게시글
        PostSaveRequest postSaveRequest2 = createPostSaveRequestWithDynamicTitle(member, "2년 쓴 이불 바꿔요",false, productCategoryId, wishCategoryId);
        Long postId2 = postService.savePost(postSaveRequest2, member.getId());
        Post post2 = postRepository.findById(postId2).get();

        // 첫번째 게시글 삭제
        DeletePostRequest deletePostRequest = createDeletePostRequest(postId, member.getId());
        String result = postService.deletePost(deletePostRequest, member.getId());

        // 모든 게시글 조회
        List<PostInfoDto> allPosts = postService.findAllPosts();

        //then
        // 게시글이 삭제된 뒤 하나만 조회됨
        assertThat(allPosts).extracting("title")
                .hasSize(1)
                .contains("2년 쓴 이불 바꿔요");
    }
}
