package f3f.dev1.comment;

import f3f.dev1.domain.address.model.Address;
import f3f.dev1.domain.category.application.CategoryService;
import f3f.dev1.domain.category.dao.CategoryRepository;
import f3f.dev1.domain.category.model.Category;
import f3f.dev1.domain.comment.application.CommentService;
import f3f.dev1.domain.comment.dao.CommentRepository;
import f3f.dev1.domain.comment.model.Comment;
import f3f.dev1.domain.member.application.AuthService;
import f3f.dev1.domain.member.dao.MemberRepository;
import f3f.dev1.domain.member.model.Member;
import f3f.dev1.domain.post.application.PostService;
import f3f.dev1.domain.post.dao.PostRepository;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.tag.application.PostTagService;
import f3f.dev1.domain.tag.application.TagService;
import f3f.dev1.domain.tag.dao.PostTagRepository;
import f3f.dev1.domain.tag.dao.TagRepository;
import f3f.dev1.global.common.annotation.WithMockCustomUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static f3f.dev1.domain.category.dto.CategoryDTO.CategorySaveRequest;
import static f3f.dev1.domain.comment.dto.CommentDTO.*;
import static f3f.dev1.domain.member.dto.MemberDTO.SignUpRequest;
import static f3f.dev1.domain.member.model.UserLoginType.EMAIL;
import static f3f.dev1.domain.post.dto.PostDTO.PostSaveRequest;
import static f3f.dev1.domain.post.dto.PostDTO.SearchPostRequest;
import static f3f.dev1.domain.tag.dto.TagDTO.CreateTagRequest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
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

    public CreateTagRequest createTagRequest(String tagName, Long authorId) {
        return new CreateTagRequest(tagName, authorId);
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
                .email("newstyle@email.com")
                .birthDate("990626")
                .password("password")
                .userLoginType(EMAIL)
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

    public CreateCommentRequest createCommentRequest(Member author, Long postId, String content, Long parentCommentId) {
        return CreateCommentRequest.builder()
                .authorId(author.getId())
                .postId(postId)
                .depth(0L)
                .content(content)
                .parentCommentId(parentCommentId)
                .build();

    }

    public UpdateCommentRequest createUpdateCommentRequest(Long commentId, Long parentId, Long authorId, Long postId, String content) {
        return new UpdateCommentRequest(commentId, parentId, authorId, postId, content);
    }

    public DeleteCommentRequest createDeleteCommentRequest(Long commentId, Long authorId, Long postId) {
        return new DeleteCommentRequest(commentId, authorId, postId);
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
//        CategorySaveRequest secondProductRequest = createCategorySaveRequest("product2", 1L, rootId, member);
//        CategorySaveRequest secondWishRequest = createCategorySaveRequest("wish2", 1L, rootId, member);
//        categoryService.createCategory(secondProductRequest);
//        categoryService.createCategory(secondWishRequest);

        List<String> tagNames = new ArrayList<>();
        tagNames.add("해시태그1");
        tagNames.add("해시태그2");
        PostSaveRequest request = createCompletedPostSaveRequest(member, "첫번째 게시글", "첫번째 내용", false, "도서", "전자기기", tagNames, 7500L);
        Long postId = postService.savePost(request, member.getId());
        tagService.addTagsToPost(postId, tagNames);
        return postId;

    }

    @Test
    @WithMockCustomUser
    @DisplayName("댓글 생성 테스트 - 부모댓글")
    public void createParentCommentTestForSuccess() throws Exception {
        //given
        Long postId = createPost();
        Post post = postRepository.findById(postId).get();
        Member member = post.getAuthor();
        CreateCommentRequest commentRequest = createCommentRequest(member, postId, "첫번째 댓글", null);
        CreateCommentRequest secondCommentRequest = createCommentRequest(member, postId, "두번째 댓글", null);
        CreateCommentRequest thirdCommentRequest = createCommentRequest(member, postId, "세번째 댓글", null);

        //when
        // 댓글 생성
        CommentInfoDto commentInfoDto = commentService.saveComment(commentRequest, member.getId());
        CommentInfoDto secondCommentInfoDto = commentService.saveComment(secondCommentRequest, member.getId());
        CommentInfoDto thirdCommentInfoDto = commentService.saveComment(thirdCommentRequest, member.getId());

        //then
        List<Comment> comments = commentRepository.findByPostId(postId);
        assertThat(comments).extracting("content").hasSize(3).contains("첫번째 댓글", "두번째 댓글", "세번째 댓글");
    }

    @Test
    @WithMockCustomUser
    @DisplayName("댓글 생성 및 조회 테스트 - 자식댓글")
    public void createChildCommentTestForSuccess() throws Exception {
        //given
        Long postId = createPost();
        Post post = postRepository.findById(postId).get();
        Member member = post.getAuthor();
        CreateCommentRequest commentRequest = createCommentRequest(member, postId, "첫번째 댓글", null);
        CreateCommentRequest secondCommentRequest = createCommentRequest(member, postId, "두번째 댓글", null);
        CreateCommentRequest thirdCommentRequest = createCommentRequest(member, postId, "세번째 댓글", null);

        //when
        // 댓글 생성 - 부모댓글 3개 생성
        CommentInfoDto commentInfoDto = commentService.saveComment(commentRequest, member.getId());
        CommentInfoDto secondCommentInfoDto = commentService.saveComment(secondCommentRequest, member.getId());
        CommentInfoDto thirdCommentInfoDto = commentService.saveComment(thirdCommentRequest, member.getId());

        // 댓글 생성 - 첫번째 댓글에 자식댓글 1개, 세번째 댓글에 자식댓글 2개
        CreateCommentRequest firstCommentChildRequest = createCommentRequest(member, postId, "첫번째 댓글의 자식 댓글", commentInfoDto.getId());
        CreateCommentRequest thirdCommentFirstChildRequest = createCommentRequest(member, postId, "세번째 댓글의 첫번째 자식 댓글", thirdCommentInfoDto.getId());
        CreateCommentRequest thirdCommentSecondChildRequest = createCommentRequest(member, postId, "세번째 댓글의 두번째 자식 댓글", thirdCommentInfoDto.getId());

        CommentInfoDto firstChildInfoDto = commentService.saveComment(firstCommentChildRequest, member.getId());
        CommentInfoDto secondChildInfoDto = commentService.saveComment(thirdCommentFirstChildRequest, member.getId());
        CommentInfoDto thirdChildInfoDto = commentService.saveComment(thirdCommentSecondChildRequest, member.getId());

        //then
        // 검증 1 - 댓글 전체
        List<Comment> comments = commentRepository.findByPostId(postId);
        assertThat(comments).extracting("content")
                .hasSize(6)
                .contains("첫번째 댓글", "두번째 댓글", "세번째 댓글", "첫번째 댓글의 자식 댓글", "세번째 댓글의 첫번째 자식 댓글", "세번째 댓글의 두번째 자식 댓글");


        // 검증 2 - 자식 댓글 1
        List<Comment> firstCommentChilds = commentRepository.findByParentId(commentInfoDto.getId());
        assertThat(firstCommentChilds).extracting("content")
                .hasSize(1).contains("첫번째 댓글의 자식 댓글");

        // 검증 3 - 자식 댓글 2
        List<Comment> thirdCommentChilds = commentRepository.findByParentId(thirdCommentInfoDto.getId());
        assertThat(thirdCommentChilds).extracting("content")
                .hasSize(2).contains("세번째 댓글의 첫번째 자식 댓글", "세번째 댓글의 두번째 자식 댓글");

    }

    @Test
    @DisplayName("After QueryDsl - 게시글Id로 댓글 조회")
    public void findCommentByPostIdWithQueryDSL() throws Exception {
        //given
        Long postId = createPost();
        Post post = postRepository.findById(postId).get();
        Member member = post.getAuthor();
        CreateCommentRequest commentRequest = createCommentRequest(member, postId, "첫번째 댓글", null);
        CreateCommentRequest secondCommentRequest = createCommentRequest(member, postId, "두번째 댓글", null);
        CreateCommentRequest thirdCommentRequest = createCommentRequest(member, postId, "세번째 댓글", null);

        //when
        CommentInfoDto commentInfoDto = commentService.saveComment(commentRequest, member.getId());
        CommentInfoDto secondCommentInfoDto = commentService.saveComment(secondCommentRequest, member.getId());
        CommentInfoDto thirdCommentInfoDto = commentService.saveComment(thirdCommentRequest, member.getId());

        CreateCommentRequest firstCommentChildRequest = createCommentRequest(member, postId, "첫번째 댓글의 자식 댓글", commentInfoDto.getId());
        CreateCommentRequest thirdCommentFirstChildRequest = createCommentRequest(member, postId, "세번째 댓글의 첫번째 자식 댓글", thirdCommentInfoDto.getId());
        CreateCommentRequest thirdCommentSecondChildRequest = createCommentRequest(member, postId, "세번째 댓글의 두번째 자식 댓글", thirdCommentInfoDto.getId());

        CommentInfoDto firstChildInfoDto = commentService.saveComment(firstCommentChildRequest, member.getId());
        CommentInfoDto secondChildInfoDto = commentService.saveComment(thirdCommentFirstChildRequest, member.getId());
        CommentInfoDto thirdChildInfoDto = commentService.saveComment(thirdCommentSecondChildRequest, member.getId());

        //then
        List<CommentInfoDto> resultList = commentService.findCommentDtosByPostId(postId);
        assertThat(resultList).extracting("content")
                .hasSize(6)
                .contains("첫번째 댓글", "두번째 댓글", "세번째 댓글", "첫번째 댓글의 자식 댓글", "세번째 댓글의 첫번째 자식 댓글", "세번째 댓글의 두번째 자식 댓글");
    }

    @Test
    @WithMockCustomUser
    @DisplayName("댓글 업데이트 테스트 - 부모댓글 수정")
    public void updateParentCommentTestForSuccess() throws Exception {
        //given
        Long postId = createPost();
        Post post = postRepository.findById(postId).get();
        Member member = post.getAuthor();
        CreateCommentRequest commentRequest = createCommentRequest(member, postId, "첫번째 댓글", null);
        CreateCommentRequest secondCommentRequest = createCommentRequest(member, postId, "두번째 댓글", null);
        CreateCommentRequest thirdCommentRequest = createCommentRequest(member, postId, "세번째 댓글", null);
        CommentInfoDto commentInfoDto = commentService.saveComment(commentRequest, member.getId());
        CommentInfoDto secondCommentInfoDto = commentService.saveComment(secondCommentRequest, member.getId());
        CommentInfoDto thirdCommentInfoDto = commentService.saveComment(thirdCommentRequest, member.getId());

        //when
        UpdateCommentRequest secondCommentUpdate = createUpdateCommentRequest(secondCommentInfoDto.getId(), secondCommentRequest.getParentCommentId(), member.getId(), postId, "두번째 댓글 수정");
        CommentInfoDto updatedSecondComment = commentService.updateComment(secondCommentUpdate, member.getId());

        //then
        List<Comment> comments = commentRepository.findByPostId(postId);
        assertThat(comments).extracting("content")
                .hasSize(3).contains("첫번째 댓글", "두번째 댓글 수정", "세번째 댓글");

    }

    @Test
    @DisplayName("댓글 업데이트 테스트 - 자식댓글 수정")
    public void updateChildCommentTestForSuccess() throws Exception {
        //given
        Long postId = createPost();
        Post post = postRepository.findById(postId).get();
        Member member = post.getAuthor();
        CreateCommentRequest commentRequest = createCommentRequest(member, postId, "첫번째 댓글", null);
        CreateCommentRequest secondCommentRequest = createCommentRequest(member, postId, "두번째 댓글", null);
        CreateCommentRequest thirdCommentRequest = createCommentRequest(member, postId, "세번째 댓글", null);
        CommentInfoDto commentInfoDto = commentService.saveComment(commentRequest, member.getId());
        CommentInfoDto secondCommentInfoDto = commentService.saveComment(secondCommentRequest, member.getId());
        CommentInfoDto thirdCommentInfoDto = commentService.saveComment(thirdCommentRequest, member.getId());

        //when
        CreateCommentRequest thirdCommentFirstChildRequest = createCommentRequest(member, postId, "세번째 댓글의 자식 댓글", thirdCommentInfoDto.getId());
        CommentInfoDto childCommentInfoDto = commentService.saveComment(thirdCommentFirstChildRequest, member.getId());
        UpdateCommentRequest childCommentUpdateRequest = createUpdateCommentRequest(childCommentInfoDto.getId(), childCommentInfoDto.getParentCommentId(), member.getId(), postId, "세번째 댓글의 자식 댓글 수정");
        CommentInfoDto updatedCommentInfo = commentService.updateComment(childCommentUpdateRequest, member.getId());

        //then
        List<Comment> comments = commentRepository.findByPostId(postId);
        assertThat(comments).extracting("content").hasSize(4)
                .contains("첫번째 댓글", "두번째 댓글", "세번째 댓글", "세번째 댓글의 자식 댓글 수정");

        List<Comment> childCommentsOfThirdComment = commentRepository.findByParentId(thirdCommentInfoDto.getId());
        assertThat(childCommentsOfThirdComment).extracting("content").hasSize(1).contains("세번째 댓글의 자식 댓글 수정");
    }


    @Test
    @DisplayName("댓글 삭제 테스트 - 자식댓글 단일 삭제")
    public void deleteSingleChildCommentForSuccess() throws Exception {
        //given
        Long postId = createPost();
        Post post = postRepository.findById(postId).get();
        Member member = post.getAuthor();
        CreateCommentRequest commentRequest = createCommentRequest(member, postId, "첫번째 댓글", null);
        CreateCommentRequest secondCommentRequest = createCommentRequest(member, postId, "두번째 댓글", null);
        CreateCommentRequest thirdCommentRequest = createCommentRequest(member, postId, "세번째 댓글", null);
        CommentInfoDto commentInfoDto = commentService.saveComment(commentRequest, member.getId());
        CommentInfoDto secondCommentInfoDto = commentService.saveComment(secondCommentRequest, member.getId());
        CommentInfoDto thirdCommentInfoDto = commentService.saveComment(thirdCommentRequest, member.getId());
        CreateCommentRequest thirdCommentFirstChildRequest = createCommentRequest(member, postId, "세번째 댓글의 자식 댓글", thirdCommentInfoDto.getId());
        CommentInfoDto childCommentInfoDto = commentService.saveComment(thirdCommentFirstChildRequest, member.getId());

        //when
        DeleteCommentRequest deleteCommentRequest = createDeleteCommentRequest(childCommentInfoDto.getId(), member.getId(), postId);
        String result = commentService.deleteComment(deleteCommentRequest, member.getId());

        //then
        List<Comment> comments = commentRepository.findByPostId(postId);
        assertThat(comments).extracting("content").hasSize(3).contains("첫번째 댓글", "두번째 댓글", "세번째 댓글");

        List<Comment> emptyList = commentRepository.findByParentId(thirdCommentInfoDto.getId());
        assertThat(emptyList).isEmpty();
    }

//    @Test
//    @DisplayName("댓글 삭제 테스트 - 부모댓글 통합 삭제")
//    public void deleteTotalParentCommentForSuccess() throws Exception {
//        //given
//        Long postId = createPost();
//        Post post = postRepository.findById(postId).get();
//        Member member = post.getAuthor();
//        CreateCommentRequest commentRequest = createCommentRequest(member, postId, "첫번째 댓글", null);
//        CreateCommentRequest secondCommentRequest = createCommentRequest(member, postId, "두번째 댓글", null);
//        CreateCommentRequest thirdCommentRequest = createCommentRequest(member, postId, "세번째 댓글", null);
//        CommentInfoDto commentInfoDto = commentService.saveComment(commentRequest, member.getId());
//        CommentInfoDto secondCommentInfoDto = commentService.saveComment(secondCommentRequest, member.getId());
//        CommentInfoDto thirdCommentInfoDto = commentService.saveComment(thirdCommentRequest, member.getId());
//        CreateCommentRequest firstCommentFirstChildRequest = createCommentRequest(member, postId, "첫번째 댓글의 첫번째 자식 댓글", commentInfoDto.getId());
//        CreateCommentRequest firstCommentSecondChildRequest = createCommentRequest(member, postId, "첫번째 댓글의 두번째 자식 댓글", commentInfoDto.getId());
//        CreateCommentRequest firstCommentThirdChildRequest = createCommentRequest(member, postId, "첫번째 댓글의 세번째 자식 댓글", commentInfoDto.getId());
//        CommentInfoDto childCommentInfoDto = commentService.saveComment(firstCommentFirstChildRequest, member.getId());
//        CommentInfoDto secondChildCommentInfoDto = commentService.saveComment(firstCommentSecondChildRequest, member.getId());
//        CommentInfoDto thirdChildCommentInfoDto = commentService.saveComment(firstCommentThirdChildRequest, member.getId());
//
//        //when
//        DeleteCommentRequest deleteFirstCommentRequest = createDeleteCommentRequest(commentInfoDto.getId(), member.getId(), postId);
//        commentService.deleteComment(deleteFirstCommentRequest, member.getId());
//
//        //then
//        List<Comment> comments = commentRepository.findByPostId(postId);
//        assertThat(comments).extracting("content").hasSize(2).contains("두번째 댓글", "세번째 댓글");
//    }
}
