package com.zjquincy.ncu.blockchain;

import java.io.Serializable;

public abstract class Block implements Serializable {
    //区块类，区块中储存事务信息，一个事务储存在一个区块内

    protected String prevBlockIdentifier;//区块中储存上一个区块的标识符
    protected long timestamp;

    public abstract String getIdentifier();

}
