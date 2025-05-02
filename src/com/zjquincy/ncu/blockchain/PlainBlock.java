package com.zjquincy.ncu.blockchain;

import com.zjquincy.ncu.data.IData;

import java.util.List;
import java.util.Objects;

public class PlainBlock extends Block {

    private final List<Transaction<IData>> message;
    //区块中的数据，可以有若干个事务。官方库的列表已经重写过hashCode方法，可以保证只要元素不变，哈希值就不变。

    public PlainBlock(long timestamp, String prev, List<Transaction<IData>> message) {
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
