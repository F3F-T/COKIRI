package f3f.dev1.domain.member.dto;

import com.querydsl.core.annotations.QueryProjection;
import f3f.dev1.domain.member.model.Member;
import f3f.dev1.domain.member.model.UserLoginType;
import f3f.dev1.domain.address.model.Address;
import f3f.dev1.domain.model.TradeStatus;
import f3f.dev1.domain.post.dao.PostRepository;
import lombok.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

import static f3f.dev1.domain.address.dto.AddressDTO.*;
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

        public void encrypt(PasswordEncoder passwordEncoder) {
            this.password = passwordEncoder.encode(password);
        }

        public Member toEntity() {

            return Member.builder()
                    .username(userName)
                    .nickname(nickname)
                    .phoneNumber(phoneNumber)
                    .description("")
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


        public void encrypt(PasswordEncoder passwordEncoder) {
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

        private String imageUrl;

        private String nickname;

        private String description;

        private String phoneNumber;

        private String email;

        private String birthDate;

        private UserLoginType loginType;


    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class UserInfoWithAddress {
        private UserDetail userDetail;

        private List<AddressInfoDTO> address;
    }


    @Builder
    @NoArgsConstructor
    @Getter
    public static class UserDetail {
        private Long id;

        private Long scrapId;
        private String userName;

        private String imageUrl;

        private String nickname;

        private String description;

        private String phoneNumber;

        private String email;

        private String birthDate;

        private UserLoginType loginType;

        @QueryProjection
        public UserDetail(Long id, Long scrapId, String userName, String imageUrl, String nickname, String description, String phoneNumber, String email, String birthDate, UserLoginType loginType) {
            this.id = id;
            this.scrapId = scrapId;
            this.userName = userName;
            this.imageUrl = imageUrl;
            this.nickname = nickname;
            this.description = description;
            this.phoneNumber = phoneNumber;
            this.email = email;
            this.birthDate = birthDate;
            this.loginType = loginType;
        }
    }


    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class UpdateUserInfo {

        private Long userId;

        private String nickname;

        private Address address;

        private String phoneNumber;

    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class UpdateUserPassword {

        private Long userId;

        private String oldPassword;

        private String newPassword;

        public void encrypt(PasswordEncoder passwordEncoder) {
            this.oldPassword = passwordEncoder.encode(oldPassword);
            this.newPassword = passwordEncoder.encode(newPassword);
        }
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class UpdateUserImage {

        private Long userId;

        private String newImageUrl;
    }


    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class FindEmailDto {
        private String userName;
        private String phoneNumber;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class EncryptEmailDto {
        private String email;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class FindPasswordDto {
        private String userName;
        private String phoneNumber;

        private String email;

    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class ReturnPasswordDto {
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
        private UserInfoWithAddress userInfo;

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
    public static class RedunCheckDto {
        private Boolean exists;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class UpdateMemberNicknameDto {
        private Long userId;

        private String newNickname;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class UpdateMemberPhoneNumberDto {
        private Long userId;

        private String newPhoneNumber;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class NewNicknameDto {
        private String newNickname;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class NewPhoneNumberDto {
        private String newPhoneNumber;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class UpdateDescriptionDto {
        private Long userId;
        private String description;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class NewDescriptionDto {
        private String newDescription;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class NewImageUrlDto {
        private String newImageUrl;
    }


    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class GetMemberAddressesDTO {
        private List<AddressInfoDTO> memberAddress;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class ImageUrlDto {
        private List<String> imageUrls;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class SimpleLoginDto {

        private String userId;

        private TokenIssueDTO tokenInfo;

    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class GetUserPost {
        private Long postId;
        private String thumbNail;
        private String title;
        private String tradeStatus;
        private String wishCategory;
        private Long likeCount;

        public GetUserPost(PostRepository.GetUserPostInterface getUserPostInterface) {
            this.postId = getUserPostInterface.getPostId();
            this.thumbNail = getUserPostInterface.getThumbnail();
            this.title = getUserPostInterface.getTitle();
            if (getUserPostInterface.getTradeStatus().equals("0")) {
                this.tradeStatus = TradeStatus.TRADABLE.name();
            } else if (getUserPostInterface.getTradeStatus().equals("1")) {
                this.tradeStatus = TradeStatus.TRADING.name();
            } else {
                this.tradeStatus = TradeStatus.TRADED.name();
            }
            this.wishCategory = getUserPostInterface.getName();
            this.likeCount = getUserPostInterface.getLikes();
        }
    }



    @NoArgsConstructor
    @Getter
    public static class GetUserMessageRoom {
        private Long messageRoomId;
        private Long authorId;
        private Long postId;
        private String lastMsg;
        private LocalDateTime createdDate;
        private String buyerNickname;
        private String sellerNickname;
        private Long buyerId;
        private Long sellerId;
        private String buyerThumbnail;
        private String sellerThumbnail;

        private boolean buyerDelStatus;
        private boolean sellerDelStatus;
        private String thumbnailImgPath;
        private TradeStatus tradeStatus;
        private String title;
        private String productCategory;
        private String wishCategory;
        private Long price;


        @Builder
        @QueryProjection
        public GetUserMessageRoom(Long messageRoomId, Long authorId, Long postId, String lastMsg, LocalDateTime createdDate, Long buyerId, Long sellerId, String buyerNickname, String sellerNickname, String buyerThumbnail, String sellerThumbnail, boolean buyerDelStatus, boolean sellerDelStatus, String thumbnailImgPath, TradeStatus tradeStatus, String title, String productCategory, String wishCategory, Long price) {

            this.messageRoomId = messageRoomId;
            this.authorId = authorId;
            this.postId = postId;
            this.lastMsg = lastMsg;
            this.createdDate = createdDate;
            this.buyerId = buyerId;
            this.sellerId = sellerId;
            this.buyerNickname = buyerNickname;
            this.sellerNickname = sellerNickname;
            this.buyerThumbnail = buyerThumbnail;
            this.sellerThumbnail = sellerThumbnail;
            this.buyerDelStatus = buyerDelStatus;
            this.sellerDelStatus = sellerDelStatus;
            this.thumbnailImgPath = thumbnailImgPath;
            this.title = title;
            this.productCategory = productCategory;
            this.wishCategory = wishCategory;
            this.tradeStatus = tradeStatus;
            this.price = price;
        }
    }


    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class GetOtherUserInfoDto {
        private List<AddressInfoDTO> address;
        private SimpleUserInfo userInfo;

    }

    @Builder
    @NoArgsConstructor
    @Getter
    public static class SimpleUserInfo {
        private String nickname;
        private String imageUrl;

        @QueryProjection
        public SimpleUserInfo(String nickname, String imageUrl) {
            this.nickname = nickname;
            this.imageUrl = imageUrl;
        }
    }


}
