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
        return DeletePostRequest.builder()
                .requesterId(authorId)
                .postId(postId)
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

        CategorySaveRequest productRequest = createCategorySaveRequest("product", 1L, root, member);
        CategorySaveRequest wishRequest = createCategorySaveRequest("wish", 1L, root, member);
        Long productCategoryId = categoryService.createCategory(productRequest);
        Long wishCategoryId = categoryService.createCategory(wishRequest);
//        Category product = categoryRepository.findById(productCategoryId).get();
//        Category wish = categoryRepository.findById(wishCategoryId).get();

        //when
        PostSaveRequest postSaveRequest = createPostSaveRequest(member, false, productCategoryId, wishCategoryId);
        Long postId = postService.savePost(postSaveRequest);
        Post post = postRepository.findById(postId).get();

        //then
        assertThat(post.getContent()).isEqualTo(postSaveRequest.getContent());
        assertThat(post.getTitle()).isEqualTo(postSaveRequest.getTitle());
    }

    @Test
    public void findPostByAuthorSuccess() throws Exception {
        //given

        //when

        //then
    }
}
