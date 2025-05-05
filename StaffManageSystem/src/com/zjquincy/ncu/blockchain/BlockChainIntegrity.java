package com.zjquincy.ncu.blockchain;

import com.google.gson.annotations.SerializedName;

/*
* 区块链验证结果格式：
* boolean isLegal 区块链是否完整
* String metadata 如完整，提示总区块数量信息；如不完整，提示断裂处区块信息。
* */
public class BlockChainIntegrity {//区块链验证结果，可以返回给客户端，在客户端一侧反序列化得到数据
    @SerializedName("isLegal")
    private final boolean isLegal;
    @SerializedName("metadata")
    private final String metadata;

    public BlockChainIntegrity(boolean isLegal, String metadata) {
        this.isLegal = isLegal;
        this.metadata = metadata;
    }

    @Override
    public String toString() {
        System.out.println("metadata = " + metadata);
        return "isLegal = " + isLegal +" , metadata = " + metadata;
    }
}