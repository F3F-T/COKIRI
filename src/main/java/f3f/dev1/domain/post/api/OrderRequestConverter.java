package f3f.dev1.domain.post.api;

import f3f.dev1.domain.post.model.SortOrder;
import org.springframework.core.convert.converter.Converter;

// query string으로 Enum 타입을 받는데, 이걸 자동으로 변화해줄 클래스
public class OrderRequestConverter implements Converter<String, SortOrder> {
    @Override
    public SortOrder convert(String orderValue) {
        return SortOrder.of(orderValue);
    }
}
