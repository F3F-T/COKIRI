package f3f.dev1.domain.user.dto;

import f3f.dev1.domain.model.Address;
import f3f.dev1.domain.user.model.User;
import f3f.dev1.domain.user.model.UserLoginType;
import f3f.dev1.global.config.EncryptionService;
import f3f.dev1.global.config.SHA256Encryptor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserDTO {

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

        public User toEntity() {

            return User.builder()
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
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class UpdateUserInfo {

        private String nickname;

        private Address address;

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



}
