package f3f.dev1.global.common.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME) //어노테이션을 런타임시에까지 사용할 수 있습니다. JVM이 자바 바이트코드가 담긴 class 파일에서 런타임환경을 구성하고 런타임을 종료할 때까지 메모리는 살아있다.
@Target(PARAMETER)
public @interface CurrentUser {
}
