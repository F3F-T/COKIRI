package f3f.dev1.domain.model;

public enum TradeStatus {
    TRADABLE(1, "tradable"), TRADING(2, "trading"), TRADED(3, "traded");
    private final long id;
    private final String name;

    TradeStatus(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public static TradeStatus findById(Long id) {
        for(TradeStatus status : TradeStatus.values()) {
            if(status.getId().equals(id))
                return status;
        }
        throw new IllegalArgumentException("임시 예외 : TradeStatus에 해당하지 않는 id값");
    }
}
