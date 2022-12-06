package f3f.dev1.domain.model;

public enum TradeStatus {
    TRADABLE(1, "tradable"), TRADING(2, "trading"), TRADED(3, "traded");
    private long id;
    private String name;

    TradeStatus(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
