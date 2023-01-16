package f3f.dev1.domain.post.model;

import f3f.dev1.domain.post.exception.NullOrderValueException;

public enum SortOrder {
    // 인기순
    POPULARITY,
    // 최신순 (default)
    CURRENT;

    public static SortOrder of(String orderValue) {
        if(orderValue == null) {
            throw new NullOrderValueException();
        }
        for (SortOrder value : SortOrder.values()) {
            if(value.name().equals(orderValue)) {
                return value;
            }
        }
        return CURRENT;
    }
}
