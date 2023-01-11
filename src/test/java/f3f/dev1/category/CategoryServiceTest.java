package f3f.dev1.category;

import f3f.dev1.domain.category.application.CategoryService;
import f3f.dev1.domain.category.dao.CategoryRepository;
import f3f.dev1.domain.category.dto.CategoryDTO;
import f3f.dev1.domain.category.exception.CanNotDeleteCategoryException;
import f3f.dev1.domain.category.exception.CategoryException;
import f3f.dev1.domain.category.exception.NotFoundCategoryByNameException;
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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
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
    public void deleteAll() {
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

    private CategoryDTO.CategorySaveRequest createCategoryDto(String name, Long memberId, Long depth, Long parentId) {
        CategoryDTO.CategorySaveRequest saveRequest = new CategoryDTO.CategorySaveRequest(name, memberId, depth, parentId);
        return saveRequest;
    }

    //--------------------------------카테고리 생성 테스트-----------------------------------------------------------
    @Test
    @DisplayName("(실패)카테고리 생성 테스트: 뎁스 초과")
    public void saveWrongDepthCategoryTest() throws Exception{
        //given
        MemberDTO.SignUpRequest signUpRequest = createSignUpRequest();
        authService.signUp(signUpRequest);
        Member admin = memberRepository.findByEmail(signUpRequest.getEmail()).get();
        CategoryDTO.CategorySaveRequest categoryDTO1 = createCategoryDto("도서", admin.getId(), 1L, null);
        Long cid1 = categoryService.createCategory(categoryDTO1);
        Category category1 = categoryRepository.findById(cid1).get();
        Category root = categoryRepository.findCategoryByName("root").get();
        CategoryDTO.CategorySaveRequest categoryDTO2 = createCategoryDto("아동용 도서", admin.getId(), 2L, category1.getId());

        //when & then
        assertThrows(CategoryException.class, ()->{
            Long cid2 = categoryService.createCategory(categoryDTO2);

        });
    }

    @Test
    @DisplayName("(실패)카테고리 생성 테스트: 중복된 이름")
    public void saveDuplicatedNameCategoryTest() throws Exception{
        //given
        MemberDTO.SignUpRequest signUpRequest = createSignUpRequest();
        authService.signUp(signUpRequest);
        Member admin = memberRepository.findByEmail(signUpRequest.getEmail()).get();
        CategoryDTO.CategorySaveRequest categoryDTO1 = createCategoryDto("도서", admin.getId(), 1L, null);
        Long cid1 = categoryService.createCategory(categoryDTO1);
        Category category1 = categoryRepository.findById(cid1).get();
        Category root = categoryRepository.findCategoryByName("root").get();
        CategoryDTO.CategorySaveRequest categoryDTO2 = createCategoryDto("도서", admin.getId(), 1L, category1.getId());

        //when & then
        assertThrows(CategoryException.class, ()->{
            Long cid2 = categoryService.createCategory(categoryDTO2);

        });
    }
    @Test
    @DisplayName("카테고리 생성 테스트:루트 생성 및 요청된 카테고리 만들기")
    public void saveRootAndCategoryTest() throws Exception {
        //given
        MemberDTO.SignUpRequest signUpRequest = createSignUpRequest();
        authService.signUp(signUpRequest);
        Member admin = memberRepository.findByEmail(signUpRequest.getEmail()).get();
        CategoryDTO.CategorySaveRequest categoryDTO1 = createCategoryDto("도서", admin.getId(), 1L, null);
        //when
        Long cid1 = categoryService.createCategory(categoryDTO1);
        Category category1 = categoryRepository.findById(cid1).get();
        Category root = categoryRepository.findCategoryByName("root").get();
        //then
        assertThat(categoryRepository.existsByName("root")).isEqualTo(true);
        assertThat(categoryRepository.existsByName("도서")).isEqualTo(true);
        //2번째 요청 시에 잘 만들어지는지
        CategoryDTO.CategorySaveRequest categoryDTO2 = createCategoryDto("주방", admin.getId(), 1L, root.getId());
        //when
        Long cid2 = categoryService.createCategory(categoryDTO2);
        Category category2 = categoryRepository.findById(cid2).get();
        CategoryDTO.CategorySaveRequest categoryDTO3 = createCategoryDto("취미", admin.getId(), 1L,root.getId());
        Long cid3 = categoryService.createCategory(categoryDTO3);
        Category category3 = categoryRepository.findById(cid3).get();
                //then
        assertThat(categoryRepository.existsByName("취미")).isEqualTo(true);
        assertThat(category2.getDepth()).isEqualTo(1);
        assertThat(root.getChild().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("(실패)카테고리 생성 테스트 : 부모에 널값 넣으면 오류 터지게 하기_루트는 이미 생성됨.")
    public void saveCategoryTest() throws Exception {
        //given
        MemberDTO.SignUpRequest signUpRequest = createSignUpRequest();
        authService.signUp(signUpRequest);
        Member admin = memberRepository.findByEmail(signUpRequest.getEmail()).get();
        CategoryDTO.CategorySaveRequest categoryDTO1 = createCategoryDto("도서", admin.getId(), 1L, null);
        Long cid1 = categoryService.createCategory(categoryDTO1);
        Category category1 = categoryRepository.findById(cid1).get();
        CategoryDTO.CategorySaveRequest categoryDTO2 = createCategoryDto("주방", admin.getId(), 1L, null);

        //when & then
        assertThrows(CategoryException.class, () -> {
            Long cid2 = categoryService.createCategory(categoryDTO2);
        });

    }

    //------------------------------카테고리 조회 테스트---------------------------------------
    @Test
    @DisplayName("카테고리 전체 조회 테스트")
    public void readCategoryTest() throws Exception {
        //given
        MemberDTO.SignUpRequest signUpRequest = createSignUpRequest();
        authService.signUp(signUpRequest);
        Member admin = memberRepository.findByEmail(signUpRequest.getEmail()).get();
        CategoryDTO.CategorySaveRequest categoryDTO1 = createCategoryDto("도서", admin.getId(), 1L, null);
        Long cid1 = categoryService.createCategory(categoryDTO1);
        Category root = categoryRepository.findCategoryByName("root").get();
        Category category1 = categoryRepository.findById(cid1).get();
        CategoryDTO.CategorySaveRequest categoryDTO2 = createCategoryDto("주방", admin.getId(), 1L, root.getId());
        Long cid2 = categoryService.createCategory(categoryDTO2);
        Category category2 = categoryRepository.findById(cid2).get();
        CategoryDTO.CategorySaveRequest categoryDTO3 = createCategoryDto("취미", admin.getId(), 1L, root.getId());
        Long cid3 = categoryService.createCategory(categoryDTO3);
        Category category3 = categoryRepository.findById(cid3).get();
        //when & then
        assertThat(categoryService.readTotalCategory().size()).isEqualTo(4);
    }


    //----------------------------------------카테고리 업데이트 테스트----------------------------------------------------
    @Test
    @DisplayName("카테고리 이름 업데이트 테스트")
    public void updateCategoryTest() throws Exception {
        //given
        MemberDTO.SignUpRequest signUpRequest = createSignUpRequest();
        authService.signUp(signUpRequest);
        Member admin = memberRepository.findByEmail(signUpRequest.getEmail()).get();
        CategoryDTO.CategorySaveRequest categoryDTO1 = createCategoryDto("도서", admin.getId(), 1L, null);
        Long cid1 = categoryService.createCategory(categoryDTO1);
        Category category1 = categoryRepository.findById(cid1).get();

        //when
        categoryService.updateCategoryName(cid1, "아동도서");

        //then
        assertThat(categoryRepository.existsByName("아동도서")).isEqualTo(true);
        assertThat(categoryRepository.existsByName("도서")).isEqualTo(false);
    }

    //--------------------------------------카테고리 삭제 태스트---------------------------------------------------------
    @Test
    @DisplayName("카테고리 삭제 테스트_아이디:자식 없음")
    public void deleteCategoryTest() throws Exception {
        //given
        MemberDTO.SignUpRequest signUpRequest = createSignUpRequest();
        authService.signUp(signUpRequest);
        Member admin = memberRepository.findByEmail(signUpRequest.getEmail()).get();
        CategoryDTO.CategorySaveRequest categoryDTO1 = createCategoryDto("도서", admin.getId(), 1L, null);
        Long cid1 = categoryService.createCategory(categoryDTO1);
        Category root = categoryRepository.findCategoryByName("root").get();
        Category category1 = categoryRepository.findById(cid1).get();
        CategoryDTO.CategorySaveRequest categoryDTO2 = createCategoryDto("주방", admin.getId(), 1L, root.getId());
        Long cid2 = categoryService.createCategory(categoryDTO2);
        Category category2 = categoryRepository.findById(cid2).get();
        CategoryDTO.CategorySaveRequest categoryDTO3 = createCategoryDto("취미", admin.getId(), 1L, root.getId());
        Long cid3 = categoryService.createCategory(categoryDTO3);
        Category category3 = categoryRepository.findById(cid3).get();
        //when
        categoryService.deleteCategoryByID(cid3);
        //then
        assertThat(categoryRepository.existsById(cid3)).isEqualTo(false);
        assertThat(categoryRepository.findAll().size()).isEqualTo(3);
    }

//    @Test
//    @DisplayName("(실패)카테고리 삭제 테스트_아이디:자식 있음")
//    public void deleteParentCategoryTest() throws Exception {
//        //given
//        MemberDTO.SignUpRequest signUpRequest = createSignUpRequest();
//        authService.signUp(signUpRequest);
//        Member admin = memberRepository.findByEmail(signUpRequest.getEmail()).get();
//        CategoryDTO.CategorySaveRequest categoryDTO1 = createCategoryDto("도서", admin.getId(), 1L, null);
//        Long cid1 = categoryService.createCategory(categoryDTO1);
//        Category root = categoryRepository.findCategoryByName("root").get();
//        Category category1 = categoryRepository.findById(cid1).get();
//        CategoryDTO.CategorySaveRequest categoryDTO2 = createCategoryDto("주방", admin.getId(), 1L, root.getId());
//        Long cid2 = categoryService.createCategory(categoryDTO2);
//        Category category2 = categoryRepository.findById(cid2).get();
//        CategoryDTO.CategorySaveRequest categoryDTO3 = createCategoryDto("취미", admin.getId(), 1L, root.getId());
//        Long cid3 = categoryService.createCategory(categoryDTO3);
//        Category category3 = categoryRepository.findById(cid3).get();
//        //when&then
//        assertThrows(CanNotDeleteCategoryException.class, ()->{
//            categoryService.deleteCategoryByID(root.getId());
//        });
//    }

    @Test
    @DisplayName("카테고리 삭제 테스트_이름")
    public void deleteCategoryByNameTest() throws Exception {
        //given
        MemberDTO.SignUpRequest signUpRequest = createSignUpRequest();
        authService.signUp(signUpRequest);
        Member admin = memberRepository.findByEmail(signUpRequest.getEmail()).get();
        CategoryDTO.CategorySaveRequest categoryDTO1 = createCategoryDto("도서", admin.getId(), 1L, null);
        Long cid1 = categoryService.createCategory(categoryDTO1);
        Category root = categoryRepository.findCategoryByName("root").get();
        Category category1 = categoryRepository.findById(cid1).get();
        CategoryDTO.CategorySaveRequest categoryDTO2 = createCategoryDto("주방", admin.getId(), 1L, root.getId());
        Long cid2 = categoryService.createCategory(categoryDTO2);
        Category category2 = categoryRepository.findById(cid2).get();
        CategoryDTO.CategorySaveRequest categoryDTO3 = createCategoryDto("취미", admin.getId(), 1L, root.getId());
        Long cid3 = categoryService.createCategory(categoryDTO3);
        Category category3 = categoryRepository.findById(cid3).get();
        //when
        categoryService.deleteCategoryByName("도서");
        //then
        assertThat(categoryRepository.existsByName("도서")).isEqualTo(false);
        assertThat(categoryRepository.findAll().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("(실패)카테고리 삭제 테스트_존재하지 않는 카테고리 이름")
    public void deleteWrongCategoryByNameTest() throws Exception {
        //given
        MemberDTO.SignUpRequest signUpRequest = createSignUpRequest();
        authService.signUp(signUpRequest);
        Member admin = memberRepository.findByEmail(signUpRequest.getEmail()).get();
        CategoryDTO.CategorySaveRequest categoryDTO1 = createCategoryDto("도서", admin.getId(), 1L, null);
        Long cid1 = categoryService.createCategory(categoryDTO1);
        Category root = categoryRepository.findCategoryByName("root").get();
        Category category1 = categoryRepository.findById(cid1).get();
        CategoryDTO.CategorySaveRequest categoryDTO2 = createCategoryDto("주방", admin.getId(), 1L, root.getId());
        Long cid2 = categoryService.createCategory(categoryDTO2);
        Category category2 = categoryRepository.findById(cid2).get();
        CategoryDTO.CategorySaveRequest categoryDTO3 = createCategoryDto("취미", admin.getId(), 1L, root.getId());
        Long cid3 = categoryService.createCategory(categoryDTO3);
        Category category3 = categoryRepository.findById(cid3).get();
        //when&then
        assertThrows(NotFoundCategoryByNameException.class, ()->{
            categoryService.deleteCategoryByName("생활");
        });
    }


//    @Test
//    @DisplayName("(삭제 불가)카테고리 삭제 테스트:자식 있음")
//    public void deleteParentCategoryTest() throws Exception {
//        //given
//        MemberDTO.SignUpRequest signUpRequest = createSignUpRequest();
//        authService.signUp(signUpRequest);
//        Member admin = memberRepository.findByEmail(signUpRequest.getEmail()).get();
//        CategoryDTO.CategorySaveRequest categoryDTO1 = createCategoryDto("물물교환", admin.getId(), 1L, null);
//        Long cid1 = categoryService.createCategory(categoryDTO1);
//        Category category1 = categoryRepository.findById(cid1).get();
//        Category root = categoryRepository.findCategoryByName("root").get();
//        CategoryDTO.CategorySaveRequest categoryDTO2 = createCategoryDto("끼리끼리", admin.getId(), 1L, categoryRepository.findCategoryByName("root").get().getId());
//        Long cid2 = categoryService.createCategory(categoryDTO2);
//        Category category2 = categoryRepository.findById(cid2).get();
//        CategoryDTO.CategorySaveRequest categoryDTO3 = createCategoryDto("도서", admin.getId(), 2L, category1.getId());
//        Long cid3 = categoryService.createCategory(categoryDTO3);
//        Category category3 = categoryRepository.findById(cid3).get();
//        CategoryDTO.CategorySaveRequest categoryDTO4 = createCategoryDto("주방", admin.getId(), 2L, category1.getId());
//        Long cid4 = categoryService.createCategory(categoryDTO4);
//        Category category4 = categoryRepository.findById(cid4).get();
//
//        //when,then
//        assertThrows(CanNotDeleteCategoryException.class, ()-> {
//            categoryService.deleteCategoryByID(cid1);
//        });
//        assertThat(categoryRepository.findAll().size()).isEqualTo(5);
//    }

}


