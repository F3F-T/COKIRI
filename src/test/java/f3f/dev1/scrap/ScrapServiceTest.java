package f3f.dev1.scrap;

import f3f.dev1.domain.member.application.AuthService;
import f3f.dev1.domain.member.application.MemberService;
import f3f.dev1.domain.member.dao.MemberRepository;
import f3f.dev1.domain.member.exception.NotAuthorizedException;
import f3f.dev1.domain.member.model.Member;
import f3f.dev1.domain.member.model.UserLoginType;
import f3f.dev1.domain.model.Address;
import f3f.dev1.domain.post.application.PostService;
import f3f.dev1.domain.post.dao.PostRepository;
import f3f.dev1.domain.post.dao.ScrapPostRepository;
import f3f.dev1.domain.scrap.application.ScrapService;
import f3f.dev1.domain.scrap.dao.ScrapRepository;
import f3f.dev1.domain.scrap.dto.ScrapDTO.DeleteScrapPostDTO;
import f3f.dev1.domain.scrap.model.Scrap;
import f3f.dev1.global.error.exception.NotFoundByIdException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static f3f.dev1.domain.member.dto.MemberDTO.LoginRequest;
import static f3f.dev1.domain.member.dto.MemberDTO.SignUpRequest;
import static f3f.dev1.domain.post.dto.PostDTO.PostSaveRequest;
import static f3f.dev1.domain.scrap.dto.ScrapDTO.AddScrapPostDTO;
import static f3f.dev1.domain.scrap.dto.ScrapDTO.CreateScrapPostDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
public class ScrapServiceTest {
    @Autowired
    ScrapRepository scrapRepository;

    @Autowired
    ScrapPostRepository scrapPostRepository;

    @Autowired
    PostRepository postRepository;

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
                .password("password")
                .userLoginType(UserLoginType.EMAIL)
                .build();
    }

    // 로그인 DTO 생성 메소드
    public LoginRequest createLoginRequest() {
        return LoginRequest.builder()
                .email("userEmail@email.com")
                .password("password").build();
    }

    // 포스트 생성 DTO 메소드
    public PostSaveRequest createPostSaveRequest(Long authorId) {
        return PostSaveRequest.builder()
                .authorId(authorId)
                .title("이건 테스트 게시글 제목이야")
                .content("이건 테스트 게시글 내용이지 하하")
                .tradeEachOther(false)
                .wishCategory(null)
                .productCategory(null)
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
        Optional<Scrap> scrapByUserId = scrapRepository.findScrapByMemberId(member.getId());

        // then
        assertThat(member.getId()).isEqualTo(scrapByUserId.get().getMember().getId());
    }

    @Test
    @DisplayName("스크랩에 포스트 추가 성공 테스트")
    public void addPostToScrapTestSuccess() throws Exception{
        //given
        SignUpRequest signUpRequest = createSignUpRequest();
        authService.signUp(signUpRequest);
        Member member = memberRepository.findByEmail(signUpRequest.getEmail()).get();
        Scrap scrap = scrapRepository.findScrapByMemberId(member.getId()).get();
        PostSaveRequest postSaveRequest = createPostSaveRequest(member.getId());
        Long postId = postService.savePost(postSaveRequest, member.getId());


        // when
        AddScrapPostDTO addScrapPostDTO = AddScrapPostDTO.builder().userId(member.getId()).postId(postId).build();
        CreateScrapPostDTO createScrapPostDTO = scrapService.addScrapPost(addScrapPostDTO, member.getId());
        // then
        assertThat(true).isEqualTo(scrapPostRepository.existsByScrapIdAndPostId(scrap.getId(), postId));


    }
    
    @Test
    @DisplayName("로그인된 유저가 아닌 다른 유저의 스크랩 포스트 거절 테스트")
    public void addPostToScrapTestFailByWrongUser() throws Exception{
        //given
        SignUpRequest signUpRequest = createSignUpRequest();
        authService.signUp(signUpRequest);
        Member member = memberRepository.findByEmail(signUpRequest.getEmail()).get();
        Scrap scrap = scrapRepository.findScrapByMemberId(member.getId()).get();
        PostSaveRequest postSaveRequest = createPostSaveRequest(member.getId());
        Long postId = postService.savePost(postSaveRequest, member.getId());


        // when
        AddScrapPostDTO addScrapPostDTO = AddScrapPostDTO.builder().userId(member.getId()+1).postId(postId).build();
        
        // then
        assertThrows(NotAuthorizedException.class, () -> scrapService.addScrapPost(addScrapPostDTO, member.getId()));
    }
    @Test
    @DisplayName("존재하지 않은 포스트로 인한 스크랩 실패 테스트")
    public void addPostToScrapTestFailByWrongPost() throws Exception{
        //given
        SignUpRequest signUpRequest = createSignUpRequest();
        authService.signUp(signUpRequest);
        Member member = memberRepository.findByEmail(signUpRequest.getEmail()).get();
        Scrap scrap = scrapRepository.findScrapByMemberId(member.getId()).get();
        PostSaveRequest postSaveRequest = createPostSaveRequest(member.getId());
        Long postId = postService.savePost(postSaveRequest, member.getId());


        // when
        AddScrapPostDTO addScrapPostDTO = AddScrapPostDTO.builder().userId(member.getId()).postId(postId+1).build();
        // then
        assertThrows(NotFoundByIdException.class, () -> scrapService.addScrapPost(addScrapPostDTO, member.getId()));
    }

    @Test
    @DisplayName("스크랩한 포스트 삭제 성공 테스트")
    public void deletePostScrapTestSuccess() throws Exception{
        //given
        SignUpRequest signUpRequest = createSignUpRequest();
        authService.signUp(signUpRequest);
        Member member = memberRepository.findByEmail(signUpRequest.getEmail()).get();
        Scrap scrap = scrapRepository.findScrapByMemberId(member.getId()).get();
        PostSaveRequest postSaveRequest = createPostSaveRequest(member.getId());
        Long postId = postService.savePost(postSaveRequest, member.getId());

        AddScrapPostDTO addScrapPostDTO = AddScrapPostDTO.builder().userId(member.getId()).postId(postId).build();

        CreateScrapPostDTO createScrapPostDTO = scrapService.addScrapPost(addScrapPostDTO, member.getId());
        // when
        DeleteScrapPostDTO deleteScrapPostDTO = DeleteScrapPostDTO.builder().postId(postId).userId(member.getId()).build();
        scrapService.deleteScrapPost(deleteScrapPostDTO, member.getId());
        // then
        assertThat(false).isEqualTo(scrapPostRepository.existsByScrapIdAndPostId(scrap.getId(), postId));
    }
    
    @Test
    @DisplayName("로그인된 유저가 아닌 다른 유저의 포스트 삭제 요청 ")
    public void deletePostScrapTestFailByWrongUser() throws Exception{
        SignUpRequest signUpRequest = createSignUpRequest();
        authService.signUp(signUpRequest);
        Member member = memberRepository.findByEmail(signUpRequest.getEmail()).get();
        Scrap scrap = scrapRepository.findScrapByMemberId(member.getId()).get();
        PostSaveRequest postSaveRequest = createPostSaveRequest(member.getId());
        Long postId = postService.savePost(postSaveRequest, member.getId());

        AddScrapPostDTO addScrapPostDTO = AddScrapPostDTO.builder().userId(member.getId()).postId(postId).build();

        CreateScrapPostDTO createScrapPostDTO = scrapService.addScrapPost(addScrapPostDTO, member.getId());
        // when
        DeleteScrapPostDTO deleteScrapPostDTO = DeleteScrapPostDTO.builder().postId(postId).userId(member.getId()+1).build();
        
        // then
        assertThrows(NotAuthorizedException.class, () -> scrapService.deleteScrapPost(deleteScrapPostDTO, member.getId()));
    }

    @Test
    @DisplayName("스크랩에 존재하지 않은 포스트 삭제 요청 실패 테스트")
    public void deletePostScrapTestFailByWrongPost() throws Exception{
        SignUpRequest signUpRequest = createSignUpRequest();
        authService.signUp(signUpRequest);
        Member member = memberRepository.findByEmail(signUpRequest.getEmail()).get();
        Scrap scrap = scrapRepository.findScrapByMemberId(member.getId()).get();
        PostSaveRequest postSaveRequest = createPostSaveRequest(member.getId());
        Long postId = postService.savePost(postSaveRequest, member.getId());

        AddScrapPostDTO addScrapPostDTO = AddScrapPostDTO.builder().userId(member.getId()).postId(postId).build();

        CreateScrapPostDTO createScrapPostDTO = scrapService.addScrapPost(addScrapPostDTO, member.getId());
        // when
        DeleteScrapPostDTO deleteScrapPostDTO = DeleteScrapPostDTO.builder().postId(postId+1).userId(member.getId()).build();

        // then
        assertThrows(NotFoundByIdException.class, () -> scrapService.deleteScrapPost(deleteScrapPostDTO, member.getId()));
    }
}
