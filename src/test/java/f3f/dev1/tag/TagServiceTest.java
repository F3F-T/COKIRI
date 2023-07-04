package f3f.dev1.tag;

import f3f.dev1.domain.address.model.Address;
import f3f.dev1.domain.category.application.CategoryService;
import f3f.dev1.domain.category.dao.CategoryRepository;
import f3f.dev1.domain.category.model.Category;
import f3f.dev1.domain.member.application.AuthService;
import f3f.dev1.domain.member.dao.MemberRepository;
import f3f.dev1.domain.member.model.Member;
import f3f.dev1.domain.post.application.PostService;
import f3f.dev1.domain.post.dao.PostRepository;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.tag.application.TagService;
import f3f.dev1.domain.tag.dao.PostTagRepository;
import f3f.dev1.domain.tag.dao.TagRepository;
import f3f.dev1.domain.tag.model.PostTag;
import f3f.dev1.domain.tag.model.Tag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static f3f.dev1.domain.category.dto.CategoryDTO.CategorySaveRequest;
import static f3f.dev1.domain.member.dto.MemberDTO.SignUpRequest;
import static f3f.dev1.domain.member.model.UserLoginType.EMAIL;
import static f3f.dev1.domain.post.dto.PostDTO.*;
import static f3f.dev1.domain.tag.dto.TagDTO.AddTagToPostRequest;
import static f3f.dev1.domain.tag.dto.TagDTO.CreateTagRequest;
import static org.assertj.core.api.Assertions.assertThat;

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

    public PostSaveRequest createPostSaveRequestWithDynamicTitle(Member author, String title, boolean tradeEachOther, String productName, String wishName) {
        return PostSaveRequest.builder()
                .content("테스트용 게시글 본문")
                .title(title)
                .tradeEachOther(tradeEachOther)
                .authorId(author.getId())
                .productCategory(productName)
                .tagNames(new ArrayList<>())
                .wishCategory(wishName)
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
//        CategorySaveRequest rootRequest = createCategorySaveRequest("root", 0L, null, member);
//        Long rootId = categoryService.createCategory(rootRequest);
//        Category root = categoryRepository.findById(rootId).get();
//        // product, wish 생성
//        CategorySaveRequest productRequest = createCategorySaveRequest("도서", 1L, rootId, member);
//        CategorySaveRequest wishRequest = createCategorySaveRequest("전자기기", 1L, rootId, member);
//        Long productCategoryId = categoryService.createCategory(productRequest);
//        Long wishCategoryId = categoryService.createCategory(wishRequest);

        //when
        // 게시글 추가 + 게시글에 태그 추가
        PostSaveRequest postSaveRequest = createPostSaveRequest(member, false,"도서", "전자기기");
        Long postId = postService.savePost(postSaveRequest, member.getId());
        Post post = postRepository.findById(postId).get();
        AddTagToPostRequest addTagToPostRequest = createAddTagToPostRequest(tagId, postId);
        Long postTagId = tagService.addTagToPost(addTagToPostRequest);
        PostTag postTag = postTagRepository.findById(postTagId).get();

        //then
        assertThat(postTag.getPost().getTitle()).isEqualTo("테스트용 게시글 제목");
        assertThat(postTag.getPost().getContent()).isEqualTo("테스트용 게시글 본문");
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
//        CategorySaveRequest rootRequest = createCategorySaveRequest("root", 0L, null, member);
//        Long rootId = categoryService.createCategory(rootRequest);
//        Category root = categoryRepository.findById(rootId).get();
//        // product, wish 생성
//        CategorySaveRequest productRequest = createCategorySaveRequest("도서", 1L, rootId, member);
//        CategorySaveRequest wishRequest = createCategorySaveRequest("전자기기", 1L, rootId, member);
//        Long productCategoryId = categoryService.createCategory(productRequest);
//        Long wishCategoryId = categoryService.createCategory(wishRequest);

        //when
        // 게시글 추가 + 게시글에 태그 추가
        PostSaveRequest postSaveRequest = createPostSaveRequest(member, false, "도서", "전자기기");
        Long postId = postService.savePost(postSaveRequest, member.getId());
        Post post = postRepository.findById(postId).get();
        AddTagToPostRequest addTagToPostRequest = createAddTagToPostRequest(tagId, postId);
        Long postTagId = tagService.addTagToPost(addTagToPostRequest);
        PostTag postTag = postTagRepository.findById(postTagId).get();

        PostSaveRequest secondPostSaveRequest = createPostSaveRequestWithDynamicTitle(member, "두번째 게시글", false, "도서", "전자기기");
        Long secondPostId = postService.savePost(secondPostSaveRequest, member.getId());
        Post secondPost = postRepository.findById(secondPostId).get();

        //then
        List<PostInfoDto> result = tagService.getPostsByTagName("해시태그1");
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getTitle()).isEqualTo("테스트용 게시글 제목");
        assertThat(result).extracting("content")
                .hasSize(1)
                .contains("테스트용 게시글 본문");
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
//        CategorySaveRequest rootRequest = createCategorySaveRequest("root", 0L, null, member);
//        Long rootId = categoryService.createCategory(rootRequest);
//        Category root = categoryRepository.findById(rootId).get();
//        // product, wish 생성
//        CategorySaveRequest productRequest = createCategorySaveRequest("도서", 1L, rootId, member);
//        CategorySaveRequest wishRequest = createCategorySaveRequest("전자기기", 1L, rootId, member);
//        Long productCategoryId = categoryService.createCategory(productRequest);
//        Long wishCategoryId = categoryService.createCategory(wishRequest);

        //when
        // 게시글 추가 + 게시글에 태그 추가
        // 첫번째 게시글에 해시태그 1, 2, 3이 모두 추가되어있다.
        PostSaveRequest postSaveRequest = createPostSaveRequest(member, false, "도서", "전자기기");
        Long postId = postService.savePost(postSaveRequest, member.getId());
        Post post = postRepository.findById(postId).get();
        AddTagToPostRequest addTagToPostRequest = createAddTagToPostRequest(tagId, postId);
        AddTagToPostRequest addTagToPostSecondRequest = createAddTagToPostRequest(secondTagId, postId);
        AddTagToPostRequest addTagToPostThirdRequest = createAddTagToPostRequest(thirdTagId, postId);
        tagService.addTagToPost(addTagToPostRequest);
        tagService.addTagToPost(addTagToPostSecondRequest);
        tagService.addTagToPost(addTagToPostThirdRequest);

        PostSaveRequest secondPostSaveRequest = createPostSaveRequestWithDynamicTitle(member, "두번째 게시글", false, "도서", "전자기기");
        Long secondPostId = postService.savePost(secondPostSaveRequest, member.getId());
        Post secondPost = postRepository.findById(secondPostId).get();

        //then
        List<String> names = new ArrayList<>();
        names.add("해시태그1");
        names.add("해시태그2");
        names.add("해시태그3");

        List<PostInfoDtoWithTag> postInfoDtoList = tagService.getPostsByTagNames(names);
        assertThat(postInfoDtoList).extracting("title")
                .hasSize(1)
                .contains("테스트용 게시글 제목");

        assertThat(postInfoDtoList).extracting("content")
                .hasSize(1)
                .contains("테스트용 게시글 본문");
    }

    @Test
    @DisplayName("복수 태그 이름으로 게시글 조회 - 세부 테스트")
    public void getPostsByMultiTagNamesInDetailForSuccess() throws Exception {
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
//        CategorySaveRequest rootRequest = createCategorySaveRequest("root", 0L, null, member);
//        Long rootId = categoryService.createCategory(rootRequest);
//        Category root = categoryRepository.findById(rootId).get();
//        // product, wish 생성
//        CategorySaveRequest productRequest = createCategorySaveRequest("도서", 1L, rootId, member);
//        CategorySaveRequest wishRequest = createCategorySaveRequest("전자기기", 1L, rootId, member);
//        Long productCategoryId = categoryService.createCategory(productRequest);
//        Long wishCategoryId = categoryService.createCategory(wishRequest);

        //when
        // 게시글 추가 + 게시글에 태그 추가
        // 첫번째 게시글에 해시태그 1, 2, 3이 모두 추가되어있다.
        PostSaveRequest postSaveRequest = createPostSaveRequest(member, false, "도서", "전자기기");
        Long postId = postService.savePost(postSaveRequest, member.getId());
        Post post = postRepository.findById(postId).get();
        AddTagToPostRequest addTagToPostRequest = createAddTagToPostRequest(tagId, postId);
        AddTagToPostRequest addTagToPostSecondRequest = createAddTagToPostRequest(secondTagId, postId);
        AddTagToPostRequest addTagToPostThirdRequest = createAddTagToPostRequest(thirdTagId, postId);
        tagService.addTagToPost(addTagToPostRequest);
        tagService.addTagToPost(addTagToPostSecondRequest);
        tagService.addTagToPost(addTagToPostThirdRequest);

        // 두번째 게시글에 해시태그 1, 2가 추가됐다.
        PostSaveRequest secondPostSaveRequest = createPostSaveRequestWithDynamicTitle(member, "두번째 게시글", false, "도서", "전자기기");
        Long secondPostId = postService.savePost(secondPostSaveRequest, member.getId());
        Post secondPost = postRepository.findById(secondPostId).get();
        AddTagToPostRequest addTagToSecondPostRequest = createAddTagToPostRequest(tagId, secondPostId);
        AddTagToPostRequest addSecondTagToSecondPostRequest = createAddTagToPostRequest(secondTagId, secondPostId);
        tagService.addTagToPost(addTagToSecondPostRequest);
        tagService.addTagToPost(addSecondTagToSecondPostRequest);

        // 세번째 게시글에 해시태그1이 추가되었다.
        PostSaveRequest thirdPostSaveRequest = createPostSaveRequestWithDynamicTitle(member, "세번째 게시글", false, "도서", "전자기기");
        Long thirdPostId = postService.savePost(thirdPostSaveRequest, member.getId());
        Post thirdPost = postRepository.findById(thirdPostId).get();
        AddTagToPostRequest addTagToThirdPostRequest = createAddTagToPostRequest(tagId, thirdPostId);
        tagService.addTagToPost(addTagToThirdPostRequest);

        // 네번째 게시글에는 해시태그를 추가하지 않겠다.
        PostSaveRequest fourthPostSaveRequest = createPostSaveRequestWithDynamicTitle(member, "네번째 게시글", false, "도서", "전자기기");
        Long fourthPostId = postService.savePost(fourthPostSaveRequest, member.getId());
        Post fourthPost = postRepository.findById(fourthPostId).get();

        //then
        List<String> names = new ArrayList<>();
        names.add("해시태그1");
        names.add("해시태그2");
        names.add("해시태그3");

        List<PostInfoDtoWithTag> postInfoDtoList = tagService.getPostsByTagNames(names);
        assertThat(postInfoDtoList).extracting("title")
                .contains("테스트용 게시글 제목");

        assertThat(postInfoDtoList).extracting("content")
                .contains("테스트용 게시글 본문");

        List<String> secondNames = new ArrayList<>();
        secondNames.add("해시태그1");
        secondNames.add("해시태그2");

        List<PostInfoDtoWithTag> secondPostInfoDtoList = tagService.getPostsByTagNames(secondNames);
        assertThat(secondPostInfoDtoList).extracting("title")
                .contains("두번째 게시글", "테스트용 게시글 제목");

        List<String> thirdNames = new ArrayList<>();
        thirdNames.add("해시태그1");

        List<PostInfoDtoWithTag> thirdPostInfoDtoList = tagService.getPostsByTagNames(thirdNames);
        assertThat(thirdPostInfoDtoList).extracting("title")
                .contains("세번째 게시글", "두번째 게시글", "테스트용 게시글 제목");

        // 비어있는 태그 이름 리스트를 넘기면 게시글 전체 조회
        List<String> emptyNamesList = new ArrayList<>();
        List<PostInfoDtoWithTag> fourthPostInfoDtoList = tagService.getPostsByTagNames(emptyNamesList);
        assertThat(fourthPostInfoDtoList).extracting("title")
                .contains("네번째 게시글", "세번째 게시글", "두번째 게시글", "테스트용 게시글 제목");
    }
}
