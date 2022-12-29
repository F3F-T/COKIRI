package f3f.dev1.domain.member.dto;

import f3f.dev1.domain.member.model.Member;
import f3f.dev1.domain.member.model.UserLoginType;
import f3f.dev1.domain.model.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static f3f.dev1.domain.post.dto.PostDTO.PostInfoDto;
import static f3f.dev1.domain.token.dto.TokenDTO.TokenIssueDTO;

public class MemberDTO {


    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class SignUpRequest {


        private String userName;

        private String nickname;


        private String phoneNumber;

        private String email;

        private String password;

        private String birthDate;

        private UserLoginType userLoginType;

        public void encrypt(PasswordEncoder passwordEncoder){
            this.password = passwordEncoder.encode(password);
        }

        public Member toEntity() {

            return Member.builder()
                    .username(userName)
                    .nickname(nickname)
                    .phoneNumber(phoneNumber)
                    .birthDate(birthDate)
                    .email(email)
                    .password(password)
                    .userLoginType(userLoginType)
                    .build();
        }
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class LoginRequest {
        private String email;

        private String password;


        public void encrypt(PasswordEncoder passwordEncoder){
            this.password = passwordEncoder.encode(password);
        }

        public UsernamePasswordAuthenticationToken toAuthentication() {
            return new UsernamePasswordAuthenticationToken(email, password);
        }

    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class UserInfo {

        private Long id;

        private Long scrapId;
        private String userName;

        private String nickname;

        private Address address;

        private String phoneNumber;

        private String email;

        private UserLoginType loginType;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class UpdateUserInfo {

        private String nickname;

        private Address address;

        private String phoneNumber;

    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class UpdateUserPassword {

        private String oldPassword;

        private String newPassword;

        public void encrypt(PasswordEncoder passwordEncoder){
            this.oldPassword = passwordEncoder.encode(oldPassword);
            this.newPassword = passwordEncoder.encode(newPassword);
        }
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class FindEmailDto{
        private String userName;
        private String phoneNumber;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class EncryptEmailDto{
        private String email;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class FindPasswordDto{
        private String userName;
        private String phoneNumber;

        private String email;

    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class ReturnPasswordDto{
        private String password;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class ConfirmEmailDto {
        private String email;
    }
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class EmailSentDto {
        private String email;
        private boolean success;
    }



    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class EmailConfirmCodeDto {

        private String email;
        private String code;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class CodeConfirmDto {
        private boolean matches;
    }


    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class CheckEmailDto {
        private String email;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class CheckNicknameDto {
        private String nickname;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class CheckPhoneNumberDto {
        private String phoneNumber;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class UserLoginDto {
        private UserInfo userInfo;

        private TokenIssueDTO tokenInfo;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class GetUserPostDto {
        List<PostInfoDto> userPosts;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class GetUserMessageRoomDto {
        private Long messageRoomId;

        private Long postId;

        private String opponentNickname;

    }


    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class RedunCheckDto {
        private Boolean exists;
    }




}
