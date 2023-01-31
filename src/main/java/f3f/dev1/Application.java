package f3f.dev1;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @EnableCaching
 * @EnableScheduling
 * 위 2가지 애노테이션은 캐싱을 위해서 추가하였다.
 * 스케쥴링은 주기적으로 캐시를 삭제해주기 위해서 사용된다.
 * 만료 시간을 스프링에서 기본적으로 제공해주지 않기 때문에 이렇게 별도로 스케쥴링을 등록해서 사용하는 방법을 채택하였다.
 **/
@EnableCaching
@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
