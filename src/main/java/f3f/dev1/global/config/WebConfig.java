package f3f.dev1.global.config;

import f3f.dev1.domain.post.api.OrderRequestConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PATCH", "DELETE")
                .allowCredentials(true)
                .maxAge(3000);
    }

    /*
    queryParamter로 전달받은 게시글 정렬 기준이 Enum class로 선언되어있는데,
    전달받은 String을 자동으로 Enum 타입으로 convert 해주는 converter를 등록하는 코드.
     */
}
