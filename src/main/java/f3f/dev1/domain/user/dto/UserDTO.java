package f3f.dev1.domain.user.dto;

import f3f.dev1.domain.model.Address;
import f3f.dev1.domain.user.model.User;
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

        public User toEntity() {
            password = encryptionService.encrypt(password);
            return User.builder()
                    .username(userName)
                    .nickname(nickname)
                    .address(address)
                    .phoneNumber(phoneNumber)
                    .birthDate(birthDate)
                    .email(email)
                    .password(password)
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
        private Long id;

        private String nickname;

        private Address address;

    }

}
