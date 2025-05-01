package com.zjquincy.ncu.blockchain;

import com.google.gson.annotations.SerializedName;

public class BlockChainIntegrity {//区块链验证结果，可以返回给客户端，在客户端一侧反序列化得到数据
    @SerializedName("isLegal")
    private final boolean isLegal;
    @SerializedName("metadata")
    private final String metadata;

    public BlockChainIntegrity(boolean isLegal, String metadata) {
        this.isLegal = isLegal;
        this.metadata = metadata;
    }
}