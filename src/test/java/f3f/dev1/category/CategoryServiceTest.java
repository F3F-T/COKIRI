package f3f.dev1.category;

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
import f3f.dev1.domain.model.Address;
import f3f.dev1.domain.post.dao.PostRepository;
import f3f.dev1.domain.scrap.dao.ScrapRepository;
import f3f.dev1.domain.tag.dao.TagRepository;
import f3f.dev1.domain.trade.dao.TradeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryServiceTest {
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


    @BeforeEach
    public void deleteAll(){
        memberRepository.deleteAll();
        postRepository.deleteAll();
        messageRepository.deleteAll();
        categoryRepository.deleteAll();
        tradeRepository.deleteAll();
        scrapRepository.deleteAll();
        tradeRepository.deleteAll();
    }


    @Autowired
    MemberService memberService;

    @Autowired
    AuthService authService;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Autowired
    EmailCertificationService emailCertificationService;


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
                .address(createAddress())
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

    private CategoryDTO.CategorySaveRequest createCategoryDto(String name, Long memberId, Long depth, Long parentId){
        CategoryDTO.CategorySaveRequest saveRequest = new CategoryDTO.CategorySaveRequest(name, memberId, depth, parentId);
        return saveRequest;
    }

    @Test
    @DisplayName("카테고리 생성 테스트")
    public void saveCategoryTest() throws Exception{
        //given
        MemberDTO.SignUpRequest signUpRequest = createSignUpRequest();
        authService.signUp(signUpRequest);
        Member member = memberRepository.findByEmail(signUpRequest.getEmail()).get();
        CategoryDTO.CategorySaveRequest categoryDTO = createCategoryDto("물물교환", member.getId(),1L, null);
       //when
        Long cid = categoryService.createCategory(categoryDTO);
        Category category = categoryRepository.findById(cid).get();
        //then
        assertThat(categoryRepository.findCategoryByName("root"));
    }

}
