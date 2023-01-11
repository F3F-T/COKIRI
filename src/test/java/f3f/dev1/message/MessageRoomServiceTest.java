package f3f.dev1.message;

import f3f.dev1.domain.category.application.CategoryService;
import f3f.dev1.domain.category.dao.CategoryRepository;
import f3f.dev1.domain.category.dto.CategoryDTO;
import f3f.dev1.domain.category.model.Category;
import f3f.dev1.domain.member.application.AuthService;
import f3f.dev1.domain.member.application.EmailCertificationService;
import f3f.dev1.domain.member.application.MemberService;
import f3f.dev1.domain.member.dao.MemberRepository;
import f3f.dev1.domain.member.dto.MemberDTO;
import f3f.dev1.domain.member.model.Member;
import f3f.dev1.domain.message.dao.MessageRepository;
import f3f.dev1.domain.message.dto.MessageRoomDTO;
import f3f.dev1.domain.model.Address;
import f3f.dev1.domain.post.application.PostService;
import f3f.dev1.domain.post.dao.PostRepository;
import f3f.dev1.domain.post.dto.PostDTO;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.scrap.dao.ScrapRepository;
import f3f.dev1.domain.tag.dao.TagRepository;
import f3f.dev1.domain.trade.dao.TradeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class MessageRoomServiceTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    CategoryService categoryService;
    @Autowired
    TradeRepository tradeRepository;
    @Autowired
    ScrapRepository scrapRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    MemberService memberService;
    @Autowired
    PostService postService;

    @Autowired
    AuthService authService;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Autowired
    EmailCertificationService emailCertificationService;



    @BeforeEach
    public void deleteAll() {
        memberRepository.deleteAll();
        postRepository.deleteAll();
        messageRepository.deleteAll();
        categoryRepository.deleteAll();
        tradeRepository.deleteAll();
        scrapRepository.deleteAll();
        tradeRepository.deleteAll();
    }

    // 주소 오브젝트 생성
    public Address createAddress() {
        return Address.builder()
                .addressName("address")
                .postalAddress("13556")
                .latitude("37.49455")
                .longitude("127.12170")
                .build();
    }

    // 회원가입 DTO 생성 메소드
    public MemberDTO.SignUpRequest createSignUpRequest() {
        return MemberDTO.SignUpRequest.builder()
                .userName("username")
                .nickname("nickname")
                .phoneNumber("01012345678")
                .email("userEmail@email.com")
                .birthDate("990128")
                .password("password")
                .build();
    }

    // 로그인 DTO 생성 메소드
    public MemberDTO.LoginRequest createLoginRequest() {
        return MemberDTO.LoginRequest.builder()
                .email("userEmail@email.com")
                .password("password").build();
    }

    // 업데이트 DTO 생성 메소드
    public MemberDTO.UpdateUserInfo createUpdateRequest() {
        return MemberDTO.UpdateUserInfo.builder()
                .address(createAddress())
                .nickname("newNickname")
                .phoneNumber("01088888888")
                .build();
    }
    public PostDTO.PostSaveRequest createPostSaveRequest(Member author, boolean tradeEachOther, String productName, String wishName) {
        return PostDTO.PostSaveRequest.builder()
                .content("냄새가 조금 나긴 하는데 뭐 그럭저럭 괜찮아요")
                .title("3년 신은 양말 거래 희망합니다")
                .tradeEachOther(tradeEachOther)
                .authorId(author.getId())
                .productCategory(productName)
                .tagNames(new ArrayList<>())
                .wishCategory(wishName)
                .build();
    }
    private CategoryDTO.CategorySaveRequest createCategoryDto(String name, Long memberId, Long depth, Long parentId) {
        CategoryDTO.CategorySaveRequest saveRequest = new CategoryDTO.CategorySaveRequest(name, memberId, depth, parentId);
        return saveRequest;
    }

    private CategoryDTO.CategorySaveRequest createWishCategoryDto(String name, Long memberId, Long depth, Long parentId) {
        CategoryDTO.CategorySaveRequest saveRequest = new CategoryDTO.CategorySaveRequest(name, memberId, depth, parentId);
        return saveRequest;
    }
    private MessageRoomDTO.MessageRoomSaveRequest messageRoomSaveRequest (Long postId, Long sellerId, Long buyerId){
        MessageRoomDTO.MessageRoomSaveRequest saveRequest = new MessageRoomDTO.MessageRoomSaveRequest(postId, sellerId, buyerId);
        return saveRequest;
    }

    @Test
    @DisplayName("메시지룸 생성 테스트")
    public void createMessageRoomTest() throws Exception{
        MemberDTO.SignUpRequest signUpRequest = createSignUpRequest();
        authService.signUp(signUpRequest);
        Member admin = memberRepository.findByEmail(signUpRequest.getEmail()).get();
//        MemberDTO.SignUpRequest signUpRequest1 = createSignUpRequest();
//        authService.signUp(signUpRequest1);
//        Member member = memberRepository.findByEmail(signUpRequest1.getEmail()).get();

        CategoryDTO.CategorySaveRequest categoryDTO1 = createCategoryDto("도서", admin.getId(), 1L, null);
        Long cid1 = categoryService.createCategory(categoryDTO1);
        Category category1 = categoryRepository.findById(cid1).get();
        Category root = categoryRepository.findCategoryByName("root").get();
        CategoryDTO.CategorySaveRequest categoryDTO2 = createCategoryDto("주방", admin.getId(), 1L, root.getId());
        Long cid2 = categoryService.createCategory(categoryDTO2);
        Category category2 = categoryRepository.findById(cid2).get();
        PostDTO.PostSaveRequest postSaveRequest = createPostSaveRequest(admin, true, "도서", "도서");
        Long postId = postService.savePost(postSaveRequest, admin.getId());
        Post post = postRepository.findById(postId).get();

//        MessageRoomDTO.MessageRoomSaveRequest messageRoomDTO1 = messageRoomSaveRequest();


    }

}
