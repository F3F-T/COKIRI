package f3f.dev1.domain.member.model;

import f3f.dev1.domain.model.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class MemberBase extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String email;

    private String password;



    @Enumerated(EnumType.STRING)
    protected Authority authority;

    @Enumerated(EnumType.STRING)
    private UserLoginType userLoginType;

    public void updatePassword(String pw) {
        this.password = pw;
    }



}
