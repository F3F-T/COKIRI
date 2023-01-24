package f3f.dev1.domain.member.application;

import f3f.dev1.domain.address.dao.AddressRepository;
import f3f.dev1.domain.address.dto.AddressDTO.AddressInfoDTO;
import f3f.dev1.domain.member.dao.MemberCustomRepositoryImpl;
import f3f.dev1.domain.member.dao.MemberRepository;
import f3f.dev1.domain.member.exception.*;
import f3f.dev1.domain.member.model.Member;
import f3f.dev1.domain.message.dao.MessageRoomCustomRepositoryImpl;
import f3f.dev1.domain.message.dao.MessageRoomRepository;
import f3f.dev1.domain.post.dao.PostRepository;
import f3f.dev1.domain.scrap.dao.ScrapRepository;
import f3f.dev1.domain.scrap.exception.UserScrapNotFoundException;
import f3f.dev1.domain.scrap.model.Scrap;
import f3f.dev1.global.error.exception.NotFoundByIdException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

import static f3f.dev1.domain.member.dto.MemberDTO.*;
import static f3f.dev1.global.common.constants.RandomCharacter.RandomCharacters;


@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final AddressRepository addressRepository;

    private final MemberRepository memberRepository;


    private final PasswordEncoder passwordEncoder;
    private final PostRepository postRepository;

    private final ScrapRepository scrapRepository;

    private final MemberCustomRepositoryImpl memberCustomRepositoryImpl;

    private final MessageRoomCustomRepositoryImpl messageRoomCustomRepositoryImpl;


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
    public UserInfoWithAddress getUserInfo(Long userId) {
        log.info("유저 정보 조회 호출됐음");

        return memberCustomRepositoryImpl.getUserInfo(userId);

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

    @Transactional
    public NewNicknameDto updateUserNickname(Long memberId, UpdateMemberNicknameDto updateMemberNicknameDto) {
        if (!Objects.equals(memberId, updateMemberNicknameDto.getUserId())) {
            throw new NotAuthorizedException();
        }
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundByIdException::new);
        member.updateNickname(updateMemberNicknameDto.getNewNickname());
        return NewNicknameDto.builder().newNickname(updateMemberNicknameDto.getNewNickname()).build();
    }

    @Transactional
    public NewPhoneNumberDto updateUserPhoneNumber(Long memberId, UpdateMemberPhoneNumberDto updateMemberPhoneNumberDto) {
        if (!Objects.equals(memberId, updateMemberPhoneNumberDto.getUserId())) {
            throw new NotAuthorizedException();
        }
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundByIdException::new);
        member.updatePhoneNumber(updateMemberPhoneNumberDto.getNewPhoneNumber());
        return NewPhoneNumberDto.builder().newPhoneNumber(updateMemberPhoneNumberDto.getNewPhoneNumber()).build();

    }

    @Transactional
    public NewDescriptionDto updateDescription(Long memberId, UpdateDescriptionDto updateDescriptionDto) {
        if (!Objects.equals(memberId, updateDescriptionDto.getUserId())) {
            throw new NotAuthorizedException();
        }
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundByIdException::new);
        member.updateDescription(updateDescriptionDto.getDescription());
        return NewDescriptionDto.builder().newDescription(updateDescriptionDto.getDescription()).build();

    }

    // 주소 업데이트 메소드 -> AddressService로 변경

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

    // TODO dto에 유저 검증 추가로 인한 코드 검토해야함
    // 유저 프사 변경 메소드
    @Transactional
    public NewImageUrlDto updateUserImage(UpdateUserImage updateUserImage, Long currentMemberId) {
        Member member = memberRepository.findById(currentMemberId).orElseThrow(NotFoundByIdException::new);
        member.updateImage(updateUserImage.getNewImageUrl());
        return NewImageUrlDto.builder().newImageUrl(updateUserImage.getNewImageUrl()).build();
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
    // QUERYDSL 적용해야함
    @Transactional(readOnly = true)
    public Page<GetUserPost> getUserPostDto(Long memberId, Pageable pageable) {
        List<GetUserPost> collect = postRepository.getUserPostById(memberId, pageable).stream().map(GetUserPost::new).collect(Collectors.toList());
        return new PageImpl<>(collect);


    }

    // TODO 아직 미구현
    @Transactional(readOnly = true)
    public Page<GetUserMessageRoom> getUserMessageRoom(Long memberId, Pageable pageable) {
        return messageRoomCustomRepositoryImpl.findUserMessageRoom(memberId, pageable);

    }

    // 멤버 주소 리스트 조회
    // TODO QueryDSL로 리팩터링 해야된다
    @Transactional(readOnly = true)
    public GetMemberAddressesDTO getMemberAddressesDTO(Long memberId) {
        List<AddressInfoDTO> userAddress = memberCustomRepositoryImpl.getUserAddress(memberId);

        return GetMemberAddressesDTO.builder().memberAddress(userAddress).build();
    }

    // 멤버 디테일 조회
    @Transactional(readOnly = true)
    public UserDetail getUserDetail(Long memberId) {
        return memberCustomRepositoryImpl.getUserDetail(memberId);
    }
}
