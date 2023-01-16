package f3f.dev1.domain.post.model;

import f3f.dev1.domain.post.exception.NullOrderValueException;

public enum Order {
    // 인기순
    POPULARITY,
    // 최신순 (default)
    CURRENT;

    public static Order of(String orderValue) {
        if(orderValue == null) {
            throw new NullOrderValueException();
        }
        for (Order value : Order.values()) {
            if(value.name().equals(orderValue)) {
                return value;
            }
        }
        return CURRENT;
    }
}
