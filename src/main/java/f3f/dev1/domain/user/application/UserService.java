package f3f.dev1.domain.user.application;

import f3f.dev1.domain.comment.model.Comment;
import f3f.dev1.domain.message.model.Message;
import f3f.dev1.domain.message.model.MessageRoom;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.scrap.application.ScrapService;
import f3f.dev1.domain.scrap.model.Scrap;
import f3f.dev1.domain.trade.model.Trade;
import f3f.dev1.domain.user.dao.UserRepository;
import f3f.dev1.domain.user.dto.UserDTO;
import f3f.dev1.domain.user.dto.UserDTO.SignUpRequest;
import f3f.dev1.domain.user.dto.UserDTO.UpdateUserInfo;
import f3f.dev1.domain.user.dto.UserDTO.UserInfo;
import f3f.dev1.domain.user.exception.*;
import f3f.dev1.domain.user.model.User;
import f3f.dev1.global.error.exception.NotFoundByIdException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static f3f.dev1.domain.scrap.dto.ScrapDTO.CreateScrapDTO;
import static f3f.dev1.domain.user.dto.UserDTO.*;
import static f3f.dev1.domain.user.dto.UserDTO.UpdateUserPassword;
import static f3f.dev1.global.common.constants.ResponseConstants.DELETE;
import static f3f.dev1.global.common.constants.ResponseConstants.UPDATE;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final ScrapService scrapService;

    private final SessionLoginService sessionLoginService;

    // authentication에 쓰이는 메소드, 이메일로 유저객체 리턴
    @Transactional(readOnly = true)
    public User findUserInfoByEmail(String email) {

        return userRepository.findByEmail(email).orElseThrow(UserNotFoundByEmailException::new);

    }


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
        // TODO: 닉네임 중복 검사 추가
        if (userRepository.existsByNickname(signUpRequest.getNickname())) {
            throw new DuplicateNicknameException();
        }


        signUpRequest.encrypt();

        User user = signUpRequest.toEntity();

        userRepository.save(user);
        CreateScrapDTO userScrap = CreateScrapDTO.builder().user(user).build();
        scrapService.createScrap(userScrap);
        return user.getId();
    }


    // 조회 메소드
    // 아이디로 유저 정보 조회
    // TODO: 리팩터링하면서 사용하지 않게됨, 제거 예정
    @Transactional(readOnly = true)
    public UserInfo getUserInfo(Long userId) {
        User byId = userRepository.findById(userId).orElseThrow(NotFoundByIdException::new);

        return byId.toUserInfo();

    }

    // TODO: 수요일 이후에 구현 예정
    // 아이디로 유저가 쓴 게시글 리스트 조회
    @Transactional(readOnly = true)
    public List<Post> getUserWrittenPosts(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundByIdException::new);
        return user.getPosts();
    }
    // 아이디로 유저가 쓴 댓글 리스트 조회
    @Transactional(readOnly = true)
    public List<Comment> getUserWrittenComments(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundByIdException::new);
        return user.getComments();
    }

    // 아이디로 유저가 스크랩한 게시글 리스트 조회

    @Transactional(readOnly = true)
    public List<Post> getUserScrapPosts(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundByIdException::new);
        Scrap userScrap = user.getScrap();
        return new ArrayList<>();
    }
    // 아이디로 유저가 판매자인 거래 리스트 조회
    @Transactional(readOnly = true)
    public List<Trade> getUserSellingTrades(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundByIdException::new);
        return user.getSellingTrades();
    }

    // 아이디로 유저가 구매자인 거래 조회
    @Transactional(readOnly = true)
    public List<Trade> getUserBuyingTrades(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundByIdException::new);
        return user.getBuyingTrades();
    }

    // 아이디로 유저가 판매자인 채팅방 리스트 조회
    @Transactional(readOnly = true)
    public List<MessageRoom> getUserSellingMessageRooms(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundByIdException::new);
        return user.getSellingRooms();
    }
    // 아이디로 유저가 구매자인 채팅방 리스트 조회
    @Transactional(readOnly = true)
    public List<MessageRoom> getUserBuyingMessageRooms(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundByIdException::new);
        return user.getBuyingRooms();
    }

    // 아이디로 유저가 보낸 메시지 리스트 조회
    @Transactional(readOnly = true)
    public List<Message> getUserSendMessages(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundByIdException::new);
        return user.getSendMessages();
    }
    // 아이디로 유저가 받은 메시지 리스트 조회
    @Transactional(readOnly = true)
    public List<Message> getUserReceivedMessages(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundByIdException::new);
        return user.getReceivedMessages();
    }


    // 업데이트 메소드
    // 유저 정보 업데이트 처리 메소드
    @Transactional
    public ResponseEntity<String> updateUserInfo(UpdateUserInfo updateUserInfo) {
        User user = userRepository.findByEmail(sessionLoginService.getLoginUser()).orElseThrow(UserNotFoundByEmailException::new);
        user.updateUserInfo(updateUserInfo);
        return UPDATE;
    }

    // 유저 비밀번호 업데이트 처리 메소드
    // TODO: 트레이드와 마찬가지로, 세션에서 가져온 유저 값을 항상 신뢰할 수 있을까 고민
    // TODO: 프론트에서 따로 유저를 구분짓는 아이디나 이메일을 넘겨받아야될 것 같은데, 현재는 세션에서 가져온 값을 항상 신뢰할 수 있다고 판단해서 로그인시에도 프론트로 따로 유저 아이디나 이메일 값을 안넘긴다
    // TODO: 코드리뷰후 수정 예정
    @Transactional
    public ResponseEntity<String> updateUserPassword(UpdateUserPassword updateUserPassword) {

        User user = userRepository.findByEmail(sessionLoginService.getLoginUser()).orElseThrow(UserNotFoundByEmailException::new);
        updateUserPassword.encrypt();
        System.out.println("user.getPassword() = " + user.getPassword());
        System.out.println("updateUserPassword = " + updateUserPassword.getOldPassword());
        if (!Objects.equals(user.getPassword(), updateUserPassword.getOldPassword())) {
            throw new InvalidPasswordException();
        }
        user.updateUserPassword(updateUserPassword);
        return UPDATE;
    }

    // 유저 삭제 메소드
    @Transactional
    public ResponseEntity<String> deleteUser() {
        User user = userRepository.findByEmail(sessionLoginService.getLoginUser()).orElseThrow(UserNotFoundByEmailException::new);
        userRepository.delete(user);
        sessionLoginService.logout();
        return DELETE;
    }

    @Transactional
    public EncryptEmailDto findUserEmail(FindEmailDto findEmailDto) {
        String userName = findEmailDto.getUserName();
        String phoneNumber = findEmailDto.getPhoneNumber();
        User user = userRepository.findByUserNameAndPhoneNumber(userName, phoneNumber).orElseThrow(UserNotFoundByUsernameAndPhoneException::new);
        return user.encryptEmail();



    }


    // TODO: 마이페이지 조회 메소드 필요할 것 같음 추가예정

}
