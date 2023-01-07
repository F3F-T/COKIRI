package f3f.dev1.comment;

import f3f.dev1.domain.category.application.CategoryService;
import f3f.dev1.domain.category.dao.CategoryRepository;
import f3f.dev1.domain.category.model.Category;
import f3f.dev1.domain.comment.application.CommentService;
import f3f.dev1.domain.comment.dao.CommentRepository;
import f3f.dev1.domain.comment.model.Comment;
import f3f.dev1.domain.member.application.AuthService;
import f3f.dev1.domain.member.dao.MemberRepository;
import f3f.dev1.domain.member.model.Member;
import f3f.dev1.domain.model.Address;
import f3f.dev1.domain.post.application.PostService;
import f3f.dev1.domain.post.dao.PostRepository;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.tag.application.PostTagService;
import f3f.dev1.domain.tag.application.TagService;
import f3f.dev1.domain.tag.dao.PostTagRepository;
import f3f.dev1.domain.tag.dao.TagRepository;
import f3f.dev1.global.common.annotation.WithMockCustomUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static f3f.dev1.domain.category.dto.CategoryDTO.*;
import static f3f.dev1.domain.comment.dto.CommentDTO.*;
import static f3f.dev1.domain.member.dto.MemberDTO.*;
import static f3f.dev1.domain.member.model.UserLoginType.EMAIL;
import static f3f.dev1.domain.post.dto.PostDTO.*;
import static f3f.dev1.domain.tag.dto.TagDTO.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class CommentServiceTest {

    @Autowired
    CommentService commentService;

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
                .id(1L)
                .title("제목 맘에 안들어서 바꿈")
                .content("내용도 바꿀래요")
                .productCategory(null)
                .wishCategory(null)
                .build();
    }

    public PostSaveRequest createCompletedPostSaveRequest(Member author, String title, String content, boolean tradeEachOther,
                                                                  String productName, String wishName, List<String> tagNames, Long price) {
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

    public CreateCommentRequest createCommentRequest(Member author, Long postId, String content, Long parentCommentId) {
        return CreateCommentRequest.builder()
                .authorId(author.getId())
                .postId(postId)
                .depth(0L)
                .content(content)
                .parentCommentId(parentCommentId)
                .build();
    }

    public Long createPost() {
        SignUpRequest signUpRequest = createSignUpRequest();
        authService.signUp(signUpRequest);
        Member member = memberRepository.findByEmail(signUpRequest.getEmail()).get();
        CreateTagRequest tagRequest = createTagRequest("해시태그1", member.getId());
        CreateTagRequest secondTagRequest = createTagRequest("해시태그2", member.getId());
        CreateTagRequest thirdTagRequest = createTagRequest("해시태그3", member.getId());
        tagService.createTag(tagRequest);
        tagService.createTag(secondTagRequest);
        tagService.createTag(thirdTagRequest);

        CategorySaveRequest rootRequest = createCategorySaveRequest("root", 0L, null, member);
        Long rootId = categoryService.createCategory(rootRequest);
        Category root = categoryRepository.findById(rootId).get();
        // product, wish 생성

        CategorySaveRequest productRequest = createCategorySaveRequest("product", 1L, rootId, member);
        CategorySaveRequest wishRequest = createCategorySaveRequest("wish", 1L, rootId, member);
        categoryService.createCategory(productRequest);
        categoryService.createCategory(wishRequest);

        CategorySaveRequest secondProductRequest = createCategorySaveRequest("product2", 1L, rootId, member);
        CategorySaveRequest secondWishRequest = createCategorySaveRequest("wish2", 1L, rootId, member);
        categoryService.createCategory(secondProductRequest);
        categoryService.createCategory(secondWishRequest);

        List<String> tagNames = new ArrayList<>();
        tagNames.add("해시태그1");
        tagNames.add("해시태그2");
        PostSaveRequest request = createCompletedPostSaveRequest(member, "첫번째 게시글", "첫번째 내용", false, productRequest.getName(), wishRequest.getName(), tagNames, 7500L);
        Long postId = postService.savePost(request, member.getId());
        tagService.addTagsToPost(postId, tagNames);
        return postId;

    }

    @Test
    @WithMockCustomUser
    @DisplayName("댓글 생성 테스트 - 부모 댓글")
    public void createCommentTestForSuccess() throws Exception {
        //given
        Long postId = createPost();
        Post post = postRepository.findById(postId).get();
        Member member = post.getAuthor();
        CreateCommentRequest commentRequest = createCommentRequest(member, postId, "첫번째 댓글", null);
        CreateCommentRequest secondCommentRequest = createCommentRequest(member, postId, "두번째 댓글", null);
        CreateCommentRequest thirdCommentRequest = createCommentRequest(member, postId, "세번째 댓글", null);

        //when
        // 댓글 생성
        CommentInfoDto commentInfoDto = commentService.createComment(commentRequest, member.getId());
        CommentInfoDto secondCommentInfoDto = commentService.createComment(secondCommentRequest, member.getId());
        CommentInfoDto thirdCommentInfoDto = commentService.createComment(thirdCommentRequest, member.getId());

        //then
        List<Comment> comments = commentRepository.findByPostId(postId);
        assertThat(comments).extracting("content").hasSize(3).contains("첫번째 댓글", "두번째 댓글", "세번째 댓글");
    }


}
