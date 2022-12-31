package f3f.dev1.tag;

import f3f.dev1.domain.category.application.CategoryService;
import f3f.dev1.domain.category.dao.CategoryRepository;
import f3f.dev1.domain.category.dto.CategoryDTO;
import f3f.dev1.domain.category.model.Category;
import f3f.dev1.domain.member.application.AuthService;
import f3f.dev1.domain.member.dao.MemberRepository;
import f3f.dev1.domain.member.dto.MemberDTO;
import f3f.dev1.domain.member.model.Member;
import f3f.dev1.domain.model.Address;
import f3f.dev1.domain.post.application.PostService;
import f3f.dev1.domain.post.dao.PostRepository;
import f3f.dev1.domain.post.dto.PostDTO;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.tag.application.TagService;
import f3f.dev1.domain.tag.dao.PostTagRepository;
import f3f.dev1.domain.tag.dao.TagRepository;
import f3f.dev1.domain.tag.dto.TagDTO;
import f3f.dev1.domain.tag.model.PostTag;
import f3f.dev1.domain.tag.model.Tag;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static f3f.dev1.domain.category.dto.CategoryDTO.*;
import static f3f.dev1.domain.member.dto.MemberDTO.*;
import static f3f.dev1.domain.member.model.UserLoginType.EMAIL;
import static f3f.dev1.domain.post.dto.PostDTO.*;
import static f3f.dev1.domain.tag.dto.TagDTO.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class TagServiceTest {
    @Autowired
    PostService postService;

    @Autowired
    TagService tagService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    AuthService authService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    PostTagRepository postTagRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @BeforeEach
    public void deleteAll() {
        memberRepository.deleteAll();
        postRepository.deleteAll();
        categoryRepository.deleteAll();
        tagRepository.deleteAll();
        postTagRepository.deleteAll();
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

    public CreateTagRequest createTagRequest(String tagName, Long authorId) {
        return new CreateTagRequest(tagName, authorId);
    }

    public AddTagToPostRequest createAddTagToPostRequest(Long tagId, Long postId) {
        return new AddTagToPostRequest(tagId, postId);
    }

    @Test
    @DisplayName("태그 생성 테스트")
    public void createTagTestForSuccess() throws Exception {
        //given
        SignUpRequest signUpRequest = createSignUpRequest();
        authService.signUp(signUpRequest);
        Member member = memberRepository.findByEmail(signUpRequest.getEmail()).get();

        //when
        CreateTagRequest tagRequest = createTagRequest("해시태그1", member.getId());
        Long tagId = tagService.createTag(tagRequest);

        //then
        Tag tag = tagRepository.findById(tagId).get();
        assertThat(tag.getName()).isEqualTo("해시태그1");
    }

    @Test
    @Rollback
    @DisplayName("태그 게시글 추가 테스트")
    public void createTagToPostTestForSuccess() throws Exception {
        //given
        SignUpRequest signUpRequest = createSignUpRequest();
        authService.signUp(signUpRequest);
        Member member = memberRepository.findByEmail(signUpRequest.getEmail()).get();
        CreateTagRequest tagRequest = createTagRequest("해시태그1", member.getId());
        Long tagId = tagService.createTag(tagRequest);

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
        PostSaveRequest postSaveRequest = createPostSaveRequest(member, false, productCategoryId, wishCategoryId);
        Long postId = postService.savePost(postSaveRequest);
        Post post = postRepository.findById(postId).get();
        AddTagToPostRequest addTagToPostRequest = createAddTagToPostRequest(tagId, postId);
        Long postTagId = tagService.addTagToPost(addTagToPostRequest);
        PostTag postTag = postTagRepository.findById(postTagId).get();

        //then
        assertThat(postTag.getPost().getTitle()).isEqualTo("3년 신은 양말 거래 희망합니다");
        assertThat(postTag.getPost().getContent()).isEqualTo("냄새가 조금 나긴 하는데 뭐 그럭저럭 괜찮아요");
        assertThat(postTag.getTag().getName()).isEqualTo("해시태그1");
    }

    @Test
    @DisplayName("단일 태그 이름으로 게시글 조회 테스트")
    public void getPostsBySingleTagName() throws Exception {
        //given
        SignUpRequest signUpRequest = createSignUpRequest();
        authService.signUp(signUpRequest);
        Member member = memberRepository.findByEmail(signUpRequest.getEmail()).get();
        CreateTagRequest tagRequest = createTagRequest("해시태그1", member.getId());
        Long tagId = tagService.createTag(tagRequest);

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
        PostSaveRequest postSaveRequest = createPostSaveRequest(member, false, productCategoryId, wishCategoryId);
        Long postId = postService.savePost(postSaveRequest);
        Post post = postRepository.findById(postId).get();
        AddTagToPostRequest addTagToPostRequest = createAddTagToPostRequest(tagId, postId);
        Long postTagId = tagService.addTagToPost(addTagToPostRequest);
        PostTag postTag = postTagRepository.findById(postTagId).get();

        PostSaveRequest secondPostSaveRequest = createPostSaveRequestWithDynamicTitle(member, "두번째 게시글", false, productCategoryId, wishCategoryId);
        Long secondPostId = postService.savePost(secondPostSaveRequest);
        Post secondPost = postRepository.findById(secondPostId).get();

        //then
        List<PostInfoDto> result = tagService.getPostsByTagName("해시태그1");
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getTitle()).isEqualTo("3년 신은 양말 거래 희망합니다");
        assertThat(result).extracting("content")
                .hasSize(1)
                .contains("냄새가 조금 나긴 하는데 뭐 그럭저럭 괜찮아요");
    }

    @Test
    @DisplayName("복수 태그 이름으로 게시글 조회 테스트")
    public void getPostsByMultiTagNames() throws Exception {
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
        Long postId = postService.savePost(postSaveRequest);
        Post post = postRepository.findById(postId).get();
        AddTagToPostRequest addTagToPostRequest = createAddTagToPostRequest(tagId, postId);
        AddTagToPostRequest addTagToPostSecondRequest = createAddTagToPostRequest(secondTagId, postId);
        AddTagToPostRequest addTagToPostThirdRequest = createAddTagToPostRequest(thirdTagId, postId);
        tagService.addTagToPost(addTagToPostRequest);
        tagService.addTagToPost(addTagToPostSecondRequest);
        tagService.addTagToPost(addTagToPostThirdRequest);

        PostSaveRequest secondPostSaveRequest = createPostSaveRequestWithDynamicTitle(member, "두번째 게시글", false, productCategoryId, wishCategoryId);
        Long secondPostId = postService.savePost(secondPostSaveRequest);
        Post secondPost = postRepository.findById(secondPostId).get();

        //then
        List<String> names = new ArrayList<>();
        names.add("해시태그1");
        names.add("해시태그2");
        names.add("해시태그3");

        List<PostInfoDto> postInfoDtoList = tagService.getPostsByTagNames(names);
        assertThat(postInfoDtoList).extracting("title")
                .hasSize(1)
                .contains("3년 신은 양말 거래 희망합니다");

        assertThat(postInfoDtoList).extracting("content")
                .hasSize(1)
                .contains("냄새가 조금 나긴 하는데 뭐 그럭저럭 괜찮아요");
    }
}
