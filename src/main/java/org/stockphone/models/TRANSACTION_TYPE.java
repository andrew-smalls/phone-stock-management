package org.stockphone.models;

public enum TRANSACTION_TYPE {
    ADD("ADD"),
    UPDATE("UPDATE"),
    SALE("SALE"),
    RESELLER_RESERVE("RESELLER_RESERVE"),
    CLEAR("CLEAR");

    TRANSACTION_TYPE(String transactionDescription) {
    }
}
