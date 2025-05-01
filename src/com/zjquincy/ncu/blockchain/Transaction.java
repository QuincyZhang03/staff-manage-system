package com.zjquincy.ncu.blockchain;

import com.zjquincy.ncu.data.IData;
import java.io.Serializable;
import java.util.Objects;

public class Transaction<T extends IData> implements Serializable {
    //抽象事务类型，其中T为操作的数据类型，在本项目中可以为Staff或Department
    public enum TransactionType {
        CREATE,
        DELETE,
        UPDATE
    }

    private final TransactionType type;
    private final T prevData;//修改前的数据
    private final T afterData;//修改后的数据

    public Transaction(TransactionType type, T prevData, T afterData) {
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
