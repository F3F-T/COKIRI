package f3f.dev1.domain.member.application;

//import f3f.dev1.domain.member.dao.MemberRepository;
//import f3f.dev1.domain.member.exception.UserNotFoundByEmailException;
//import f3f.dev1.domain.member.exception.UserNotFoundException;
//import f3f.dev1.domain.member.model.Member;
//import f3f.dev1.domain.member.model.UserLevel;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.servlet.http.HttpSession;
//
//import static f3f.dev1.domain.member.dto.MemberDTO.LoginRequest;
//import static f3f.dev1.global.common.constants.UserConstants.AUTH_State;
//import static f3f.dev1.global.common.constants.UserConstants.USER_ID;
//
//@Service
//@RequiredArgsConstructor
//public class SessionLoginService {
//    private final HttpSession httpSession;
//
//    private final MemberRepository memberRepository;
//
//    @Transactional(readOnly = true)
//    public void existsByEmailAndPassword(LoginRequest loginRequest) {
//        loginRequest.encrypt();
//        String email = loginRequest.getEmail();
//        String password = loginRequest.getPassword();
//        if (!memberRepository.existsByEmailAndPassword(email, password)) {
//            throw new UserNotFoundException("아이디 또는 비밀번호가 일치하지 않습니다.");
//        }
//    }
//
//    @Transactional(readOnly = true)
//    public Long login(LoginRequest loginRequest) {
//        existsByEmailAndPassword(loginRequest);
//        String email = loginRequest.getEmail();
//        setUserLevel(email);
//        httpSession.setAttribute(USER_ID, email);
//        httpSession.setMaxInactiveInterval(3600*24);
//
//        return memberRepository.findByEmail(email).orElseThrow(UserNotFoundByEmailException::new).getId();
//    }
//
//    public void setUserLevel(String email){
//        Member member = memberRepository.findByEmail(email)
//                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다."));
//
//        httpSession.setAttribute(AUTH_State, member.getUserLevel());
//    }
//
//    public void logout() {
//        httpSession.removeAttribute(USER_ID);
//
//    }
//
//
//    public String getLoginUser() {
//        return (String) httpSession.getAttribute(USER_ID);
//    }
//
//    public UserLevel getUserLevel() {
//        return (UserLevel) httpSession.getAttribute(AUTH_State);
//    }
//}
