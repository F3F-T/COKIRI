package f3f.dev1.domain.member.api;

import f3f.dev1.domain.member.application.AuthService;
import f3f.dev1.domain.member.application.MemberService;
import f3f.dev1.domain.model.Address;
import f3f.dev1.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static f3f.dev1.domain.member.dto.MemberDTO.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;


    private final AuthService authService;

    // 유저 정보 조회
    @GetMapping(value = "/user")
    public ResponseEntity<UserInfo> getUserInfo() {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(memberService.getUserInfo(currentMemberId));

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

    @PatchMapping(value = "/user/image")
    public ResponseEntity<String> updateUserImage(@RequestBody UpdateUserImage updateUserImage) {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(memberService.updateUserImage(updateUserImage, currentMemberId));
    }

    // 유저 주소 업데이트
    @PatchMapping(value = "/user/address")
    public ResponseEntity<Address> updateUserAddress(@RequestBody UpdateUserAddress address) {

        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        memberService.updateUserAddress(address, currentMemberId);
        return ResponseEntity.ok(address.getAddress());
    }

    // 로그아웃
    @DeleteMapping("/user/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) throws IOException {

        String token = request.getHeader("Authorization").split(" ")[1];
        System.out.println("token = " + token);
        return ResponseEntity.ok(authService.logout(token));
    }


    // 마이페이지용 조회 - 유저가 작성한 게시글 리스트 리턴
    @GetMapping("/user/posts")
    public ResponseEntity<GetUserPostDto> getUserPosts() {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(memberService.getUserPostDto(currentMemberId));
    }
    // 유저가 속한 채팅방 리스트 리턴
    @GetMapping("/user/messagerooms")
    public ResponseEntity<GetUserMessageRoomDto> getUserMessageRooms() {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(memberService.getUserMessageRoomDto(currentMemberId));
    }
}
