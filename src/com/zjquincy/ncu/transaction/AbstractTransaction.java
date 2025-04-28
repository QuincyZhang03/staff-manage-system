package com.zjquincy.ncu.transaction;

public abstract class AbstractTransaction {
    private int id;
    private TransactionType type;
    public abstract void execute();
}
