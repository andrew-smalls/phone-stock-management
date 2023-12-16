package models;

public enum TRANSACTION_TYPE {
    ADD("add"),
    UPDATE("update"),
    SALE("SALE"),
    RETURN("return"),
    CLEAR("clear");

    TRANSACTION_TYPE(String add) {
    }

    public String getValue(TRANSACTION_TYPE transactionType) {
        return transactionType.name();
    }
}
