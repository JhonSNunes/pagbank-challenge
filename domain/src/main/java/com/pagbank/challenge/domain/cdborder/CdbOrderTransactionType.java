package com.pagbank.challenge.domain.cdborder;

public enum CdbOrderTransactionType {
    PURCHASE,
    SELL;

    public boolean isSell() {
        return this == SELL;
    }

    public boolean isPurchase() {
        return this == PURCHASE;
    }
}
