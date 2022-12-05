package f3f.dev1.domain.scrap.model;
import f3f.dev1.domain.user.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Scrap {
    @Id
    @GeneratedValue
    @Column(name = "scrap_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
