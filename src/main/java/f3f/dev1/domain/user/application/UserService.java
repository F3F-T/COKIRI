package f3f.dev1.domain.user.application;

import f3f.dev1.domain.comment.model.Comment;
import f3f.dev1.domain.message.model.Message;
import f3f.dev1.domain.message.model.MessageRoom;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.scrap.application.ScrapService;
import f3f.dev1.domain.scrap.dao.ScrapRepository;
import f3f.dev1.domain.scrap.model.Scrap;
import f3f.dev1.domain.trade.model.Trade;
import f3f.dev1.domain.user.dao.UserRepository;
import f3f.dev1.domain.user.dto.UserDTO.LoginRequest;
import f3f.dev1.domain.user.dto.UserDTO.SignUpRequest;
import f3f.dev1.domain.user.dto.UserDTO.UpdateUserInfo;
import f3f.dev1.domain.user.dto.UserDTO.UserInfo;
import f3f.dev1.domain.user.exception.DuplicateEmailException;
import f3f.dev1.domain.user.exception.DuplicatePhoneNumberExepction;
import f3f.dev1.domain.user.exception.InvalidPasswordException;
import f3f.dev1.domain.user.exception.NotFoundByEmailException;
import f3f.dev1.domain.user.model.User;
import f3f.dev1.global.error.exception.NotFoundByIdException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static f3f.dev1.domain.scrap.dto.ScrapDTO.CreateScrapDTO;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ScrapRepository scrapRepository;

    private final ScrapService scrapService;

    // 회원가입 요청 처리 메소드, 유저 생성
    // signUpRequest로 넘어오는 값 검증은 컨트롤러에서 진행하게 구현 예정
    @Transactional
    public Long signUp(SignUpRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new DuplicateEmailException();
        }
        if (userRepository.existsByPhoneNumber(signUpRequest.getPhoneNumber())) {
            throw new DuplicatePhoneNumberExepction();
        }


        User user = signUpRequest.toEntity();
        userRepository.save(user);
        CreateScrapDTO userScrap = CreateScrapDTO.builder().user(user).build();
        scrapService.createScrap(userScrap);
        return user.getId();
    }
    // 로그인 요청 처리 메소드, 이메일 & 비밀번호로 로그인 가능 여부 확인
    @Transactional
    public Long login(LoginRequest loginRequest) {
        User byEmail = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(NotFoundByEmailException::new);

        if (!byEmail.getPassword().equals(loginRequest.getPassword())) {
            throw new InvalidPasswordException();
        }

        return byEmail.getId();

    }


    // 조회 메소드
    // 아이디로 유저 정보 조회
    @Transactional
    public UserInfo getUserInfo(Long userId) {
        User byId = userRepository.findById(userId).orElseThrow(NotFoundByIdException::new);

        return byId.toUserInfo();

    }
    // 아이디로 유저가 쓴 게시글 리스트 조회
    @Transactional
    public List<Post> getUserWrittenPosts(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundByIdException::new);
        return user.getPosts();
    }
    // 아이디로 유저가 쓴 댓글 리스트 조회
    @Transactional
    public List<Comment> getUserWrittenComments(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundByIdException::new);
        return user.getComments();
    }

    // 아이디로 유저가 스크랩한 게시글 리스트 조회
    // TODO: scrapPost 구현되면 유저가 스크랩한 포스트 리스트 리턴하게 구현해야함
    @Transactional
    public List<Post> getUserScrapPosts(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundByIdException::new);
        Scrap userScrap = user.getScrap();
        return new ArrayList<>();
    }
    // 아이디로 유저가 판매자인 거래 리스트 조회
    @Transactional
    public List<Trade> getUserSellingTrades(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundByIdException::new);
        return user.getSellingTrades();
    }

    // 아이디로 유저가 구매자인 거래 조회
    @Transactional
    public List<Trade> getUserBuyingTrades(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundByIdException::new);
        return user.getBuyingTrades();
    }

    // 아이디로 유저가 판매자인 채팅방 리스트 조회
    @Transactional
    public List<MessageRoom> getUserSellingMessageRooms(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundByIdException::new);
        return user.getSellingRooms();
    }
    // 아이디로 유저가 구매자인 채팅방 리스트 조회
    @Transactional
    public List<MessageRoom> getUserBuyingMessageRooms(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundByIdException::new);
        return user.getBuyingRooms();
    }

    // 아이디로 유저가 보낸 메시지 리스트 조회
    @Transactional
    public List<Message> getUserSendMessages(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundByIdException::new);
        return user.getSendMessages();
    }
    // 아이디로 유저가 받은 메시지 리스트 조회
    @Transactional
    public List<Message> getUserReceivedMessages(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundByIdException::new);
        return user.getReceivedMessages();
    }


    // 업데이트 메소드
    // 유저 정보 업데이트 처리 메소드
    @Transactional
    public UpdateUserInfo updateUserInfo(UpdateUserInfo updateUserInfo) {
        User user = userRepository.findById(updateUserInfo.getId()).orElseThrow(NotFoundByIdException::new);
        return user.updateUserInfo(updateUserInfo);
    }

    // 유저 삭제 메소드
    // 아이디로 회원 삭제 메소드
    @Transactional
    public Long deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundByIdException::new);
        userRepository.delete(user);
        return userId;
    }


}
