package com.zjquincy.ncu.blockchain;

import java.util.Objects;

public class PlainBlock extends Block {

    private final Transaction<?> message;//区块中的数据

    public PlainBlock(long timestamp, String prev, Transaction<?> message) {
        prevBlockIdentifier = prev;
        this.timestamp = timestamp;
        this.message = message;
    }

    @Override
    public int hashCode() {
        //每个区块的hashcode字段由前驱区块的标识符和本区块的数据进行哈希计算得出
        return Objects.hash(prevBlockIdentifier, message);
    }

    @Override
    public String getIdentifier()  {
        //把时间戳和哈希码连接成字符串作为唯一标识符
        //此处在子类中覆盖hashCode()方法，实际上调用的是子类覆盖后的版本，
        //这也是Java多态的一个体现。
        return timestamp + "@" + hashCode();
    }
}
