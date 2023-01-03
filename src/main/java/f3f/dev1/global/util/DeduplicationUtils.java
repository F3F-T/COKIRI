package f3f.dev1.global.util;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DeduplicationUtils {

    // 중복 제거 유틸 클래스

    /**
     * @param list : 중복이 있는 list
     * @param key : 중복 제거의 기준이 되는 key
     * @param <T> : 제네릭
     * @return list : 중복이 제거된 list
     */

    public static <T> List<T> deduplication(final List<T> list, Function<? super T, ?> key) {
        return list.stream()
                .filter(deduplication(key))
                .collect(Collectors.toList());
    }

    private static <T> Predicate<T> deduplication(Function<? super T, ?> key) {
        final Set<Object> set = ConcurrentHashMap.newKeySet();
        return predicate -> set.add(key.apply(predicate));
    }
}
