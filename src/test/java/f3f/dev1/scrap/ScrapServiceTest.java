package f3f.dev1.scrap;

import f3f.dev1.domain.category.model.Category;
import f3f.dev1.domain.member.application.AuthService;
import f3f.dev1.domain.member.application.MemberService;
import f3f.dev1.domain.member.dao.MemberRepository;
import f3f.dev1.domain.member.dto.MemberDTO;
import f3f.dev1.domain.member.model.Member;
import f3f.dev1.domain.model.Address;
import f3f.dev1.domain.post.application.PostService;
import f3f.dev1.domain.post.dao.ScrapPostRepository;
import f3f.dev1.domain.post.dto.PostDTO;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.scrap.application.ScrapService;
import f3f.dev1.domain.scrap.dao.ScrapRepository;
import f3f.dev1.domain.scrap.dto.ScrapDTO;
import f3f.dev1.domain.scrap.model.Scrap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static f3f.dev1.domain.member.dto.MemberDTO.*;
import static f3f.dev1.domain.post.dto.PostDTO.*;
import static f3f.dev1.domain.scrap.dto.ScrapDTO.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
public class ScrapServiceTest {
    @Autowired
    ScrapRepository scrapRepository;

    @Autowired
    ScrapPostRepository scrapPostRepository;

    @Autowired
    ScrapService scrapService;

    @Autowired
    AuthService authService;

    @Autowired
    PostService postService;

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;


    public Address createAddress() {
        return Address.builder()
                .addressName("address")
                .postalAddress("13556")
                .latitude("37.49455")
                .longitude("127.12170")
                .build();
    }

    // 회원가입 DTO 생성 메소드
    public SignUpRequest createSignUpRequest() {
        return SignUpRequest.builder()
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
    public LoginRequest createLoginRequest() {
        return LoginRequest.builder()
                .email("userEmail@email.com")
                .password("password").build();
    }

    // 포스트 생성 DTO 메소드
    public PostSaveRequest createPostSaveRequest(Member author) {
        return PostSaveRequest.builder()
                .author(author)
                .title("이건 테스트 게시글 제목이야")
                .content("이건 테스트 게시글 내용이지 하하")
                .tradeEachOther(false)
                .wishCategory(new Category())
                .productCategory(new Category())
                .build();
    }


    @Test
    @DisplayName("유저 생성시 스크랩 생성 확인 테스트")
    public void createScrapTest() throws Exception{
        //given
        SignUpRequest signUpRequest = createSignUpRequest();
        authService.signUp(signUpRequest);
        Member member = memberRepository.findByEmail(signUpRequest.getEmail()).get();

        // when
        Optional<Scrap> scrapByUserId = scrapRepository.findScrapByUserId(member.getId());

        // then
        assertThat(member.getId()).isEqualTo(scrapByUserId.get().getUser().getId());
    }

    @Test
    @DisplayName("스크랩에 포스트 추가 성공 테스트")
    public void addPostToScrapTestSuccess() throws Exception{
        //given
        SignUpRequest signUpRequest = createSignUpRequest();
        authService.signUp(signUpRequest);
        Member member = memberRepository.findByEmail(signUpRequest.getEmail()).get();
        PostSaveRequest postSaveRequest = createPostSaveRequest(member);
        Long postId = postService.savePost(postSaveRequest);


        // when
        AddScrapPostDTO addScrapPostDTO = AddScrapPostDTO.builder().userId(member.getId()).postId(postId).build();
        scrapService.addScrapPost(addScrapPostDTO);
        // then
        assertThat(true).isEqualTo(scrapPostRepository.existsByScrapIdAndPostId(member.getScrap().getId(), postId));

    }
}
