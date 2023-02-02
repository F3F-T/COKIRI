package f3f.dev1.global.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;

public class CustomKeyGenerator implements KeyGenerator {


    @Override
    public Object generate(Object target, Method method, Object... params) {
        final StringBuilder sb = new StringBuilder();
        for(int i=0; i< params.length; i++) {
            // hashCode는 일반적으로 각 객체의 주소값을 변환하여 생성한 객체의 고유한 정수값이다.
            if (params[i] != null) {
                sb.append(params[i].hashCode());
            }
            if(i != params.length - 1) {
                sb.append("-");
            }
        }
        return String.format("%s-%s", method.getName(), sb);
    }
}
