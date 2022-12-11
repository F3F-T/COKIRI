package f3f.dev1.domain.user.session;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

// 구글에서 스프링 로그인 관련해서 찾아보다가 나중에 쓸 수도 있을 것 같아서 복붙해놓은 코드입니다
@Component
public class SessionManager {

    public static final String SESSION_COOKIE_NAME = "userSessionId";

    private final Map<String, Object> sessionStore = new ConcurrentHashMap<>();

    public void createSession(Object value, HttpServletResponse response){

        // SessionId 생성, 값을 세션에 저장
        String SessionId = UUID.randomUUID().toString(); // UUID 를 활용한 SessionId 생성
        sessionStore.put(SessionId, value); // 세션 저장소에 SessionId 와 보관할 값 저장

        // 쿠키 생성 : 쿠키 이름은 SESSION_COOKIE_NAME , 값은 SessionId
        Cookie mySessionCookie = new Cookie(SESSION_COOKIE_NAME, SessionId);
        response.addCookie(mySessionCookie);
    }

    public Object getSession(HttpServletRequest request){
        // SessionCookie 에 findCookie 를 메서드를 사용해서 찾아온 SESSION_COOKIE_NAME 를 저장함
        Cookie Sessioncookie = findCookie(request, SESSION_COOKIE_NAME);
        if(Sessioncookie == null){
            return null;
        }
        return sessionStore.get(Sessioncookie.getValue());
    }

    public Cookie findCookie(HttpServletRequest request, String cookieName){
        // 여기서 CookieName 는 SESSION_COOKIE_NAME 의미

        Cookie[] cookies = request.getCookies();

        if(cookies == null){
            return null;
        }
        return Arrays.stream(cookies) // arrays 를 스트림을 바꿔줌
                .filter(cookie -> cookie.getName().equals(cookieName))
                /*
                findfirst 와 findAny 둘중 하나를 쓸 수 있는데
                1. findAny : 순서 상관X! 빨리 나오면 꺼내옴
                2. findfrist : 순서 중요! 순서에 따라서 돌다가 맞으면 꺼내옴
                 */
                .findAny()
                .orElse(null);
    }

    /*
     * 3. 세션 만료 : Session 만료는 그냥 지워버리면 된다
     */
    public void expireCookie(HttpServletRequest request){
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);

        // findCookie 로 세션을 가져와서 해당 값이 null 이 아니면 세션 스토어에 저장, 매핑된 값을 삭제!
        if(sessionCookie != null){
            sessionStore.remove(sessionCookie.getValue());
        }
    }
}
