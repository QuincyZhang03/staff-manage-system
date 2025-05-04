package com.zjquincy.ncu.blockchain;

public class GenesisBlock extends Block {

    public GenesisBlock(long timestamp) {
        this.timestamp = timestamp;
        this.prevBlockIdentifier = getIdentifier();//空区块链的创世区块，前驱区块是自己
    }

    @Override
    public String getIdentifier() {
        return timestamp + "@genesis";
        //创世区块的标识符用genesis代替区块数据
    }
}
