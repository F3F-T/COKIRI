package f3f.dev1.domain.user.api;

import f3f.dev1.domain.user.application.UserService;
import f3f.dev1.domain.user.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final SessionManager sessionManager;


}
