package f3f.dev1.trade;

import f3f.dev1.domain.member.application.AuthService;
import f3f.dev1.domain.member.exception.NotAuthorizedException;
import f3f.dev1.domain.address.model.Address;
import f3f.dev1.domain.model.TradeStatus;
import f3f.dev1.domain.post.application.PostService;
import f3f.dev1.domain.post.dto.PostDTO.PostSaveRequest;
import f3f.dev1.domain.trade.application.TradeService;
import f3f.dev1.domain.trade.dao.TradeRepository;
import f3f.dev1.domain.trade.dto.TradeDTO.CreateTradeDto;
import f3f.dev1.domain.trade.dto.TradeDTO.DeleteTradeDto;
import f3f.dev1.domain.trade.dto.TradeDTO.UpdateTradeDto;
import f3f.dev1.domain.trade.exception.DuplicateTradeException;
import f3f.dev1.domain.trade.exception.InvalidSellerIdException;
import f3f.dev1.domain.trade.model.Trade;
import f3f.dev1.domain.member.application.MemberService;
import f3f.dev1.domain.member.dao.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static f3f.dev1.domain.member.dto.MemberDTO.*;

@Transactional
@SpringBootTest
public class TradeServiceTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TradeRepository tradeRepository;

    @Autowired
    MemberService memberService;

    @Autowired
    AuthService authService;

    @Autowired
    PostService postService;

    @Autowired
    TradeService tradeService;

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
    public SignUpRequest createSignUpRequest(String email, String phoneNumber) {
        return SignUpRequest.builder()
                .userName("username")
                .nickname("nickname")
                .phoneNumber(phoneNumber)
                .email(email)
                .password("password")
                .build();
    }

    // 로그인 DTO 생성 메소드
    public LoginRequest createLoginRequest(String email) {
        return LoginRequest.builder()
                .email(email)
                .password("password").build();
    }

    // 게시글 생성 DTO 메소드
    public PostSaveRequest createPostSaveRequest(Long authorId) {

        return PostSaveRequest.builder()
                .productCategory(null)
                .wishCategory(null)
                .tradeEachOther(false)
                .title("title")
                .content("content")
                .authorId(authorId).build();


    }

    // 트레이드 생성 DTO 생성 메소드
    public CreateTradeDto createTradeDto(Long sellerId, Long postId) {
        return CreateTradeDto.builder()
                .sellerId(sellerId)
                .postId(postId).build();
    }
    @Test
    @DisplayName("트레이드 생성 성공 테스트")
    public void createTradeTestSuccess() throws Exception {
        //given
        SignUpRequest signUpRequest1 = createSignUpRequest("testuser1@email.com", "01012345678");
        SignUpRequest signUpRequest2 = createSignUpRequest("testuser2@email.com", "01056781234");
        authService.signUp(signUpRequest1);
        authService.signUp(signUpRequest2);
        Long userId1 = memberRepository.findByEmail("testuser1@email.com").get().getId();
        Long userId2 = memberRepository.findByEmail("testuser2@email.com").get().getId();

        PostSaveRequest postSaveRequest = createPostSaveRequest(userId1);
        Long postId = postService.savePost(postSaveRequest, userId1);


        // when
        CreateTradeDto tradeDto = CreateTradeDto.builder().sellerId(userId1).postId(postId).build();
        Long tradeId = tradeService.createTrade(tradeDto, userId1);
        Optional<Trade> byId = tradeRepository.findById(tradeId);

        // then
        assertArrayEquals(new Long[]{userId1, userId2, postId}, new Long[]{byId.get().getSeller().getId(), byId.get().getPost().getId()});

    }

    @Test
    @DisplayName("로그인 되지 않은 사용자에 해당하는 트레이드 생성 요청 실패 테스트")
    public void createTradeTestFailByWrongUser() throws Exception{
        //given
        SignUpRequest signUpRequest1 = createSignUpRequest("testuser1@email.com", "01012345678");
        SignUpRequest signUpRequest2 = createSignUpRequest("testuser2@email.com", "01056781234");
        authService.signUp(signUpRequest1);
        authService.signUp(signUpRequest2);
        Long userId1 = memberRepository.findByEmail("testuser1@email.com").get().getId();
        Long userId2 = memberRepository.findByEmail("testuser2@email.com").get().getId();
        PostSaveRequest postSaveRequest = createPostSaveRequest(userId1);
        Long postId = postService.savePost(postSaveRequest, userId1);


        // when
        CreateTradeDto tradeDto = CreateTradeDto.builder().sellerId(userId1).postId(postId).build();

        // then
        assertThrows(NotAuthorizedException.class, () -> tradeService.createTrade(tradeDto, userId1 + 2));
    }

    @Test
    @DisplayName("글 게시자가 아닌 사용자의 거래 생성 실패 테스트")
    public void createTradeTestFailByWrongSeller() throws Exception{
        //given
        SignUpRequest signUpRequest1 = createSignUpRequest("testuser1@email.com", "01012345678");
        SignUpRequest signUpRequest2 = createSignUpRequest("testuser2@email.com", "01056781234");
        authService.signUp(signUpRequest1);
        authService.signUp(signUpRequest2);
        Long userId1 = memberRepository.findByEmail("testuser1@email.com").get().getId();
        Long userId2 = memberRepository.findByEmail("testuser2@email.com").get().getId();
        PostSaveRequest postSaveRequest = createPostSaveRequest(userId1);
        Long postId = postService.savePost(postSaveRequest, userId1);


        // when
        CreateTradeDto tradeDto = CreateTradeDto.builder().sellerId(userId2).postId(postId).build();
        // then
        assertThrows(InvalidSellerIdException.class, () -> tradeService.createTrade(tradeDto, userId2));
    }

    @Test
    @DisplayName("이미 생성된 거래로 인한 거래 생성 실패 테스트")
    public void createTradeTestFailByDuplicateTrade() throws Exception{
        //given
        SignUpRequest signUpRequest1 = createSignUpRequest("testuser1@email.com", "01012345678");
        SignUpRequest signUpRequest2 = createSignUpRequest("testuser2@email.com", "01056781234");
        authService.signUp(signUpRequest1);
        authService.signUp(signUpRequest2);
        Long userId1 = memberRepository.findByEmail("testuser1@email.com").get().getId();
        Long userId2 = memberRepository.findByEmail("testuser2@email.com").get().getId();
        PostSaveRequest postSaveRequest = createPostSaveRequest(userId1);
        Long postId = postService.savePost(postSaveRequest, userId1);


        // when
        CreateTradeDto tradeDto = CreateTradeDto.builder().sellerId(userId1).postId(postId).build();
        Long tradeId = tradeService.createTrade(tradeDto, userId1);


        // then
        assertThrows(DuplicateTradeException.class, () -> tradeService.createTrade(tradeDto, userId1));
    }

    @Test
    @DisplayName("거래 상태 변경 성공 테스트")
    public void updateTradeStatusTestSuccess() throws Exception{
        //given
        SignUpRequest signUpRequest1 = createSignUpRequest("testuser1@email.com", "01012345678");
        SignUpRequest signUpRequest2 = createSignUpRequest("testuser2@email.com", "01056781234");
        authService.signUp(signUpRequest1);
        authService.signUp(signUpRequest2);
        Long userId1 = memberRepository.findByEmail("testuser1@email.com").get().getId();
        Long userId2 = memberRepository.findByEmail("testuser2@email.com").get().getId();
        PostSaveRequest postSaveRequest = createPostSaveRequest(userId1);
        Long postId = postService.savePost(postSaveRequest, userId1);

        CreateTradeDto tradeDto = CreateTradeDto.builder().sellerId(userId1).postId(postId).build();
        // when
        Long tradeId = tradeService.createTrade(tradeDto, userId1);
        UpdateTradeDto updateTradeDto = UpdateTradeDto.builder().postId(postId).userId(userId1).tradeStatus(TradeStatus.TRADING).build();
        tradeService.updateTradeStatus(updateTradeDto, userId1);

        // then
        Trade trade = tradeRepository.findById(tradeId).get();
        assertThat(trade.getTradeStatus()).isEqualTo(TradeStatus.TRADING);
    }

    @Test
    @DisplayName("판매자가 아닌 유저의 업데이트 요청 실패 테스트")
    public void updateTradeStatusTestFailByWrongUser() throws Exception{
        //given
        SignUpRequest signUpRequest1 = createSignUpRequest("testuser1@email.com", "01012345678");
        SignUpRequest signUpRequest2 = createSignUpRequest("testuser2@email.com", "01056781234");
        authService.signUp(signUpRequest1);
        authService.signUp(signUpRequest2);
        Long userId1 = memberRepository.findByEmail("testuser1@email.com").get().getId();
        Long userId2 = memberRepository.findByEmail("testuser2@email.com").get().getId();
        PostSaveRequest postSaveRequest = createPostSaveRequest(userId1);
        Long postId = postService.savePost(postSaveRequest, userId1);

        CreateTradeDto tradeDto = CreateTradeDto.builder().sellerId(userId1).postId(postId).build();
        // when
        Long tradeId = tradeService.createTrade(tradeDto, userId1);
        UpdateTradeDto updateTradeDto = UpdateTradeDto.builder().postId(postId).userId(userId2).tradeStatus(TradeStatus.TRADING).build();
        // then
        assertThrows(InvalidSellerIdException.class, ()->tradeService.updateTradeStatus(updateTradeDto, userId2));
    }

    @Test
    @DisplayName("로그인 하지 않은 유저의 업데이트 요청 실패 테스트")
    public void updateTradeStatusTestFailByNonLoginUser() throws Exception{
        //given
        SignUpRequest signUpRequest1 = createSignUpRequest("testuser1@email.com", "01012345678");
        SignUpRequest signUpRequest2 = createSignUpRequest("testuser2@email.com", "01056781234");
        authService.signUp(signUpRequest1);
        authService.signUp(signUpRequest2);
        Long userId1 = memberRepository.findByEmail("testuser1@email.com").get().getId();
        Long userId2 = memberRepository.findByEmail("testuser2@email.com").get().getId();
        PostSaveRequest postSaveRequest = createPostSaveRequest(userId1);
        Long postId = postService.savePost(postSaveRequest, userId1);

        CreateTradeDto tradeDto = CreateTradeDto.builder().sellerId(userId1).postId(postId).build();
        // when
        Long tradeId = tradeService.createTrade(tradeDto, userId1);
        UpdateTradeDto updateTradeDto = UpdateTradeDto.builder().postId(postId).userId(userId1).tradeStatus(TradeStatus.TRADING).build();

        // then
        assertThrows(NotAuthorizedException.class, () -> tradeService.updateTradeStatus(updateTradeDto, userId2));
    }

    @Test
    @DisplayName("거래 삭제 성공 테스트")
    public void deleteTradeTestSuccess() throws Exception{
        //given
        SignUpRequest signUpRequest1 = createSignUpRequest("testuser1@email.com", "01012345678");
        SignUpRequest signUpRequest2 = createSignUpRequest("testuser2@email.com", "01056781234");
        authService.signUp(signUpRequest1);
        authService.signUp(signUpRequest2);
        Long userId1 = memberRepository.findByEmail("testuser1@email.com").get().getId();
        Long userId2 = memberRepository.findByEmail("testuser2@email.com").get().getId();
        PostSaveRequest postSaveRequest = createPostSaveRequest(userId1);
        Long postId = postService.savePost(postSaveRequest, userId1);

        CreateTradeDto tradeDto = CreateTradeDto.builder().sellerId(userId1).postId(postId).build();
        // when
        Long tradeId = tradeService.createTrade(tradeDto, userId1);
        DeleteTradeDto deleteTradeDto = DeleteTradeDto.builder().postId(postId).userId(userId1).build();
        tradeService.deleteTrade(deleteTradeDto, userId1);
        // then
        assertThat(false).isEqualTo(tradeRepository.existsById(tradeId));

    }

    @Test
    @DisplayName("로그인된 유저가 아닌요청으로 인한 거래 삭제 실패 테스트")
    public void deleteTradeTestFailByNonLogin() throws Exception{
        //given
        SignUpRequest signUpRequest1 = createSignUpRequest("testuser1@email.com", "01012345678");
        SignUpRequest signUpRequest2 = createSignUpRequest("testuser2@email.com", "01056781234");
        authService.signUp(signUpRequest1);
        authService.signUp(signUpRequest2);
        Long userId1 = memberRepository.findByEmail("testuser1@email.com").get().getId();
        Long userId2 = memberRepository.findByEmail("testuser2@email.com").get().getId();
        PostSaveRequest postSaveRequest = createPostSaveRequest(userId1);
        Long postId = postService.savePost(postSaveRequest, userId1);

        CreateTradeDto tradeDto = CreateTradeDto.builder().sellerId(userId1).postId(postId).build();
        // when
        Long tradeId = tradeService.createTrade(tradeDto, userId1);
        DeleteTradeDto deleteTradeDto = DeleteTradeDto.builder().postId(postId).userId(userId1).build();
        // then
        assertThrows(NotAuthorizedException.class, () -> tradeService.deleteTrade(deleteTradeDto, userId2));
    }

    @Test
    @DisplayName("잘못된 판매자 요청으로 인한 거래 삭제 테스트")
    public void deleteTradeTestFailByInvalidSeller() throws Exception{
        //given
        SignUpRequest signUpRequest1 = createSignUpRequest("testuser1@email.com", "01012345678");
        SignUpRequest signUpRequest2 = createSignUpRequest("testuser2@email.com", "01056781234");
        authService.signUp(signUpRequest1);
        authService.signUp(signUpRequest2);
        Long userId1 = memberRepository.findByEmail("testuser1@email.com").get().getId();
        Long userId2 = memberRepository.findByEmail("testuser2@email.com").get().getId();
        PostSaveRequest postSaveRequest = createPostSaveRequest(userId1);
        Long postId = postService.savePost(postSaveRequest, userId1);

        CreateTradeDto tradeDto = CreateTradeDto.builder().sellerId(userId1).postId(postId).build();
        // when
        Long tradeId = tradeService.createTrade(tradeDto, userId1);
        DeleteTradeDto deleteTradeDto = DeleteTradeDto.builder().postId(postId).userId(userId2).build();

        // then
        assertThrows(InvalidSellerIdException.class, () -> tradeService.deleteTrade(deleteTradeDto, userId2));
    }
}
