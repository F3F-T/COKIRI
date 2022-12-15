package f3f.dev1.global.common.annotation;

import f3f.dev1.domain.user.model.UserLevel;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(METHOD)
public @interface LoginCheck {

    UserLevel authority() default UserLevel.UNAUTH;
}
