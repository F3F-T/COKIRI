package f3f.dev1.user;

import f3f.dev1.domain.model.Address;
import f3f.dev1.domain.scrap.dao.ScrapRepository;
import f3f.dev1.domain.user.application.UserService;
import f3f.dev1.domain.user.dao.UserRepository;
import f3f.dev1.domain.user.dto.UserDTO;
import f3f.dev1.domain.user.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

@Transactional
@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    ScrapRepository scrapRepository;

    @Test
    @DisplayName("유저 생성 성공 테스트")
    public void signUpTestSuccess() throws Exception{
        //given
        UserDTO.SignUpRequest signUpRequest = new UserDTO.SignUpRequest("username", "nickname", new Address(), "phoneNumber", "email", "password");


        // when
        Long userId = userService.signUp(signUpRequest);
        Optional<User> byId = userRepository.findById(userId);
        // then
        assertThat(byId.get().getId()).isEqualTo(userId);
    }
}
