package f3f.dev1.domain.member.model;

import f3f.dev1.domain.comment.model.Comment;
import f3f.dev1.domain.message.model.Message;
import f3f.dev1.domain.message.model.MessageRoom;
import f3f.dev1.domain.model.Address;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.scrap.model.Scrap;
import f3f.dev1.domain.trade.model.Trade;
import f3f.dev1.domain.member.dto.MemberDTO.EncryptEmailDto;
import f3f.dev1.domain.member.dto.MemberDTO.UpdateUserPassword;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static f3f.dev1.domain.member.dto.MemberDTO.UpdateUserInfo;
import static f3f.dev1.domain.member.dto.MemberDTO.UserInfo;
import static f3f.dev1.domain.member.model.Authority.ROLE_USER;

@Entity
@Getter
@NoArgsConstructor
public class Member extends MemberBase {

    @Embedded
    private Address address;

    private String birthDate;

    private String phoneNumber;

    private String userName;

    private String nickname;

    private String imageUrl;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    @OneToOne(mappedBy = "member", cascade = CascadeType.REMOVE)
    private Scrap scrap;

    @OneToMany(mappedBy = "seller", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<MessageRoom> sellingRooms = new ArrayList<>();

    @OneToMany(mappedBy = "buyer", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<MessageRoom> buyingRooms = new ArrayList<>();

    @OneToMany(mappedBy = "sender", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Message> sendMessages = new ArrayList<>();

    @OneToMany(mappedBy = "receiver", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Message> receivedMessages = new ArrayList<>();

//    @OneToMany(mappedBy = "buyer", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
//    private List<Trade> buyingTrades = new ArrayList<>();

    @OneToMany(mappedBy = "seller", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Trade> sellingTrades = new ArrayList<>();

    @Builder
    public Member(Long id, String email, String password, String username, Address address, String birthDate, String phoneNumber, String nickname, UserLoginType userLoginType) {
        super(id, email, password, ROLE_USER, userLoginType);
        this.userName = username;
        this.address = address;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.nickname = nickname;
        this.imageUrl = "https://cdn-icons-png.flaticon.com/128/7178/7178514.png";
    }

    public UserInfo toUserInfo(Long scrapId) {
        return UserInfo.builder()
                .address(this.address)
                .userName(this.userName)
                .email(getEmail())
                .phoneNumber(this.phoneNumber)
                .nickname(this.nickname)
                .loginType(this.getUserLoginType())
                .imageUrl(this.imageUrl)
                .id(this.getId())
                .scrapId(scrapId)
                .birthDate(this.birthDate)
                .build();
    }

    public void updateUserInfo(UpdateUserInfo updateUserInfo) {
        this.address = updateUserInfo.getAddress();
        this.nickname = updateUserInfo.getNickname();
        this.phoneNumber = updateUserInfo.getPhoneNumber();

    }

    public void updateUserPassword(UpdateUserPassword updateUserPassword) {
        super.updatePassword(updateUserPassword.getNewPassword());
    }

    public void updateAddress(Address address) {
        this.address = address;
    }

    public void updateImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void updateNickname(String nickname){
        this.nickname = nickname;
    }

    public void updatePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }



    public EncryptEmailDto encryptEmail() {
        String[] subEmail = this.getEmail().split("@");
        int asteriskNum = subEmail[0].length() - 3;
        String asterisks = "*".repeat(asteriskNum);
        subEmail[0] = subEmail[0].substring(0, 3) + asterisks;
        String returnEmail = subEmail[0] + "@" + subEmail[1];
        return new EncryptEmailDto(returnEmail);
    }
}
