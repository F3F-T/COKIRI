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
import f3f.dev1.global.common.annotation.WithMockCustomUser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static f3f.dev1.domain.category.dto.CategoryDTO.*;
import static f3f.dev1.domain.member.dto.MemberDTO.*;
import static f3f.dev1.domain.member.model.UserLoginType.EMAIL;
import static f3f.dev1.domain.post.dto.PostDTO.*;
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
    MemberRepository memberRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @BeforeEach
    public void deleteAll() {
        memberRepository.deleteAll();
        postRepository.deleteAll();
        commentRepository.deleteAll();
        categoryRepository.deleteAll();
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
                .postTags(null)
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

    // 업데이트 요청, postTags 제외
    public UpdatePostRequest createUpdatePostRequest(Long postId, String title, String content, Long productCategoryId, Long wishCategoryId) {
        return UpdatePostRequest.builder()
                .postId(postId)
                .title(title)
                .content(content)
                .productCategoryId(productCategoryId)
                .wishCategoryId(wishCategoryId)
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
    @DisplayName("태그 정보와 함께 게시글 조회 테스트")
    public void getPostsWithTagsTestForSuccess() throws Exception {
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
        List<String> names = new ArrayList<>();
        names.add("해시태그1");
        names.add("해시태그2");
        names.add("해시태그3");
        PostSaveRequest postSaveRequestWithTag = createPostSaveRequestWithTag(member, false, productCategoryId, wishCategoryId, names);
        Long postId = postService.savePost(postSaveRequestWithTag, member.getId());
        Post post = postRepository.findById(postId).get();

        //두번째 게시글
        List<String> names2 = new ArrayList<>();
        names2.add("해시태그4");
        PostSaveRequest postSaveRequestWithTag2 = createPostSaveRequestWithTag(member, false, productCategoryId, wishCategoryId, names2);
        Long postId2 = postService.savePost(postSaveRequestWithTag2, member.getId());
        Post post2 = postRepository.findById(postId2).get();

        //then
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
        UpdatePostRequest updatePostRequest = createUpdatePostRequest(postId, "변경한 제목", "변경한 내용", post.getProductCategory().getId(), post.getWishCategory().getId());
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
        String result = postService.deletePost(deletePostRequest);

        // 모든 게시글 조회
        List<PostInfoDto> allPosts = postService.findAllPosts();

        //then
        // 게시글이 삭제된 뒤 하나만 조회됨
        assertThat(allPosts).extracting("title")
                .hasSize(1)
                .contains("2년 쓴 이불 바꿔요");
    }
}
