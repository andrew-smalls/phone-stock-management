package models;

public enum TRANSACTION_TYPE {
    ADD("ADD"),
    UPDATE("UPDATE"),
    SALE("SALE"),
    RETURN("RETURN"),
    RESELLER_RESERVE("RESELLER_RESERVE"),
    CLEAR("CLEAR");

    TRANSACTION_TYPE(String add) {
    }
}
