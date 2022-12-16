package f3f.dev1.domain.member.dto;

import f3f.dev1.domain.model.Address;
import f3f.dev1.domain.member.model.Member;
import f3f.dev1.domain.member.model.UserLoginType;
import f3f.dev1.global.config.EncryptionService;
import f3f.dev1.global.config.SHA256Encryptor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberDTO {

    static EncryptionService encryptionService = new SHA256Encryptor();

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class SignUpRequest {


        private String userName;

        private String nickname;

        private Address address;

        private String phoneNumber;

        private String email;

        private String password;

        private String birthDate;

        private UserLoginType userLoginType;

        public void encrypt(){
            password = encryptionService.encrypt(password);
        }

        public Member toEntity() {

            return Member.builder()
                    .username(userName)
                    .nickname(nickname)
                    .address(address)
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


        public void encrypt(){
            password = encryptionService.encrypt(password);
        }

    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class UserInfo {
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

        public void encrypt(){
            oldPassword = encryptionService.encrypt(oldPassword);
            newPassword = encryptionService.encrypt(newPassword);
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
    public static class EmailConfirmCodeDto {
        private String code;
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



}
