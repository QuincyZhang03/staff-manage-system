package com.zjquincy.ncu.blockchain;

import java.io.Serializable;
import java.util.Objects;

public class Transaction<IData> implements Serializable {
    //抽象事务类型，其中T为操作的数据类型，在本项目中可以为Staff或Department
    public enum TransactionType {
        CREATE,
        DELETE,
        UPDATE
    }

    private final TransactionType type;
    private final IData prevData;//修改前的数据
    private final IData afterData;//修改后的数据

    public Transaction(TransactionType type, IData prevData, IData afterData) {
        this.type = type;
        this.prevData = prevData;
        this.afterData = afterData;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type.name(), prevData, afterData);
    }
    //枚举类型的hashCode不固定，必须要用.name()使其固定！
}
