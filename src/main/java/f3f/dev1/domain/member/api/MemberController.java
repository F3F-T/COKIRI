package f3f.dev1.domain.member.api;

import f3f.dev1.domain.member.application.MemberService;
import f3f.dev1.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static f3f.dev1.domain.member.dto.MemberDTO.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;


    // 유저 정보 조회
    @GetMapping(value = "/user")
    public ResponseEntity<UserInfoWithAddress> getUserInfo() {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(memberService.getUserInfo(currentMemberId));

    }

    // 유저 디테일 조회
    @GetMapping(value = "/user/detail")
    public ResponseEntity<UserDetail> getUserDetail() {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(memberService.getUserDetail(currentMemberId));
    }

    // 유저 정보 수정
    @PatchMapping(value = "/user")
    public ResponseEntity<UserInfo> updateUserInfo(@RequestBody UpdateUserInfo updateUserInfo) {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(memberService.updateUserInfo(updateUserInfo, currentMemberId));
    }

    // 유저 삭제
    @DeleteMapping(value = "/user")
    public ResponseEntity<String> deleteUser() {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(memberService.deleteUser(currentMemberId));

    }

    // 유저 비밀번호 변경
    @PatchMapping(value = "/user/password")
    public ResponseEntity<String> updateUserPassword(@RequestBody UpdateUserPassword updateUserPassword) {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(memberService.updateUserPassword(updateUserPassword, currentMemberId));

    }


    // 유저 닉네임 업데이트
    @PatchMapping(value = "/user/nickname")
    public ResponseEntity<NewNicknameDto> updateUserNickname(@RequestBody UpdateMemberNicknameDto updateMemberNicknameDto) {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(memberService.updateUserNickname(currentMemberId, updateMemberNicknameDto));

    }

    // 유저 폰번호 업데이트
    @PatchMapping(value = "/user/phone")
    public ResponseEntity<NewPhoneNumberDto> updateUserPhoneNumber(@RequestBody UpdateMemberPhoneNumberDto updateMemberPhoneNumberDto) {

        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(memberService.updateUserPhoneNumber(currentMemberId, updateMemberPhoneNumberDto));

    }

    // 유저 한줄 소개 업데이트
    @PatchMapping(value = "/user/description")
    public ResponseEntity<NewDescriptionDto> updateUserDescription(@RequestBody UpdateDescriptionDto updateDescriptionDto) {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(memberService.updateDescription(currentMemberId, updateDescriptionDto));
    }

    // 유저 이미지 url 업데이트
    @PatchMapping(value = "/user/imageUrl")
    public ResponseEntity<NewImageUrlDto> updateUserImageUrl(@RequestBody UpdateUserImage updateUserImage) {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(memberService.updateUserImage(updateUserImage, currentMemberId));
    }

    // 유저 주소 업데이트 --> AddressController로 변경


    // 마이페이지용 조회 - 유저가 작성한 게시글 리스트 리턴
    @GetMapping("/user/posts")
    public ResponseEntity<Page<GetUserPost>> getUserPosts(Pageable pageable) {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(memberService.getUserPostDto(currentMemberId, pageable));
    }


    // 유저가 속한 채팅방 리스트 리턴
    @GetMapping("/user/messageRooms")
    public ResponseEntity<Page<GetUserMessageRoom>> getUserMessageRooms(Pageable pageable) {

        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(memberService.getUserMessageRoom(currentMemberId, pageable));
    }

    // 유저 주소 리스트 조회
    @GetMapping("/user/address")
    public ResponseEntity<GetMemberAddressesDTO> getMemberAddress() {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(memberService.getMemberAddressesDTO(currentMemberId));
    }

    @GetMapping("/user/{user_id}")
    public ResponseEntity<GetOtherUserInfoDto> getUserById(@PathVariable(name = "user_id") Long userId) {
        return ResponseEntity.ok(memberService.getOtherUserById(userId));
    }
}
