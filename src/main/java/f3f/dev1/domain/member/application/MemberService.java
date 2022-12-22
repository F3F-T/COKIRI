package f3f.dev1.domain.member.application;

import f3f.dev1.domain.member.dao.MemberRepository;
import f3f.dev1.domain.member.exception.*;
import f3f.dev1.domain.member.model.Member;
import f3f.dev1.domain.message.dao.MessageRoomRepository;
import f3f.dev1.domain.model.Address;
import f3f.dev1.domain.post.dao.PostRepository;
import f3f.dev1.domain.post.dto.PostDTO;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.scrap.application.ScrapService;
import f3f.dev1.domain.scrap.dao.ScrapRepository;
import f3f.dev1.domain.scrap.exception.UserScrapNotFoundException;
import f3f.dev1.domain.scrap.model.Scrap;
import f3f.dev1.global.error.exception.NotFoundByIdException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static f3f.dev1.domain.member.dto.MemberDTO.*;
import static f3f.dev1.domain.post.dto.PostDTO.*;
import static f3f.dev1.global.common.constants.RandomCharacter.RandomCharacters;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final ScrapService scrapService;

    private final PasswordEncoder passwordEncoder;
    private final PostRepository postRepository;

    private final MessageRoomRepository messageRoomRepository;
    private final ScrapRepository scrapRepository;


    @Transactional(readOnly = true)
    public RedunCheckDto existsByEmail(String email) {
        return new RedunCheckDto(memberRepository.existsByEmail(email));
    }

    @Transactional(readOnly = true)
    public RedunCheckDto existsByNickname(String nickname) {
        return new RedunCheckDto(memberRepository.existsByNickname(nickname));
    }

    @Transactional(readOnly = true)
    public RedunCheckDto existsByPhoneNumber(String phoneNumber) {
        return new RedunCheckDto(memberRepository.existsByPhoneNumber(phoneNumber));
    }


    // 조회 메소드
    // 아이디로 유저 정보 조회
    @Transactional(readOnly = true)
    public UserInfo getUserInfo(Long userId) {
        Member byId = memberRepository.findById(userId).orElseThrow(NotFoundByIdException::new);
        Scrap scrap = scrapRepository.findScrapByMemberId(byId.getId()).orElseThrow(UserScrapNotFoundException::new);


        return byId.toUserInfo(scrap.getId());

    }


    // 업데이트 메소드
    // 유저 정보 업데이트 처리 메소드
    @Transactional
    public UserInfo updateUserInfo(UpdateUserInfo updateUserInfo, Long currentMemberId) {
        Member member = memberRepository.findById(currentMemberId).orElseThrow(NotFoundByIdException::new);
        if (!member.getNickname().equals(updateUserInfo.getNickname()) && memberRepository.existsByNickname(updateUserInfo.getNickname())) {
            throw new DuplicateNicknameException();
        }
        if (!member.getPhoneNumber().equals(updateUserInfo.getPhoneNumber()) && memberRepository.existsByPhoneNumber(updateUserInfo.getPhoneNumber())) {
            throw new DuplicatePhoneNumberExepction();
        }
        member.updateUserInfo(updateUserInfo);
        Scrap scrap = scrapRepository.findScrapByMemberId(member.getId()).orElseThrow(UserScrapNotFoundException::new);

        return member.toUserInfo(scrap.getId());
    }

    // 주소 업데이트 메소드
    @Transactional
    public void updateUserAddress(Address address, Long currentMemberId) {
        Member member = memberRepository.findById(currentMemberId).orElseThrow(NotFoundByIdException::new);
        member.updateAddress(address);
    }

    // 유저 비밀번호 업데이트 처리 메소드
    @Transactional
    public String updateUserPassword(UpdateUserPassword updateUserPassword, Long currentMemberId) {

        Member member = memberRepository.findById(currentMemberId).orElseThrow(NotFoundByIdException::new);
        if (!passwordEncoder.matches(updateUserPassword.getOldPassword(), member.getPassword())) {
            throw new InvalidPasswordException();
        }
        updateUserPassword.encrypt(passwordEncoder);
        member.updateUserPassword(updateUserPassword);
        return "UPDATE";
    }

    // 유저 삭제 메소드
    @Transactional
    public String deleteUser(Long currentMemberId) {
        Member member = memberRepository.findById(currentMemberId).orElseThrow(NotFoundByIdException::new);
        memberRepository.delete(member);
        return "DELETE";
    }

    // 이메일 찾기 메소드
    @Transactional(readOnly = true)
    public EncryptEmailDto findUserEmail(FindEmailDto findEmailDto) {
        String userName = findEmailDto.getUserName();
        String phoneNumber = findEmailDto.getPhoneNumber();
        Member member = memberRepository.findByUserNameAndPhoneNumber(userName, phoneNumber).orElseThrow(UserNotFoundByUsernameAndPhoneException::new);
        return member.encryptEmail();

    }

    // 비밀 번호 찾기 메소드
    @Transactional
    public ReturnPasswordDto findUserPassword(FindPasswordDto findPasswordDto) {
        String userName = findPasswordDto.getUserName();
        String phoneNumber = findPasswordDto.getPhoneNumber();
        String email = findPasswordDto.getEmail();
        Member member = memberRepository.findByUserNameAndPhoneNumberAndEmail(userName, phoneNumber, email).orElseThrow(UserNotFoundException::new);
        String newPassword = createRandomPassword();

        UpdateUserPassword updateUserPassword = UpdateUserPassword.builder()
                .newPassword(newPassword)
                .oldPassword("resetPassword").build();
        updateUserPassword.encrypt(passwordEncoder);
        member.updateUserPassword(updateUserPassword);
        return ReturnPasswordDto.builder().password(newPassword).build();
    }
    // 비밀번호 찾을때 랜덤한 비밀번호 생성해주는 메소드
    public String createRandomPassword() {
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = (int) (random.nextFloat() * RandomCharacters.length);
            stringBuilder.append(RandomCharacters[randomLimitedInt]);
        }

        return stringBuilder.toString();
    }


    // TODO 마이페이지 조회 메소드 필요할 것 같음 추가예정 - 조회할떄 각 정보 DTO로 감싸서 리턴하게 해야함, 각 도메인 별로 조회용 DTO 생성되면 구현 예정

    @Transactional(readOnly = true)
    public GetUserPostDto getUserPostDto(Long memberId) {
        List<Post> byAuthorId = postRepository.findByAuthorId(memberId);
        List<PostInfoDto> userPosts = new ArrayList<>();
        // TODO SPOTCAST 코드 보고 리스트 DTO로 뱉는거로 바꿀 예정
        for (Post post : byAuthorId) {
            userPosts.add(post.toInfoDto());
        }

        return GetUserPostDto.builder().userPosts(userPosts).build();

    }
    // TODO 아직 미구현
    @Transactional(readOnly = true)
    public GetUserMessageRoomDto getUserMessageRoomDto(Long memberId) {
//        messageRoomRepository.
        return null;

    }
}
