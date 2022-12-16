package f3f.dev1.domain.member.application;

import f3f.dev1.domain.member.model.Member;
import f3f.dev1.domain.scrap.application.ScrapService;
import f3f.dev1.domain.member.dao.MemberRepository;
import f3f.dev1.domain.member.exception.*;
import f3f.dev1.global.error.exception.NotFoundByIdException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Random;

import static f3f.dev1.domain.scrap.dto.ScrapDTO.CreateScrapDTO;
import static f3f.dev1.domain.member.dto.MemberDTO.*;
import static f3f.dev1.global.common.constants.RandomCharacter.RandomCharacters;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final ScrapService scrapService;

//    private final SessionLoginService sessionLoginService;


    @Transactional(readOnly = true)
    public Boolean existsByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public Boolean existsByNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    @Transactional(readOnly = true)
    public Boolean existsByPhoneNumber(String phoneNumber){
        return memberRepository.existsByPhoneNumber(phoneNumber);}



    // authentication에 쓰이는 메소드, 이메일로 유저객체 리턴
    @Transactional(readOnly = true)
    public Member findUserInfoByEmail(String email) {

        return memberRepository.findByEmail(email).orElseThrow(UserNotFoundByEmailException::new);

    }


    // 회원가입 요청 처리 메소드, 유저 생성
    // signUpRequest로 넘어오는 값 검증은 컨트롤러에서 진행하게 구현 예정
    @Transactional
    public Long signUp(SignUpRequest signUpRequest) {
        if (memberRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new DuplicateEmailException();
        }
        if (memberRepository.existsByPhoneNumber(signUpRequest.getPhoneNumber())) {
            throw new DuplicatePhoneNumberExepction();
        }
        // TODO: 닉네임 중복 검사 추가
        if (memberRepository.existsByNickname(signUpRequest.getNickname())) {
            throw new DuplicateNicknameException();
        }


        signUpRequest.encrypt();

        Member member = signUpRequest.toEntity();

        memberRepository.save(member);
        CreateScrapDTO userScrap = CreateScrapDTO.builder().user(member).build();
        scrapService.createScrap(userScrap);
        return member.getId();
    }


    // 조회 메소드
    // 아이디로 유저 정보 조회
    @Transactional(readOnly = true)
    public UserInfo getUserInfo(Long userId) {
        Member byId = memberRepository.findById(userId).orElseThrow(NotFoundByIdException::new);

        return byId.toUserInfo();

    }


    // 업데이트 메소드
    // 유저 정보 업데이트 처리 메소드
    // TODO: 유저 닉네임 중복 검사 추가
    @Transactional
    public UserInfo updateUserInfo(UpdateUserInfo updateUserInfo) {
        Member member = memberRepository.findByEmail(sessionLoginService.getLoginUser()).orElseThrow(UserNotFoundByEmailException::new);
        if (!member.getNickname().equals(updateUserInfo.getNickname()) && memberRepository.existsByNickname(updateUserInfo.getNickname())) {
            throw new DuplicateNicknameException();
        }
        if (!member.getPhoneNumber().equals(updateUserInfo.getPhoneNumber()) && memberRepository.existsByPhoneNumber(updateUserInfo.getPhoneNumber())) {
            throw new DuplicatePhoneNumberExepction();
        }
        member.updateUserInfo(updateUserInfo);
        return member.toUserInfo();
    }

    // 유저 비밀번호 업데이트 처리 메소드
    @Transactional
    public String updateUserPassword(UpdateUserPassword updateUserPassword) {

        Member member = memberRepository.findByEmail(sessionLoginService.getLoginUser()).orElseThrow(UserNotFoundByEmailException::new);
        updateUserPassword.encrypt();

        if (!Objects.equals(member.getPassword(), updateUserPassword.getOldPassword())) {
            throw new InvalidPasswordException();
        }
        member.updateUserPassword(updateUserPassword);
        return "UPDATE";
    }

    // 유저 삭제 메소드
    @Transactional
    public String deleteUser() {
        Member member = memberRepository.findByEmail(sessionLoginService.getLoginUser()).orElseThrow(UserNotFoundByEmailException::new);
        memberRepository.delete(member);
        sessionLoginService.logout();
        return "DELETE";
    }
    // 이메일 찾기 메소드
    @Transactional
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
        updateUserPassword.encrypt();
        member.updateUserPassword(updateUserPassword);
        return ReturnPasswordDto.builder().password(newPassword).build();
    }

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


    // TODO: 마이페이지 조회 메소드 필요할 것 같음 추가예정 - 조회할떄 각 정보 DTO로 감싸서 리턴하게 해야함, 각 도메인 별로 조회용 DTO 생성되면 구현 예정
    // TODO: 이메일 인증 추가해야된다.

}
