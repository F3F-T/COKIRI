package f3f.dev1.global.config.intercepter;

//import f3f.dev1.domain.member.application.SessionLoginService;
//import f3f.dev1.domain.member.exception.NotAuthorizedException;
//import f3f.dev1.domain.member.exception.UnauthenticatedUserException;
//import f3f.dev1.domain.member.model.UserLevel;
//import f3f.dev1.global.common.annotation.LoginCheck;
//import lombok.RequiredArgsConstructor;
//import org.springframework.core.env.Environment;
//import org.springframework.stereotype.Component;
//import org.springframework.web.method.HandlerMethod;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import javax.inject.Inject;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import java.util.Objects;
//
//import static f3f.dev1.domain.member.model.UserLevel.AUTH;
//
//@Component
//@RequiredArgsConstructor
//public class LoginCheckInterceptor implements HandlerInterceptor {
//    private final SessionLoginService sessionLoginService;
//    @Inject
//    private final Environment environment;
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
//        String[] activeProfiles = environment.getActiveProfiles();
//
////        if("test".equals(activeProfiles[0])){
////            return true;
////        }
//
//        if (handler instanceof HandlerMethod) {
//            HandlerMethod handlerMethod = (HandlerMethod) handler;
//            LoginCheck loginCheck = handlerMethod.getMethodAnnotation(LoginCheck.class);
//
//
//            if (loginCheck == null) {
//                return true;
//            }
//
//
//            if (sessionLoginService.getLoginUser() == null) {
//                throw new UnauthenticatedUserException("로그인 후 이용 가능합니다.");
//            }
//
//            UserLevel auth = loginCheck.authority();
//
//
//            if (Objects.requireNonNull(auth) == AUTH) {
//                authUserLevel();
//            }
//
//        }
//        return true;
//    }
//
//    private void authUserLevel() {
//        UserLevel auth = sessionLoginService.getUserLevel();
//        if(auth == UserLevel.UNAUTH){
//            throw new NotAuthorizedException("해당 리소스에 대한 접근 권한이 존재하지 않습니다.");
//        }
//    }
//}
